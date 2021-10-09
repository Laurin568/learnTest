package com.example.remoteservice_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class MathService extends Service {

    private final IMyAidlInterface.Stub mBinder  = new IMyAidlInterface.Stub() {
        @Override
        public long Add(long a, long b) throws RemoteException {
            Log.i("server","AIDL request sent from the client: a->"+a+"::b->"+b);
            return a+b;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(MathService.this,"远程绑定:MathService",Toast.LENGTH_SHORT).show();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(MathService.this,"取消远程绑定",Toast.LENGTH_SHORT).show();
        return false;
    }
}
