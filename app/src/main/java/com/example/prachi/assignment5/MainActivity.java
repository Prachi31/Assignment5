package com.example.prachi.assignment5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity {

    private static final String DEBUG_TAG = "HttpExample";
//    private ProgressDialog pDialog;
    String text;
    private TextView data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!= null){
             text = savedInstanceState.getString("TextView");

        }
        else{
            text = "";
        }
        data = (TextView) findViewById(R.id.data);
        data.setText(text);


    }
    protected void onSaveInstanceState(Bundle state){
        state.putString("TextView",data.getText().toString());
        super.onSaveInstanceState(state);
    }
//    public void onConfigurationChanges(Configuration c){
//        super.onConfigurationChanged(c);
////        int mcounter = c.getInt();
//        if(c.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            Toast.makeText(MainActivity.this,"landscape",Toast.LENGTH_SHORT).show();
//
//        }
//    }

    public void downloaddata(View v){
        String StringUrl = "https://www.iiitd.ac.in/about";
        ConnectivityManager cmanager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cmanager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            new DownloadFileFromURL().execute(StringUrl);
        }
        else{
            data.setText("Network connectivity unavailable");
        }


    }

    class DownloadFileFromURL extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Starting download", Toast.LENGTH_SHORT).show();

//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Loading... Please wait...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }


        @Override
        protected String doInBackground(String... StringUrl) {
            URL url = null;
            try {
                url = new URL(StringUrl[0]);
                return download(StringUrl[0]);

            }  catch (IOException e) {
                return "Check your URL again.Unable to retrieve web page.";
            }

        }

        @Override
        protected void onPostExecute(String file_url) {
            Toast.makeText(MainActivity.this,"Download complete", Toast.LENGTH_SHORT).show();
            data.setText(file_url);
        }

    }

    private String download(String StringUrl) throws IOException{
        InputStream input = null;
        int len = 550;
        try{
            URL url = new URL(StringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int res = conn.getResponseCode();
            Log.d(DEBUG_TAG,"The response is: "+res);
            input = conn.getInputStream();
            String content = readIt(input,len);
            Log.d(DEBUG_TAG,"The response is: "+content);
            return content;
        }
        finally {
            if(input != null){
                input.close();
            }
        }
    }

    private String readIt(InputStream input, int len)
                throws IOException, UnsupportedEncodingException{
        Reader reader = null;
        reader = new InputStreamReader(input, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
