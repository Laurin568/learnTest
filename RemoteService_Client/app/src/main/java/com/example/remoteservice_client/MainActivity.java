package com.example.remoteservice_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remoteservice_server.IMyAidlInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean isBound = false;
    private IMyAidlInterface myAidlInterface;
    private ServiceConnection mConnection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = findViewById(R.id.ans_tv);
        Button bind_bt = findViewById(R.id.bind_bt);
        Button unbind_bt = findViewById(R.id.unbind_bt);
        Button add_bt = findViewById(R.id.add_bt);
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                myAidlInterface = IMyAidlInterface.Stub.asInterface(iBinder);
//                try {
//                    Toast.makeText(MainActivity.this,String.valueOf(myAidlInterface.Add(1,1)),Toast.LENGTH_LONG).show();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                myAidlInterface = null;

            }
        };
        bind_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!isBound){
                   Intent serviceIntent = new Intent();
//                   ComponentName componentName = new ComponentName("com.example.remoteservice_server",
//                           "com.example.remoteservice_server.MathService");
                   serviceIntent.setAction("com.example.remoteservice_server.MathService");
//                   serviceIntent.setPackage("com.example.remoteservice_server");
//                   serviceIntent.setComponent(componentName);
                  serviceIntent.setClassName("com.example.remoteservice_server","com.example.remoteservice_server.MathService");
//                   Intent eiIntent = new Intent(getExplicitIntent(MainActivity.this,serviceIntent));
                   bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                   isBound = true;
               }
            }
        });
        unbind_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBound){
                   unbindService(mConnection);
                   isBound = false;
                   myAidlInterface = null;
                }
            }
        });

        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myAidlInterface == null){
                    tv.setText("未绑定远程服务");
                    return;
                }
                long a = Math.round(Math.random()*100);
                long b = Math.round(Math.random()*100);
                long result = 0;
                try {
                    result = myAidlInterface.Add(a,b);
                    String msg = String.valueOf(a)+"+"+String.valueOf(b)+"="+String.valueOf(result);
                    tv.setText(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Intent getExplicitIntent(Context context, Intent implicitIntent){
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList =pm.queryIntentServices(implicitIntent,0);
        if(resolveInfoList == null|| resolveInfoList.size()!=1){
            return null;
        }
        ResolveInfo serviceInfo = resolveInfoList.get(0);
        String packagename = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName componentName = new ComponentName(packagename,className);
        Intent explicitInent = new Intent(implicitIntent);
        explicitInent.setComponent(componentName);
        return explicitInent;
    }
}
