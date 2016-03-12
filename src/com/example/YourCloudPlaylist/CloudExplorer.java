package com.example.YourCloudPlaylist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import java.util.ArrayList;

/**
 * Created by pglg on 12-03-2016.
 */
public class CloudExplorer extends AsyncTask<Void, Void, DropboxAPI.Entry> {

    public DropboxEntryAsyncResponse delegate = null;
    private String path;
    private Handler handler;

    public CloudExplorer(DropboxEntryAsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected DropboxAPI.Entry doInBackground(Void... params) {
        DropboxAPI.Entry directory=new DropboxAPI.Entry();
        try {
            directory = DbApi.mDBApi.metadata(path, 1000, null, true, null);

        } catch (DropboxException e) {
            e.printStackTrace();
        }
        return directory;
    }

    @Override
    protected void onPostExecute(DropboxAPI.Entry directory) {
       delegate.processFinish(directory);
    }
}
