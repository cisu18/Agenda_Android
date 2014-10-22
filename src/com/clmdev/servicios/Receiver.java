package com.clmdev.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context,ServicioAlerta.class);
        if(!ServicioAlerta.isRunning())context.startService(serviceIntent);
    }
}
