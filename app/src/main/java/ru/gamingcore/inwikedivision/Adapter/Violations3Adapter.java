package ru.gamingcore.inwikedivision.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.gamingcore.inwikedivision.Utils.JsonData;

public class Violations3Adapter extends BaseAdapter {
    private static final String TAG = "INWIKE";
    private LayoutInflater inflater;

    public List<JsonData.Build> builds = new ArrayList<>();

    public Violations3Adapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return builds.size();
    }

    @Override
    public JsonData.Build getItem(int i) {
        return builds.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
        if (convertview == null) {
            convertview = inflater.inflate(android.R.layout.simple_spinner_item, null);
        }

        TextView tv = (TextView) convertview;
        tv.setText(builds.get(i).name_builds);
        return convertview;
    }
}
