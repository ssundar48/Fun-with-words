package com.example.ssundar.funwithwords;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FilenameFilter;

public class file_browse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFolderList("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String rootDir = "/sdcard/";
    private String[] mFileList;
    private String mChosenFile;
    private String txtFile;
    private static final int DIALOG_LOAD_FILE = 1;
    private static final int DIALOG_DISPLAY_ERROR = 2;

    private void loadFolderList(String file) {
        File mPath = new File(rootDir + "/" + file);
        try {
            mPath.mkdirs();
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        if(mPath.exists()) {
            if (mPath.isDirectory()) {
                mFileList = mPath.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        if (filename.contains(".txt")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
        else {
            mFileList= new String[0];
        }
        onCreateDialog(DIALOG_LOAD_FILE);
    }

    public void sendMessage() {
        Intent intent = new Intent(this, file_display.class);
        intent.putExtra("txtFile", txtFile);
        startActivity(intent);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if(mFileList == null) {
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenFile = mFileList[which];
                        System.out.println("Chosen: " + mChosenFile);
                        if(new File(rootDir + mChosenFile).isDirectory()) {
                            onCreateDialog(2);
                            return;
                        }
                        System.out.println("Out");
                        txtFile = mChosenFile;
                        sendMessage();
                        return;
                    }
                });
                break;
            case DIALOG_DISPLAY_ERROR:
                builder.setTitle("File not valid!!");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }
                );
                break;
        }
        dialog = builder.show();
        return dialog;
    }
}