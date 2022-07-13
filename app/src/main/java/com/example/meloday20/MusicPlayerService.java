package com.example.meloday20;

import java.security.Provider;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {

    private final IBinder mBinder = new PlayerBinder();
    private PreviewPlayer mPlayer = new PreviewPlayer();

    public static Intent getIntent(Context context) {
        return new Intent(context, MusicPlayerService.class);
    }

    public class PlayerBinder extends Binder {
        public MusicPlayer getService() {
            return mPlayer;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mPlayer.release();
        super.onDestroy();
    }
}