package com.example.YourCloudPlaylist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pglg on 07-03-2016.
 */
public class FileExplorer extends ListActivity {

    private List<String> item = null;
    private List<String> path = null;
    private String root;
    private String home;
    private TextView myPath;
    private String currentDir;
    private FileType fileType;
    private FileManagerFactory factory=new FileManagerFactory();
    private  ArrayAdapter<String> fileList;
    private Context context=this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileexp);
        Intent intent = getIntent();
        fileType = (FileType) intent.getSerializableExtra("mode");
        myPath = (TextView)findViewById(R.id.summary);
        setPaths();
        getDir(home);
    }
    private void getDir(String dirPath)
    {
        factory.getInstance().execute(dirPath);
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        factory.getInstance().execute(path.get(position));
    }

    public void cancelClicked(View view) {
        finish();
    }

    public void selectClicked(View view) {
        Intent giveDevicePath=new Intent();
        giveDevicePath.putExtra("path",currentDir);
        setResult(RESULT_OK, giveDevicePath);
        finish();
    }

    private void setPaths(){
        switch(fileType){
            case DROPBOX_FILE:
                home = DropboxFile.getHome();
                root=DropboxFile.getRoot();
                break;
            case DEVICE_FILE:
                root=DeviceFile.getRoot();
                home=DeviceFile.getHome();
        }
    }
    //I need to use factory because I want call asynctask class (File Manager) more than one time
    class FileManagerFactory{
         FileManager getInstance(){
           FileManager manager= new FileManager(fileType,context,
                    new MyFileAsyncResponse() {
                        @Override
                        public void processFinish(MyFile file) {
                            if(file!=null){
                                if (file.isDirectory()) {
                                    if (file.canRead()) {
                                        myPath.setText("Location: " + file.getPath());
                                        currentDir = file.getPath();
                                        item = new ArrayList<String>();
                                        path = new ArrayList<String>();
                                        MyFile[] files = (MyFile[]) file.listFiles();
                                        if (!file.getPath().equals(root)) {
                                            item.add("../");
                                            path.add(file.getParent());
                                        }
                                        for (MyFile f : files) {
                                            if (!f.isHidden() && f.canRead()) {
                                            path.add(f.getPath());
                                            if (f.isDirectory())
                                                item.add(f.getName() + "/");
                                            else
                                                item.add(f.getName());
                                             }
                                        }
                                        fileList = new ArrayAdapter<String>(context, R.layout.row, item);
                                        setListAdapter(fileList);
                                    }
                                    else
                                    {
                                        new AlertDialog.Builder(context)
                                                //.setIcon(R.drawable.icon)
                                                .setTitle("[" + file.getName() + "] folder can't be read!")
                                                .setPositiveButton("OK",null).show();
                                    }

                                }
                            }
                            else{
                                Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
             return manager;
        }
    }
}
