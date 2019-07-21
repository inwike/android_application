package ru.gamingcore.inwikedivision.network;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerWork {
    private static final String TAG = "INWIKE";

    private static final String verif_id = "66b457e4-7c02-11e2-9362-001b11b25590";
    private static final String HOST = "http://192.168.1.35/Inwike/hs/Inwike/ID";
    private static final String[] methods =
            {"exec_data","allow_scan","create_violation","list_violation"};
    private static final String UID = "?exec_uid=";


    private static final int GET_UID = 0;
    private static final int GET_SCAN = 1;
    private static final int SET_VIO = 2;
    private static final int GET_VIO = 3;

    public Bitmap tempScan;

    private boolean loaded = false;
    private boolean started = false;

    private String current_uid;



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

            current_uid = uid;
            started = true;

            GetJsonAsync dataAsync = new GetJsonAsync();
            dataAsync.setListener(listner);
            dataAsync.execute(HOST,methods[GET_UID].concat(UID.concat(current_uid)),"web:web","GET");
    }

    public void getScan(String allow_id) {
        GetJsonAsync dataAsync = new GetJsonAsync();
        dataAsync.setListener(listner);
        dataAsync.execute(HOST,methods[GET_SCAN].concat(UID.concat(current_uid).concat("&allow_id=".concat(allow_id))),"web:web","GET");
    }


    public void getVio() {
        GetJsonAsync dataAsync = new GetJsonAsync();
        dataAsync.setListener(listner);
        dataAsync.execute(HOST,methods[GET_VIO],"web:web","GET");
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
                loaded = false;
            }
        } catch (JSONException e) {
            if (listener != null) {
                listener.onError();
            }
        }
        started = false;
    }

    //(c) http://www.pocketmagic.net/?p=1662
    private String m_szDevIDShort = "35" + //we make this look like a valid IMEI
            Build.BOARD.length()%10+ Build.BRAND.length()%10 +
            Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
            Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
            Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
            Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
            Build.TAGS.length()%10 + Build.TYPE.length()%10 +
            Build.USER.length()%10 ; //13 digits
}