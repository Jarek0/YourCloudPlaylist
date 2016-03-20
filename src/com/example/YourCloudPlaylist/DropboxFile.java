package com.example.YourCloudPlaylist;


/**
 * Created by pglg on 12-03-2016.
 */
public class DropboxFile implements MyFile {

    public static String getRoot() { return "/"; }
    public static String getHome() {
        return getRoot();
    }

    private String path;
    private boolean isDirectory;
    private String name;
    private String parentPath;
    private DropboxFile[] listFiles;

    DropboxFile(String path,String name,String parentPath,DropboxFile[] listFiles,boolean isDirectory){
        this.path=path;
        this.name=name;
        this.parentPath=parentPath;
        this.listFiles=listFiles;
        this.isDirectory=isDirectory;
    }

    @Override
    public DropboxFile[] listFiles(){
        return listFiles;
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
        return path;
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getParent() {
        return parentPath;
    }
}
