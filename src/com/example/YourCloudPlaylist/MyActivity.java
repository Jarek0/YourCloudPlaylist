package com.example.YourCloudPlaylist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final int FILE_EXPLORER=1;
    private Boolean exit = false;
    private TextView playlistNameTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        playlistNameTextView=(TextView)findViewById(R.id.playlist_name);
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit. ",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void onLogOutButtonClick(View view) {
        SharedPreferences prefs = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
        DbApi.mDBApi.getSession().unlink();
        DbApi.logged=false;

        Intent openLogActivity=new Intent(this,LogActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(openLogActivity);
    }

    public void onDeviceExplorerButtonClick(View view) {

       Intent openExplorer=new Intent(this,FileExplorer.class);
        openExplorer.putExtra("mode",FileType.DEVICE_FILE);
        startActivityForResult(openExplorer,FILE_EXPLORER);
    }

    public void onDropboxExplorerButtonClick(View view) {
        Intent openExplorer=new Intent(this,FileExplorer.class);
        openExplorer.putExtra("mode",FileType.DROPBOX_FILE);
        startActivityForResult(openExplorer,FILE_EXPLORER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == FILE_EXPLORER) {
            String currentDir=data.getExtras().getString("path");
            Log.i ("elo",requestCode+" "+resultCode+" "+currentDir);
        }
    }

    public void onGenerateButtonClick(View view) {
        String playlistName= String.valueOf(playlistNameTextView.getText());
        new PlaylistGenerator(this).execute(Environment.getExternalStorageDirectory().getPath(),"/",playlistName);
    }
}
