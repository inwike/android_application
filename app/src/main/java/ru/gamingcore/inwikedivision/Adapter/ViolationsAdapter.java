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

public class ViolationsAdapter extends BaseAdapter {
    private static final String TAG = "INWIKE";
    private LayoutInflater inflater;


    public List<JsonData.Violation> violations = new ArrayList<>();

    public ViolationsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return violations.size();
    }

    @Override
    public JsonData.Violation getItem(int i) {
        return violations.get(i);
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
        tv.setText(violations.get(i).violation_name);
        return convertview;
    }
}
