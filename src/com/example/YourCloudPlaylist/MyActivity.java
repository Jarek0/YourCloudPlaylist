package com.example.YourCloudPlaylist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity {

    private final int DEVICE_EXPLORER =1;
    private final int DROPBOX_EXPLORER=2;
    private Boolean exit = false;
    private TextView playlistNameTextView;
    private TextView devicePathTextView;
    private TextView dropboxPathTextView;
    private String currentDir;

    private int lastEditedTextView=0; // to avoid retouched text view

    /******To disable keyboard showing************/
    private View.OnTouchListener devicePathTextViewTouchBehaviour = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {

            /*To disable opened keyboard*/
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if(lastEditedTextView!=1){
                lastEditedTextView=1;
                onDeviceExplorerButtonClick(v);
            }
            return true;
        }
    };

    private View.OnTouchListener dropboxPathTextViewTouchBehaviour = new View.OnTouchListener() {
        public boolean onTouch (View v, MotionEvent event) {

              /*To disable opened keyboard*/
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if(lastEditedTextView!=2){
                lastEditedTextView=2;
                onDropboxExplorerButtonClick(v);
            }
            return true;
        }
    };
    /********************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        playlistNameTextView=(TextView)findViewById(R.id.playlist_name);
        devicePathTextView=(TextView)findViewById(R.id.device_path);
        devicePathTextView.setOnTouchListener(devicePathTextViewTouchBehaviour);
        dropboxPathTextView=(TextView)findViewById(R.id.dropbox_path);
        dropboxPathTextView.setOnTouchListener(dropboxPathTextViewTouchBehaviour);

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
        startActivityForResult(openExplorer, DEVICE_EXPLORER);
    }

    public void onDropboxExplorerButtonClick(View view) {
        Intent openExplorer=new Intent(this,FileExplorer.class);
        openExplorer.putExtra("mode",FileType.DROPBOX_FILE);
        startActivityForResult(openExplorer, DROPBOX_EXPLORER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("1","jeden");
        if (resultCode == RESULT_OK ) {
            Log.i("2","dwa");
            currentDir=data.getExtras().getString("path");
            switch(requestCode){
                case DEVICE_EXPLORER:{
                    devicePathTextView.setText(currentDir);
                    break;
                }
                case DROPBOX_EXPLORER:{
                    dropboxPathTextView.setText(currentDir);
                }
            }
        }
    }

    public void onGenerateButtonClick(View view) {
        String playlistName= String.valueOf(playlistNameTextView.getText());
        new PlaylistGenerator(this).execute(devicePathTextView.getText().toString(),
                                            dropboxPathTextView.getText().toString(),
                                            playlistName);
    }
}
