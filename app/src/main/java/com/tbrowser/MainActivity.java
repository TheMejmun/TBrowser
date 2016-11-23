package com.tbrowser;

import android.Manifest;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText searchbox;
    ActionBar ab;
    MenuItem reloadButton;
    loadURL loadurl;
    Menu menu;
    Animation rotation;
    ImageView loadingIcon;
    static boolean asyncTaskRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request Permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET}, 404);

        // Set up everything
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        searchbox = (EditText) findViewById(R.id.searchbox);
        loadingIcon = (ImageView) findViewById(R.id.loading_icon);

        // Animation
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        rotation.setRepeatCount(Animation.INFINITE);


        // When Enter is pressed
        searchbox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (asyncTaskRunning) {
                        // TODO Something when trying to load again
                    } else {
                        // Load URL
                        if (!searchbox.getText().toString().trim().isEmpty()) {
                            loadurl = new loadURL();
                            loadurl.execute(searchbox.getText().toString());
                        }
                    }

                    // Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    // URL Loader
    private class loadURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            // Graphix
            loadingIcon.clearAnimation();
            loadingIcon.setVisibility(View.INVISIBLE);
            reloadButton = menu.findItem(R.id.menu_load);
            reloadButton.setIcon(R.drawable.ic_autorenew_white_48dp);
            reloadButton.setTitle(R.string.reload);
            asyncTaskRunning = false;
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {
            // Graphix
            asyncTaskRunning = true;
            loadingIcon.setAnimation(rotation);
            loadingIcon.animate();
            loadingIcon.setVisibility(View.VISIBLE);
            reloadButton = menu.findItem(R.id.menu_load);
            reloadButton.setIcon(R.drawable.ic_clear_white_48dp);
            reloadButton.setTitle(R.string.stop);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            // Graphix
            loadingIcon.clearAnimation();
            loadingIcon.setVisibility(View.INVISIBLE);
            reloadButton = menu.findItem(R.id.menu_load);
            reloadButton.setIcon(R.drawable.ic_autorenew_white_48dp);
            reloadButton.setTitle(R.string.reload);
            asyncTaskRunning = false;
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... params) {
            //TODO Replace with URL loading
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // OptionsMenu
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    // Menu click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_load:

                // Reload
                if (item.getTitle().equals(getString(R.string.reload))) {
                    Log.i("menu_load", "reload");

                    // Load
                } else if (item.getTitle().equals(getString(R.string.load))) {
                    Log.i("menu_load", "load");

                    // Stop Loading
                } else if (item.getTitle().equals(getString(R.string.stop))) {
                    Log.i("menu_load", "stop");
                    loadurl.cancel(true);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
