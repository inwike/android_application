package ru.gamingcore.inwikedivision;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ru.gamingcore.inwikedivision.network.ServerWork;

public class MyService extends Service {
    private final LocalBinder localBinder = new LocalBinder();
    public JsonData jsonData = new JsonData();

    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public ServerWork serverWork = new ServerWork();


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


}
