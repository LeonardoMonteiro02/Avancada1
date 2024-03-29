package com.example.myfleet;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMap extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView bottomNavigationView2;

    private boolean isLoggedIn;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_page);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView2 = findViewById(R.id.bottom_navigation2);

        BottomNavigationConfig.configureNavigation(this, bottomNavigationView, bottomNavigationView2);
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
}
