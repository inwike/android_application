package ru.gamingcore.inwikedivision.network;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public  class GetJsonAsync extends AsyncTask<String,Void, String> {
    private static final String TAG = "INWIKE";

    private AsyncTaskListener listener;

    public void setListener(AsyncTaskListener listener) {
        this.listener = listener;
    }

    public interface AsyncTaskListener {
        void onFinished(String info);
        void onError();
    }

    @Override
    protected String  doInBackground(String... data) {
        String answer = "";
        try {

            URL url = new URL (data[0].concat(data[1]));

            byte[] encoding = Base64.encode(data[2].getBytes(),Base64.NO_WRAP);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(data[3]);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; param=value");
            connection.setRequestProperty("Authorization", "Basic " + new String(encoding));

            if(data[3].contains("POST")) {
                connection.setDoOutput(true);
                connection.setDoInput(true);
                OutputStream os = connection.getOutputStream();
                os.write(data[4].getBytes(StandardCharsets.UTF_8));
                connection.connect();
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // все ок
            } else {
                Log.d(TAG, "Error "+connection.getResponseCode());
                if (listener != null) {
                    listener.onError();
                }
                return null;
            }


            InputStream content = connection.getInputStream();
            BufferedReader in   =
                    new BufferedReader (new InputStreamReader(content));

            String line;
            while ((line = in.readLine()) != null) {
                answer = answer.concat(line);
            }
        }  catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException "+e.getLocalizedMessage());
            if (listener != null) {
                listener.onError();
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException "+e.getLocalizedMessage());
            if (listener != null) {
                listener.onError();
            }
        } catch(IOException e) {
            Log.e(TAG, "IOException "+e.getLocalizedMessage());
            if (listener != null) {
                listener.onError();
            }
        }
        return answer;
    }

    @Override
    protected void onPostExecute(String answer) {
        if (listener != null) {
            listener.onFinished(answer);
        }
    }
}
