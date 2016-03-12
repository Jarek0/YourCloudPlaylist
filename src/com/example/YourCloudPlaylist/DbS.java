package com.example.YourCloudPlaylist;

/**
 * Created by pglg on 12-03-2016.
 */
public class DbS {
    private static DbS ourInstance = new DbS();

    public static DbS getInstance() {
        return ourInstance;
    }

    private DbS() {
    }
}
