package com.example.YourCloudPlaylist;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

/**
 * Created by pglg on 12-03-2016.
 */
public class FileManager extends AsyncTask<String, Void, MyFile> {

    private ProgressDialog dialog;
    private FileType typeOfFile;

    private DropboxFile dropboxFileFactory(DropboxAPI.Entry entry){
        return new DropboxFile( entry.path,
                                entry.fileName(),
                                entry.parentPath(),
                                null, //you can see list of neighbour files only for current file
                                entry.isDir);
    }
    public MyFileAsyncResponse delegate = null;

    public FileManager(FileType typeOfFile,Context context, MyFileAsyncResponse delegate){
        this.delegate = delegate;
        dialog=new ProgressDialog(context);
        this.typeOfFile=typeOfFile;
    }

    @Override
    protected void onPreExecute() {
        if(typeOfFile!=FileType.DEVICE_FILE) {
            this.dialog.setMessage("Wait..");
            this.dialog.setIndeterminate(false);
            this.dialog.setCancelable(false);
            this.dialog.show();
        }
    }

    @Override
    protected MyFile doInBackground(String... params) {

        if(params.length!=1 || params[0]==null) return null; //bledne dane wejsciowe
        String path=params[0];
        switch(typeOfFile) {
            case DROPBOX_FILE:{

                DropboxAPI.Entry directory;
                DropboxFile dFile;
                DropboxFile[] listFiles=null;
                try {
                    directory = DbApi.mDBApi.metadata(path, 1000, null, true, null);
                    if(directory.isDir) {
                            listFiles = new DropboxFile[directory.contents.size()];
                            for (int i = 0; i < directory.contents.size(); i++) {
                                listFiles[i] = dropboxFileFactory(directory.contents.get(i));
                            }
                        }
                        dFile = new DropboxFile(path,
                                directory.fileName(),
                                directory.parentPath(),
                                listFiles,//when file isnt dir the listfile will be null
                                directory.isDir);
                        return dFile;
                }
                catch (DropboxException e) {
                    return null;
                }
            }
            case DEVICE_FILE:
                return new DeviceFile(path);
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(MyFile file) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        delegate.processFinish(file);
    }
}
