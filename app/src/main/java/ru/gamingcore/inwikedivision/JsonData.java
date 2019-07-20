package ru.gamingcore.inwikedivision;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonData {

    public String exec_name;
    public List<Build> builds;//=new Array
    public List<Allowance> allowances;//=new Array
    public String exec_foto;
    public String org_name;


    class Build {
        public String name_builds;
        public String proj_id;
        public String proj_name;
        public String address;
    }

    class Allowance {
        public String name_allow;
        public String start_date;
        public String stop_date;
    }


    public void Parse(JSONObject obj) {
        try {
            exec_name = obj.getString("exec_name");
            exec_foto = obj.getString("exec_foto");
            org_name = obj.getString("org_name");

            JSONArray builds = obj.getJSONArray("builds");
            this.builds = new ArrayList<>(builds.length());

            for (int i = 0; i < builds.length(); i++) {
                JSONObject data = builds.getJSONObject(i);
                Build build = new Build();
                build.address = data.getString("address");
                build.name_builds = data.getString("name_builds");
                build.proj_id = data.getString("proj_id");
                build.proj_name = data.getString("proj_name");
            }


            JSONArray allowances = obj.getJSONArray("allowances");
            this.allowances = new ArrayList<>(allowances.length());

            for (int i = 0; i < builds.length(); i++) {
                JSONObject data = builds.getJSONObject(i);
                Allowance allowance = new Allowance();
                allowance.name_allow = data.getString("name_allow");
                allowance.start_date = data.getString("start_date");
                allowance.stop_date = data.getString("stop_date");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
