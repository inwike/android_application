package ru.gamingcore.inwikedivision.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import ru.gamingcore.inwikedivision.Service.MyService;
import ru.gamingcore.inwikedivision.Utils.NonSwipeableViewPager;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.Tabs.ScreenSlidePagerAdapter;
import ru.gamingcore.inwikedivision.network.ServerWork;

public class InfoActivity extends AppCompatActivity {
    private static final String TAG ="INWIKE";
    private NonSwipeableViewPager pager;

    private MyService service;
    private ScreenSlidePagerAdapter pagerAdapter;

    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(TAG, "MainActivity onServiceConnected");
            service = ((MyService.LocalBinder)binder).getService();
            pagerAdapter.init(service);
            service.serverWork.setListener(listner);
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

        pager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);
        pager.addOnPageChangeListener(pagerListener);
        pager.setAllowedSwipeDirection(NonSwipeableViewPager.SwipeDirection.left);
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }


    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
            pager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pagerAdapter.setPosition(i,pager.getCurrentItem() + 1);
                    pager.setCurrentItem(pager.getCurrentItem()+1,true);
                }
            }, 10);
        }
    };

    ViewPager.OnPageChangeListener pagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position == 0) {
                ListView projs = pager.findViewById(R.id.projs);
                if(projs != null) {
                    projs.setOnItemClickListener(listener);
                }
            } else if(position == 1) {
                ListView allows = pager.findViewById(R.id.allows);
                if(allows != null) {
                    allows.setOnItemClickListener(listener);
                }
            } else if(position == 2) {
                Button btn =  pager.findViewById(R.id.btn);
                if(btn != null) {
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            service.serverWork.getScan( pagerAdapter.getAllowID());
                        }
                    });
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

        private void startScan() {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        }

    private ServerWork.ServerListener listner = new ServerWork.ServerListener() {

        @Override
        public void onFinished(JSONObject obj) {
            String Scan = null;
            try {
                Scan = obj.getString("Scan");
                byte[] buf = Base64.decode(Scan,Base64.NO_WRAP);
                service.serverWork.tempScan = BitmapFactory.decodeByteArray(buf, 0, buf.length);
                startScan();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError() {
            Log.e(TAG,"onError");
        }
    };
}
