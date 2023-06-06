package com.example.smsmarketing;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class updateClass extends AsyncTask<String, String, String> {

   static String id ;
   static String Status ;


    public static void getid(String object1) {

                id = new MainActivity().getObject1(object1);
                System.out.println("id " + id);

    }

    public static void getstatus(String status){
                Status = new MainActivity().getStatus(status);
                System.out.println("status " + Status);
    }



    @Override
    public String doInBackground(String... uri) {

        String responseString = null;
        try {

            URL url = new URL("https://sms-testing.tus-appli.com/updatestatus.php?id="+id+"&status="+Status);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn.getResponseCode() == HttpsURLConnection.HTTP_OK){

            }
            else {
                responseString = "FAILED"; // See documentation for more info on response handling
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}








        /*try{

            String status="1";
            URL url = new URL("https://sms-testing.tus-appli.com/updatestatus.php?id="+id+"&status="+status);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG", conn.getResponseMessage());

            conn.disconnect();




        } catch (Exception e) {
            e.printStackTrace();
        }

         */





































