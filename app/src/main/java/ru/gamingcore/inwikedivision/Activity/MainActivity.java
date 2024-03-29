package ru.gamingcore.inwikedivision.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import ru.gamingcore.inwikedivision.Finger.FingerprintDialog;
import ru.gamingcore.inwikedivision.Service.MyService;
import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.ViolationFragment;
import ru.gamingcore.inwikedivision.network.ServerWork;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="INWIKE";
    private static int PERMISSION_REQUEST_CODE = 123456;
    public static int primaryColor;
    public static float WidthPixels;
    public static float HeightPixels;


    private LocationManager locationManager;
    private MyLocationListener locationListener;
    private MyService service;

    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((MyService.LocalBinder)binder).getService();
            service.serverWork.setListener(listener);
            service.serverWork.getVio();


            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        PERMISSION_REQUEST_CODE);
                return;
            }

            Init();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clear_layout);
        primaryColor = getColor(R.color.colorPrimary);
        Display d = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        WidthPixels = realDisplayMetrics.widthPixels;
        HeightPixels = realDisplayMetrics.heightPixels;

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    @SuppressLint("MissingPermission")
    private void Init() {
        FingerprintDialog dialog = new FingerprintDialog();
        dialog.setCancelable(false);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
                setContentView(R.layout.activity_main);
                fm.unregisterFragmentLifecycleCallbacks(this);
            }
        }, false);

        dialog.show(getSupportFragmentManager(),"FingerprintDialog");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager
                .NETWORK_PROVIDER, 50000, 500, locationListener);
    }

    public void onClick(View view) {
        if(view.getId() == R.id.read) {
            Intent intent = new Intent(this, QRActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.error){
            ViolationFragment  violation = new ViolationFragment();
            violation.service = service;
            violation.show(getSupportFragmentManager(), "Violation");
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            service.jsonData.Latitude = location.getLatitude();
            service.jsonData.Longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Init();
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private ServerWork.ServerListener listener = new ServerWork.ServerListener() {

        @Override
        public void onFinished(JSONObject obj) {
            service.jsonData.Parse(obj);
        }

        @Override
        public void onError() {

        }
    };

}