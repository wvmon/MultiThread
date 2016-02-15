package com.example.hunter.multi_threadprogramming;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Declare Variables
    String fileName = "numbers.txt";
    private ProgressBar prgs;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    StringBuilder stg = new StringBuilder();
    FileOutputStream outputStream;
    ListView list;
    ArrayList<String> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set progress to 0 when starting up
        prgs = (ProgressBar) findViewById(R.id.progressbar);
        prgs.setProgress(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /*
     * BUTTON HANDLER CREATE
     * Create a file, then write 1 - 10 to it, while keeping track of the progress.
     */
    public void buttonHandlerCreate(View view) {
        // set progress status to 0
        progressStatus = 0;

        new Thread(new Runnable() {
            public void run() {

                // add 1 - 10 to stg(our StringBuilder)
                for (int i = 1; i < 11; i++) {
                    stg.append(i).append("\n");

                    // add 10 to progress bar every time we iterate through the for loop (100 is max)
                    progressStatus += 10;
                    handler.post(new Runnable() {
                        public void run() {
                            prgs.setProgress(progressStatus);
                        }
                    });

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                // write to the file
                try {
                    File file = new File(getFilesDir(), fileName);
                    outputStream = new FileOutputStream(file);
                    outputStream.write(stg.toString().getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    /*
     * BUTTON HANDLER LOAD
     * Event handler when the user clicks the load button. It will load the file onto the screen.
     */
    public void buttonHandlerLoad(View view) {
        list = (ListView) findViewById(R.id.listView);

        array = new ArrayList<>();

        new Thread(new Runnable() {
            public void run() {
                // open the file and read and display it on the screen.
                try {
                    FileInputStream f = new FileInputStream(new File(getFilesDir(), "numbers.txt"));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(f, "UTF-8"));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        array.add(line);
                    }
                    // close the file
                    f.close();
                } catch (OutOfMemoryError o) {
                    o.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }).start();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        list.setAdapter(adapter);
    }

    /*
     * BUTTON HANDLER CLEAR
     * Clears the screen and the progress bar
     */
    public void buttonHandlerClear(View view) {
        ListView list = (ListView) findViewById(R.id.listView);
        prgs.setProgress(0);
        list.setAdapter(null);
    }
}
