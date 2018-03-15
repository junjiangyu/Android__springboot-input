package com.example.johnny.myapplication;

import android.app.AlertDialog;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    String name;
    EditText nameinput;
    Button submitbtn;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //following two line enable the use of network on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        nameinput = (EditText) findViewById(R.id.nameInput);
        submitbtn = (Button) findViewById(R.id.button);
        result = (TextView) findViewById(R.id.resultview);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameinput.getText().toString();
                TextView outputview = (TextView) findViewById(R.id.resultview);
                Toast.makeText(MainActivity.this, "Your input is:"+ name, Toast.LENGTH_LONG).show();

                //sending the name parameter to internet
                try {
                    URL url = new URL("https://jersonblueapp.cfapps.io/?name="+ name);
                    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    DataOutputStream dstream = new DataOutputStream(connection.getOutputStream());
                    dstream.writeBytes(name);
                    dstream.flush();
                    dstream.close();

                    //Get response from url
                    int responseCode = connection.getResponseCode();
                    String output = "Request URL: " + url;
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseOutput = new StringBuilder();
                    String line ="";
                    while ((line = br.readLine()) != null){
                        responseOutput.append(line);
                    }
                    br.close();
                    output += System.getProperty("line.separator") + "The URL content: "+ responseOutput.toString();

                    outputview.setText(output);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });

    }
}
