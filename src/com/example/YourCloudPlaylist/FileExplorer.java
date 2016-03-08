package com.example.YourCloudPlaylist;

import android.app.Activity;
import android.os.Bundle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by pglg on 07-03-2016.
 */
public class FileExplorer extends ListActivity {
    private List<String> item = null;
    private List<String> path = null;
    private String root="/";
    private String home;
    private TextView myPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileexp);
        myPath = (TextView)findViewById(R.id.summary);
        home = Environment.getExternalStorageDirectory().getPath();
        getDir(home);
    }
    private void getDir(String dirPath)
    {
        myPath.setText("Location: " + dirPath);
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if(!dirPath.equals(root))
        {
            //item.add(root);
            //path.add(root);
            item.add("../");
            path.add(f.getParent());
        }
        for(File file : files)
        {
            if(!file.isHidden() && file.canRead()) {
                path.add(file.getPath());
                if (file.isDirectory())
                    item.add(file.getName() + "/");
                else
                    item.add(file.getName());

            }
        }
        ArrayAdapter<String> fileList =
                new ArrayAdapter<String>(this, R.layout.row, item);
        setListAdapter(fileList);

    }
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(path.get(position));
        if (file.isDirectory())
        {
            if(file.canRead())
                getDir(path.get(position));
            else
            {
                new AlertDialog.Builder(this)
                        //.setIcon(R.drawable.icon)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK",null).show();
            }
        }
        else
        {
            new AlertDialog.Builder(this)
                    //.setIcon(R.drawable.icon)
                    .setTitle("[" + file.getName() + "]")
                    .setPositiveButton("OK",null).show();
        }
    }

}
