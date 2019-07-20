package ru.gamingcore.inwikedivision.Adapter;

import android.app.Activity;
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

public class ProjAdapter extends BaseAdapter {
    private static final String TAG ="INWIKE";


    public List<JsonData.Proj> projs = new ArrayList<>();
    private LayoutInflater inflater = null;

    public int current = 0;

    public ProjAdapter(Activity activity) {
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return projs.size();
    }

    @Override
    public JsonData.Proj getItem(int i) {
        return projs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return current;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = inflater.inflate(R.layout.list_row_projs, viewGroup,false);

        TextView proj_name = view.findViewById(R.id.proj_name);
        proj_name.setText(getItem(i).proj_name);

        ImageView check = view.findViewById(R.id.check);

        if(!getItem(i).check) {
            check.setImageResource(R.drawable.red);
        } else {
            check.setImageResource(R.drawable.green);
        }

        return view;
    }
}
