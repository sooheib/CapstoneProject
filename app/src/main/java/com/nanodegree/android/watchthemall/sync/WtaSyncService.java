package com.nanodegree.android.watchthemall.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WtaSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();

    private static WtaSyncAdapter sWtaSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sWtaSyncAdapter == null) {
                sWtaSyncAdapter =
                        new WtaSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sWtaSyncAdapter.getSyncAdapterBinder();
    }
}