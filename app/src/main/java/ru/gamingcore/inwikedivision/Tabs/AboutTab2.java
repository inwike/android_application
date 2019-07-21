package ru.gamingcore.inwikedivision.Tabs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.gamingcore.inwikedivision.Adapter.AllowsAdapter;
import ru.gamingcore.inwikedivision.Adapter.BuildsAdapter;
import ru.gamingcore.inwikedivision.Utils.JsonData;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.Service.MyService;

class AboutTab2 extends DialogFragment {

    private static final String TAG = "INWIKE";
    private AllowsAdapter allowsAdapter;
    private BuildsAdapter buildsAdapter;



    public JsonData jsonData;

    private TextView exec_name;
    private TextView position_name;
    private TextView org_name;
    private TextView  proj_name;


    private ImageView Exec_foto;
    private ImageView check;


    private ListView allows;
    private ListView builds;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_page_2, container,false);
        exec_name = v.findViewById(R.id.exec_name);
        position_name = v.findViewById(R.id.position_name);
        org_name = v.findViewById(R.id.org_name);
        allows = v.findViewById(R.id.allows);
        builds = v.findViewById(R.id.builds);
        Exec_foto = v.findViewById(R.id.Exec_foto);
        proj_name = v.findViewById(R.id.proj_name);
        check = v.findViewById(R.id.check);

        if(getContext()!= null) {
            allowsAdapter = new AllowsAdapter(getContext());
            buildsAdapter = new BuildsAdapter(getContext());
        }

        exec_name.setText(jsonData.exec_name);


        org_name.setText(jsonData.org_name);
        Exec_foto.setImageBitmap(jsonData.Exec_foto);


        allows.setAdapter(allowsAdapter);
        builds.setAdapter(buildsAdapter);
        return v;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    public void init(MyService service) {
        this.jsonData = service.jsonData;
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onResume() {
        if(getDialog()!=null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                window.setAttributes(params);
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                );
            }
        }
        super.onResume();
    }

    public void setPosition(int position) {
        allowsAdapter.allows = jsonData.projs.get(position).allowances;
        buildsAdapter.builds = jsonData.projs.get(position).builds;
        buildsAdapter.notifyDataSetInvalidated();
        allowsAdapter.notifyDataSetInvalidated();
        position_name.setText(jsonData.position_name);
        proj_name.setText(jsonData.projs.get(position).proj_name);

        if(!jsonData.projs.get(position).check)
            check.setImageResource(R.drawable.red);
        else
            check.setImageResource(R.drawable.green);

    }

}
