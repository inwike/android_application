package ru.gamingcore.inwikedivision.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonData {
    private static final String TAG = "INWIKE";

    public String exec_name;
    public String position_name;
    public String exec_foto;
    public String org_name;
    public List<Proj> projs;

    public Bitmap Exec_foto = null;


    public class Build {
        public String name_builds;
        public String address;
        public String latitude;
        public String longitude;
    }

    public class Allowance {
        public String id_allow;
        public String name_allow;
        public String start_date;
        public String stop_date;
        public boolean check;
        public boolean avail;
    }

    public class Proj {
        public String proj_id;
        public String proj_name;
        public List<Allowance> allowances;
        public List<Build> builds;
        public boolean check;
    }

    public void Parse(JSONObject obj) {
        try {
            exec_name = obj.getString("exec_name");
            position_name = obj.getString("position_name");
            exec_foto = obj.getString("exec_foto");

            byte[] buf = Base64.decode(exec_foto,Base64.NO_WRAP);
            Exec_foto = BitmapFactory.decodeByteArray(buf, 0, buf.length);

            org_name = obj.getString("org_name");

            JSONArray projs = obj.getJSONArray("projs");
            this.projs = new ArrayList<>(projs.length());

            for (int i = 0; i < projs.length(); i++) {
                JSONObject data = projs.getJSONObject(i);

                Proj proj = new Proj();
                proj.proj_id = data.getString("proj_id");
                proj.proj_name = data.getString("proj_name");
                proj.check = data.getBoolean("check");

                JSONArray allowances = data.getJSONArray("allowance");
                proj.allowances = new ArrayList<>(allowances.length());

                for (int j = 0; j < allowances.length(); j++) {
                    JSONObject data2 = allowances.getJSONObject(j);
                    Allowance allowance = new Allowance();
                    allowance.id_allow = data2.getString("id_allow");
                    allowance.name_allow = data2.getString("name_allow");
                    allowance.start_date = data2.getString("start_date");
                    allowance.stop_date = data2.getString("stop_date");
                    allowance.check = data2.getBoolean("check");
                    allowance.avail = data2.getBoolean("avail");
                    proj.allowances.add(allowance);
                }

                JSONArray builds = data.getJSONArray("builds");

                proj.builds = new ArrayList<>(builds.length());

                for (int k = 0; k < builds.length(); k++) {
                    JSONObject data3 = builds.getJSONObject(k);

                    Build build = new Build();

                    build.address = data3.getString("address");
                    build.name_builds = data3.getString("name_builds");

                    build.latitude = data3.getString("latitude");
                    build.longitude = data3.getString("longitude");
                    proj.builds.add(build);
                }
                this.projs.add(proj);
            }
            Log.e(TAG,"Fully readed");

        } catch (JSONException e) {
            Log.e(TAG,"JSONException "+e.getLocalizedMessage());
        }
    }

}
