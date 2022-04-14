package com.example.whatistheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    EditText city;
    TextView result;


    @SuppressLint("StaticFieldLeak")
    public class donwload extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            String results = "";
            URL url;
            HttpURLConnection connection;
            try{
                url =new URL(urls[0]);
                connection=(HttpURLConnection) url.openConnection();
                InputStream in =connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data !=-1){
                    char corrent=(char) data;
                    results += corrent;
                    data=reader.read();
                }
                return results;


            }catch (Exception e){
                e.printStackTrace();
                return "Invalid city name :(\n pls try again";
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String msg="";
            msg= weatherInfos(s)+"\n"+tempInfos(s);
           result.setText(msg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=findViewById(R.id.imageView);
        city=findViewById(R.id.cityText);
        result=findViewById(R.id.textView2);
    }
        // fct de button
    public void weather(View view){
        donwload task=new donwload();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&appid=0ef6f3bd5816e0143c28f7ef94de7a7c");
       // hide the keybord after write the city name
        InputMethodManager mng=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mng.hideSoftInputFromWindow(city.getWindowToken(),0);
    }
//if the utilsateur enter a bad information
    //get information about weather from JSON
    public String weatherInfos(String s){
        JSONObject jsonPart = null;
        String msg="";
        try{
            JSONObject json=new JSONObject(s);
            String weatherInfo=json.getString("weather");
            JSONArray arr=new JSONArray(weatherInfo);
            for(int i=0;i<arr.length();i++){
                jsonPart=arr.getJSONObject(i);
            }
            msg= jsonPart.getString("main")+":"+jsonPart.getString("description");
            if(msg.equals("")){
                result.setText("Invalid city name :(\n pls try again");
            }
            return msg;
        }catch(Exception e){
            e.printStackTrace();
            return "Invalid city name :(\n pls try again";
        }
    }

    //get information about temp from JSON
    public String tempInfos(String s){
        JSONObject jsonPart = null;
        int temp;
        int temp_min;
        int temp_max;
        String msg="";
        try{
            JSONObject json=new JSONObject(s);
            String weatherInfo=json.getString("main");
            Log.i("!!!!!!!",weatherInfo);
            JSONArray arr=new JSONArray("["+weatherInfo+"]");
            for(int i=0;i<arr.length();i++){
                jsonPart=arr.getJSONObject(i);
            }
            //converting temp
            temp=(int)Math.ceil((jsonPart.getDouble("temp")-273.15));
            temp_min=(int)Math.ceil((jsonPart.getDouble("temp_min")-273.15));
            temp_max=(int)Math.ceil((jsonPart.getDouble("temp_max")-273.15));

            msg= "temp:"+String.valueOf(temp)+"°C\n" +
                    "temp_min:"+String.valueOf(temp_min)+"°C\n"+
                    "temp_max:"+String.valueOf(temp_max)+"°C"
                    ;
            if(msg.equals("")){
                return "";
            }
            return msg;
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

}