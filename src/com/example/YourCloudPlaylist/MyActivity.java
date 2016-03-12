package com.example.YourCloudPlaylist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final int FILE_EXPLORER=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        DbApi.initialize();
    }
    @Override
    public void onResume() {
        super.onResume();

        if (DbApi.mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                DbApi.mDBApi.getSession().finishAuthentication();

                String accessToken = DbApi.mDBApi.getSession().getOAuth2AccessToken();
                storeAuth(accessToken);
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }
    private void storeAuth(String accessToken){
        if (accessToken != null) {
            SharedPreferences prefs = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("ACCESS_KEY", "oauth2:");
            edit.putString("ACCESS_SECRET", accessToken);
            edit.commit();
        }
    }
    private void authentication()
    {
        SharedPreferences prefs = getSharedPreferences("prefs", 0);
        String key = prefs.getString("ACCESS_KEY", null);
        String secret = prefs.getString("ACCESS_SECRET", null);
        if (secret != null) {
            DbApi.mDBApi.getSession().setOAuth2AccessToken(secret);
        } else {
            DbApi.mDBApi.getSession().startOAuth2Authentication(MyActivity.this);
        }
    }
    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    public void buttonAuthClicked(View view) {
        new Authentication().execute();
    }

    public void buttonClearAuthClicked(View view) {
        clearKeys();
    }

    public void explorerButtonClick(View view) {

       Intent openExplorer=new Intent(this,FileExplorer.class);
        startActivityForResult(openExplorer,FILE_EXPLORER);
    }

    private class Authentication extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
           authentication();
            return null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == FILE_EXPLORER) {
            String currentDir=data.getExtras().getString("path");
            Log.i ("elo",requestCode+" "+resultCode+" "+currentDir);
        }

    }






}
