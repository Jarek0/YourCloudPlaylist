package com.example.YourCloudPlaylist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.example.YourCloudPlaylist.R;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by pglg on 22-03-2016.
 */
public class PlaylistGenerator extends AsyncTask<String,String,Void> {

    private ProgressDialog dialog;
    private Context context;
    private String exceptionText;
    private String devicePath;
    private String dropboxPath;
    private String playlistName;

    PlaylistGenerator(Context context){
        this.context=context;
        dialog=new ProgressDialog(context);
    }

   private  String toDirectURL(String shareURL){
        char[] chars=shareURL.toCharArray();
        chars[chars.length-1]='1';
        return String.valueOf(chars);
    }

   private String getShareURL(String strURL) throws Exception {
        URLConnection conn = null;
        String redirectedUrl = null;
        try {
            URL inputURL = new URL(strURL);
            conn = inputURL.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();
            redirectedUrl = conn.getURL().toString();
            is.close();

        } catch (MalformedURLException e) {
            throw new Exception(context.getResources().getString(R.string.connection_error));
        } catch (IOException e) {
            throw new Exception(context.getResources().getString(R.string.connection_error));
        }

        return toDirectURL(redirectedUrl);
    }

    private void makePlaylist(String path,String name,List<String>urls,List<String>names) throws Exception {

        StringBuilder playlistText=new StringBuilder("#EXTM3U");
        try{
            File playlist=new File(path,File.separator+name+".m3u");
            playlist.createNewFile();
            FileOutputStream fOut = new FileOutputStream(playlist);
            playlistText.append(System.getProperty("line.separator"));
            for(int i=0;i<urls.size();i++){
                playlistText.append("#EXTINF:-1,");
                playlistText.append(names.get(i));
                playlistText.append(System.getProperty("line.separator"));
                playlistText.append(urls.get(i));
                playlistText.append(System.getProperty("line.separator"));
            }
            fOut.write(playlistText.toString().getBytes());
            fOut.close();
        }
        catch (IOException e){
            throw new Exception(context.getResources().getString(R.string.saving_file_error));
        }

    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(context.getResources().getString(R.string.wait));
        this.dialog.setIndeterminate(false);
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    @Override
    protected Void doInBackground(String... params) {

        devicePath=params[0];
        dropboxPath=params[1];
        playlistName=params[2];
        DropboxAPI.Entry directory;
        DropboxAPI.DropboxLink link;
        String shareLink;
        List<String> urls=new ArrayList<>();
        List<String> names=new ArrayList<>();

        try{
            directory = DbApi.mDBApi.metadata(dropboxPath, 1000, null, true, null);
                for (DropboxAPI.Entry entry:directory.contents) {
                   if(!entry.isDir){
                       publishProgress(context.getResources().getString(R.string.making_link)+"\n"+entry.fileName());
                      link=DbApi.mDBApi.share(entry.path);
                       shareLink=getShareURL(link.url);
                       urls.add(shareLink);
                       names.add(entry.fileName());
                   }
                }
            publishProgress(context.getResources().getString(R.string.creating_playlist));
            makePlaylist(devicePath,playlistName,urls,names);
        }
        catch (DropboxException e){
            exceptionText=e.getMessage();
            return null;
        }
        catch (Exception e){
            exceptionText=e.getMessage();
            return null;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        this.dialog.setMessage(values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.dialog.cancel();
        Toast.makeText(context, context.getResources().getString(R.string.incorrect_playlist),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if(exceptionText!=null){
            Toast.makeText(context, context.getResources().getString(R.string.error_occured)+"\n"+exceptionText, Toast.LENGTH_LONG).show();
        }

    }
}
