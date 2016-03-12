package com.example.YourCloudPlaylist;

import com.dropbox.client2.DropboxAPI;

/**
 * Created by pglg on 12-03-2016.
 */
//class that implements it can get dropboxapi.entry from async task

interface DropboxEntryAsyncResponse {
    void processFinish(DropboxAPI.Entry output);
}
