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

public class AllowsAdapter extends BaseAdapter {
    private static final String TAG ="INWIKE";


    public List<JsonData.Allowance> allows = new ArrayList<>();
    private LayoutInflater inflater = null;


    public int current = 0;

    public AllowsAdapter(Activity activity) {
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return allows.size();
    }

    @Override
    public JsonData.Allowance getItem(int i) {
        return allows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return current;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = inflater.inflate(R.layout.list_row_allows, viewGroup,false);

        TextView allow_name = view.findViewById(R.id.allow_name);
        allow_name.setText(getItem(i).name_allow);

        ImageView check = view.findViewById(R.id.check);

        if(! getItem(i).check)
            check.setImageResource(R.drawable.red);
        else
            check.setImageResource(R.drawable.green);

        return view;
    }
}
