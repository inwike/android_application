package ru.gamingcore.inwikedivision.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.gamingcore.inwikedivision.Utils.JsonData;
import ru.gamingcore.inwikedivision.R;

public class BuildsAdapter extends BaseAdapter {
    private static final String TAG ="INWIKE";

    public List<JsonData.Build> builds = new ArrayList<>();
    private LayoutInflater inflater = null;

    public int current = 0;

    public BuildsAdapter(Activity activity) {
        inflater = activity.getLayoutInflater();
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
        return current;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = inflater.inflate(R.layout.list_row_builds, viewGroup,false);

        TextView allow_name = view.findViewById(R.id.build_name);
        allow_name.setText(getItem(i).name_builds);

        return view;
    }
}
