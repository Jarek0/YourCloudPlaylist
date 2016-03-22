package com.example.YourCloudPlaylist;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

/**
 * Created by pglg on 28-02-2016.
 */
public class DbApi {
    private static final String APP_KEY = "219201jk4eje28t";
    private static final String APP_SECRET ="zzeam9b1iaxe0mu";
    static boolean logged=false;

    static DropboxAPI<AndroidAuthSession> mDBApi;
    static void initialize(){
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
    }
}
