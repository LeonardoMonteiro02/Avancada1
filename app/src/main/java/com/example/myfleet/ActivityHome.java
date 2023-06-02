package com.example.myfleet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfleet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityHome extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationConfig bottomNavigationConfig;

    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationConfig = new BottomNavigationConfig(this, bottomNavigationView);
        bottomNavigationConfig.configureBottomNavigation();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            exitApp();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 2000);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    private void exitApp() {
        mHandler.removeCallbacks(mRunnable);
        finishAffinity();
    }

    // Restante do código da ActivityHome...

}
