package ru.gamingcore.inwikedivision;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter  extends BaseAdapter {
    public List<JsonData.Build> tasks = new ArrayList<>();
    private LayoutInflater inflater = null;

    public int current = 0;

    public TaskAdapter(Activity activity) {
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public JsonData.Build getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return current;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
            view = inflater.inflate(R.layout.list_row, viewGroup,false);

    /*    TextView title = view.findViewById(R.id.title); // title
        TextView artist = view.findViewById(R.id.artist); // artist name

        String singer = data.get(position);
        if(singer != null)
            title.setText(singer);

        if(type && songs != null) {

            Song song = songs.get(singer);
            if(song != null)
                artist.setText(song.singer);
        }*/

        return view;
    }
}
