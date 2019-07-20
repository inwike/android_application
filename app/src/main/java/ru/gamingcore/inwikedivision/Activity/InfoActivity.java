package ru.gamingcore.inwikedivision.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ru.gamingcore.inwikedivision.MyService;
import ru.gamingcore.inwikedivision.ProjAdapter;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.ScreenSlidePagerAdapter;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG ="INWIKE";


    private MyService service;
    private ScreenSlidePagerAdapter pagerAdapter;







    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "MainActivity onServiceConnected");
            service = ((MyService.LocalBinder)binder).getService();
            pagerAdapter.init(service);


            //service.serverWork.setListener(listener);
        }

        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "MainActivity onServiceDisconnected");
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);




        /*projs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/


        ViewPager pager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }
}
