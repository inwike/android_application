package ru.gamingcore.inwikedivision.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.gamingcore.inwikedivision.Utils.JsonData;
import ru.gamingcore.inwikedivision.R;

public class Violations2Adapter extends BaseAdapter {
    private static final String TAG = "INWIKE";
    private LayoutInflater inflater;


    public List<JsonData.Proj> projs = new ArrayList<>();

    public Violations2Adapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return projs.size();
    }

    @Override
    public String getItem(int i) {
        return "test";
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
        tv.setText(projs.get(i).proj_name);
        return convertview;
    }
}
