package com.example.YourCloudPlaylist;

import com.dropbox.client2.DropboxAPI;

/**
 * Created by pglg on 12-03-2016.
 */
public class DropboxFile implements MyFile {

    DropboxAPI.Entry outputFromDropbox;

    @Override
    public String getRoot() {
        return null;
    }

    @Override
    public String getHome() {
        return null;
    }

    @Override
    public void setPath(String path) {
        CloudExplorer asyncTask = (CloudExplorer) new CloudExplorer(new DropboxEntryAsyncResponse() {
            @Override
            public void processFinish(DropboxAPI.Entry output) {
                outputFromDropbox=output;
            }
         }).execute();
    }

    @Override
    public MyFile[] listfiles() {
        return new MyFile[0];
    }

    @Override
    public boolean isHidden() {
        //dropbox files cannot be hidden
        return false;
    }

    @Override
    public boolean canRead() {
        //dropbox files must be readable
        return true;
    }

    @Override
    public String getPath() {
        return outputFromDropbox.path;
    }

    @Override
    public boolean isDirectory() {
        return outputFromDropbox.isDir;
    }

    @Override
    public String getName() {
        return outputFromDropbox.fileName();
    }
}
