package ru.gamingcore.inwikedivision.network;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerWork {
    private static final String TAG = "INWIKE";

    private static final int GET_UID = 0;

    private boolean loaded = false;
    private boolean started = false;

    private static final String[] methods =
            {"exec_data"};

    private static final String UID = "?exec_uid=";

    private ServerListener listener;

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    public interface ServerListener {
        void onFinished(JSONObject obj);
        void onError();
    }

    public void connect(String uid) {
        if(started)
            return;

            String url = "http://192.168.1.35/Inwike/hs/Inwike/ID/";
            started = true;

            GetJsonAsync dataAsync = new GetJsonAsync();
            dataAsync.setListener(listner);
            dataAsync.execute(url,methods[GET_UID].concat(UID.concat(uid)),"web:web","GET");
    }

    private GetJsonAsync.AsyncTaskListener listner = new GetJsonAsync.AsyncTaskListener() {
        @Override
        public void onFinished(String result) {
            onJson(result);
        }

        @Override
        public void onError() {
            started = false;
        }
    };

    private void onJson(String result) {
        try {
            if(result == null) {
                throw new JSONException("null string");
            }
            JSONObject obj = new JSONObject(result);
            if (listener != null && !loaded) {
                loaded = true;
                listener.onFinished(obj);
            }
        } catch (JSONException e) {
            if (listener != null) {
                listener.onError();
            }
        }
        started = false;
    }
}
