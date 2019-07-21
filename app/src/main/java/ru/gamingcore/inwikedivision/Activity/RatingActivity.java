package ru.gamingcore.inwikedivision.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.gamingcore.inwikedivision.R;
import ru.gamingcore.inwikedivision.Service.MyService;

public class RatingActivity extends AppCompatActivity {
    private MyService service;
    private TextView proj_name;

    private ServiceConnection sConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = ((MyService.LocalBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Intent i = getIntent();
        proj_name = findViewById(R.id.project_name);
        proj_name.setText(i.getStringExtra("project_name"));
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, sConn, BIND_AUTO_CREATE);
    }

    public void onClick(View view) {
        Toast.makeText(this,"Сохранено",Toast.LENGTH_LONG).show();
        finish();
    }
}
