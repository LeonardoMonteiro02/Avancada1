package com.example.myfleet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.myfleet.ActivityHome;
import com.example.myfleet.CalculatorActivity;
import com.example.myfleet.MainActivity;
import com.example.myfleet.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationConfig {

    public static void configureNavigation(final Activity activity, final BottomNavigationView bottomNavigationView, final BottomNavigationView bottomNavigationView2) {


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(activity, ActivityHome.class);
                    activity.startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_exit) {
                    showExitConfirmationDialog(activity);
                    return true;
                }
                return false;
            }
        });

        if (bottomNavigationView2 != null) {
            bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_dados) {
                        showToast(activity, "Dados");
                        return true;
                    } else if (itemId == R.id.nav_Calculadora) {
                        Intent intent = new Intent(activity, CalculatorActivity.class);
                        activity.startActivity(intent);
                        return true;
                    } else if (itemId == R.id.nav_map) {
                        showToast(activity, "Mapa");
                        return true;
                    }

                    return false;
                }
            });
        }
    }

    private static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    private static void showExitConfirmationDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Confirmação")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout(activity);
                        redirectToLogin(activity);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private static void logout(Activity activity) {
        SessionManager sessionManager = new SessionManager(activity);
        sessionManager.clearSession();
    }

    private static void redirectToLogin(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
