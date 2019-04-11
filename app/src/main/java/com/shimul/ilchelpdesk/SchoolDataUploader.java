package com.shimul.ilchelpdesk;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SchoolDataUploader extends AsyncTask<String, Void , String> {
    //private static final String whereToUpload = "http://103.234.26.37:8080/shimul/insert.php";
    private static final String whereToUpload = "http://www.foosociety.com/rest/v1/ticket/create";
    private AlertDialog alertDialog;

    public SchoolDataUploader(Context ctx)
    {
        alertDialog = new AlertDialog.Builder(ctx).create();
    }

    @Override
    protected String doInBackground(String... params) {

        //String district =  params[0];
        String school =  params[0];

        //String comment =  params[2];
        String title1 =  params[1];
        String description1 =  params[2];
        String reporter =  params[3];
        String assaginer =  params[4];


        try {
            URL url = new URL(whereToUpload);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String postData = URLEncoder.encode("schoolId","UTF-8")+"="+URLEncoder.encode(school,"UTF-8")+"&"+
                    URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title1,"UTF-8")+"&"+
                    URLEncoder.encode("description","UTF-8")+"="+URLEncoder.encode(description1,"UTF-8")+"&"+
                    URLEncoder.encode("reporterEmail","UTF-8")+"="+URLEncoder.encode(reporter,"UTF-8")+"&"+
                    URLEncoder.encode("assigneeEmail","UTF-8")+"="+URLEncoder.encode(assaginer,"UTF-8");
            bufferedWriter.write(postData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            //receives feedback
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line="";
            while((line = bufferedReader.readLine()) != null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();;
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "Problem occurs";
    }


    @Override
    protected void onPreExecute() {
        alertDialog.setTitle("Uploading comments");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
