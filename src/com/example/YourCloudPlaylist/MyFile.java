package com.example.YourCloudPlaylist;

/**
 * Created by pglg on 12-03-2016.
 */
public interface MyFile {
    String getRoot();
    String getHome();
    void setPath(String path);
    MyFile[] listfiles();
    boolean isHidden();
    boolean canRead();
    String getPath();
    boolean isDirectory();
    String getName();
}
