package com.example.bidorbuy.bidorbuy;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    //Declare global objects
    private Button btn_search;
    private EditText txt_search;
    private TextView view_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the Id's of the button, Edit text and the text view
        btn_search = (Button) findViewById(R.id.btn_search);
        txt_search = (EditText) findViewById(R.id.txt_search);
        view_search = (TextView) findViewById(R.id.search_view);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JSONTask().execute("http://services.groupkt.com/country/get/iso3code/IND");
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            //Get the URL connection
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                //Get a stream and store it in a stream object
                InputStream stream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {

                    buffer.append(line);
                }
                String finalJSON = buffer.toString();
                JSONObject JSONParent = new JSONObject(finalJSON);
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                //Check if connection is not null,then disconnect and buffered reader is not null, then close
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            view_search.setText(results);
        }
    }

}
