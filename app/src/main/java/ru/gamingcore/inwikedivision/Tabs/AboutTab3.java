package ru.gamingcore.inwikedivision.Tabs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import ru.gamingcore.inwikedivision.Utils.JsonData;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.Service.MyService;

import static ru.gamingcore.inwikedivision.Activity.MainActivity.primaryColor;

class AboutTab3 extends DialogFragment {

    private static final String TAG = "INWIKE";

    public JsonData jsonData;
    public String id_allow;


    private TextView exec_name;
    private TextView position_name;
    private TextView org_name;
    private TextView  proj_name;
    private TextView  start_date;
    private TextView  stop_date;
    private TextView  allow_name;

    private ImageView Exec_foto;
    private ImageView check;
    private ImageView check2;
    private View green;
    private View red;




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.about_page_3, container,false);
        exec_name = v.findViewById(R.id.exec_name);
        position_name = v.findViewById(R.id.position_name);
        org_name = v.findViewById(R.id.org_name);
        Exec_foto = v.findViewById(R.id.Exec_foto);
        proj_name = v.findViewById(R.id.proj_name);
        start_date = v.findViewById(R.id.start_date);
        stop_date = v.findViewById(R.id.stop_date);
        allow_name = v.findViewById(R.id.allow_name);
        check = v.findViewById(R.id.check);
        check2 = v.findViewById(R.id.check2);
        red = v.findViewById(R.id.red);
        green = v.findViewById(R.id.green);

        exec_name.setText(jsonData.exec_name);
        org_name.setText(jsonData.org_name);
        Exec_foto.setImageBitmap(jsonData.Exec_foto);

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
        if(getDialog() != null) {
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

    public void setPosition(int proj_position,int allow_position) {
        position_name.setText(jsonData.position_name);
        proj_name.setText(jsonData.projs.get(proj_position).proj_name);

        JsonData.Allowance allowance = jsonData.projs.get(proj_position).allowances.get(allow_position);
        allow_name.setText(allowance.name_allow);
        start_date.setText(allowance.start_date);
        stop_date.setText(allowance.stop_date);

        id_allow = allowance.id_allow;

        if(!jsonData.projs.get(proj_position).check)
            check.setImageResource(R.drawable.red);
        else
            check.setImageResource(R.drawable.green);

        if(!allowance.check) {
            check2.setImageResource(R.drawable.red);
            stop_date.setTextColor(0xFFFF0000);
        } else {
            stop_date.setTextColor(primaryColor);
            check2.setImageResource(R.drawable.green);
        }

        if(!allowance.avail) {
            red.setVisibility(View.VISIBLE);
            green.setVisibility(View.GONE);
        } else {
            green.setVisibility(View.VISIBLE);
            red.setVisibility(View.GONE);
        }
    }

}
