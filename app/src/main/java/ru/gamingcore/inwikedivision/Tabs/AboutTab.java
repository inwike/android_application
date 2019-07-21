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

import ru.gamingcore.inwikedivision.Utils.JsonData;
import ru.gamingcore.inwikedivision.Adapter.ProjAdapter;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.Service.MyService;

class AboutTab extends androidx.fragment.app.DialogFragment {

    private ProjAdapter projAdapter;

    public JsonData jsonData;

    private TextView exec_name;
    private TextView position_name;
    private TextView org_name;

    private ImageView Exec_foto;

    private ListView projs;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_page, container,false);
        exec_name = v.findViewById(R.id.exec_name);
        position_name = v.findViewById(R.id.position_name);
        org_name = v.findViewById(R.id.org_name);
        projs = v.findViewById(R.id.projs);
        Exec_foto = v.findViewById(R.id.Exec_foto);

        if(getActivity()!= null) {
            projAdapter = new ProjAdapter( getContext());
        }


        exec_name.setText(jsonData.exec_name);

        position_name.setText(jsonData.position_name);

        org_name.setText(jsonData.org_name);

        projAdapter.projs = jsonData.projs;

        Exec_foto.setImageBitmap(jsonData.Exec_foto);

        projs.setAdapter(projAdapter);

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
}
