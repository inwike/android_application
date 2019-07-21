package ru.gamingcore.inwikedivision;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import ru.gamingcore.inwikedivision.Adapter.Violations2Adapter;
import ru.gamingcore.inwikedivision.Adapter.Violations3Adapter;
import ru.gamingcore.inwikedivision.Adapter.ViolationsAdapter;
import ru.gamingcore.inwikedivision.Service.MyService;
import ru.gamingcore.inwikedivision.Utils.JsonData;

import static ru.gamingcore.inwikedivision.Activity.MainActivity.HeightPixels;
import static ru.gamingcore.inwikedivision.Activity.MainActivity.WidthPixels;


public class ViolationFragment extends DialogFragment implements OnClickListener{
    private static final String TAG = "INWIKE";
    public MyService service;

    private Spinner vSpinner;
    private Spinner pSpinner;
    private Spinner bSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("");
        View v = inflater.inflate(R.layout.violation_fragment, container,false);
        v.findViewById(R.id.save).setOnClickListener(this);

        vSpinner = v.findViewById(R.id.vSpinner);
        pSpinner = v.findViewById(R.id.pSpinner);
        bSpinner = v.findViewById(R.id.bSpinner);

        ViolationsAdapter vAdapter;
        Violations2Adapter pAdapter;
        final Violations3Adapter bAdapter;

        vAdapter = new ViolationsAdapter(getContext());
        pAdapter = new Violations2Adapter(getContext());
        bAdapter = new Violations3Adapter(getContext());

        vAdapter.violations = service.jsonData.violations;

        pAdapter.projs = service.jsonData.activeProjs;

        if(pAdapter.projs .size() > 0)
            bAdapter.builds = pAdapter.projs.get(0).activeBuilds;

        vSpinner.setAdapter(vAdapter);
        pSpinner.setAdapter(pAdapter);
        bSpinner.setAdapter(bAdapter);

        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.save) {
            Toast.makeText(getContext(),"Нарушение добавлено",Toast.LENGTH_LONG).show();;
            dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public void onResume() {
        if(getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = (int) (WidthPixels - HeightPixels * 0.05f);
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

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    private boolean shown;

    public boolean isShown() {
        return shown;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!shown) {
            this.shown = true;
            super.show(manager, tag);
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (!shown) {
            this.shown = true;
            return super.show(transaction, tag);
        }

        // TODO: titlebar_bg what happens when -1 is returned
        // it may be better to store the backStackId from the first show() and return it
        return -1;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        this.shown = false;
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        this.shown = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        this.shown = false;
    }
}