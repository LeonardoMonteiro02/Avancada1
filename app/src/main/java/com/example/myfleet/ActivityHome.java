package com.example.myfleet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationConfig bottomNavigationConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationConfig = new BottomNavigationConfig(this, bottomNavigationView);
        bottomNavigationConfig.configureBottomNavigation();

        // Restante do código da ActivityHome...
    }

    // Restante do código da ActivityHome...
}
