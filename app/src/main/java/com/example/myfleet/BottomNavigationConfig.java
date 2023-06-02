package com.example.myfleet;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationConfig {
    private Context context;
    private BottomNavigationView bottomNavigationView;

    public BottomNavigationConfig(Context context, BottomNavigationView bottomNavigationView) {
        this.context = context;
        this.bottomNavigationView = bottomNavigationView;
    }

    public void configureBottomNavigation() {
        // Configure os itens de menu e o listener de seleção do BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Ação para o item Home
                    Toast.makeText(context, "Home", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_exit) {
                    // Exibir o diálogo de confirmação
                    new AlertDialog.Builder(context)
                            .setTitle("Confirmação")
                            .setMessage("Tem certeza que deseja sair?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Executar ação de logout e redirecionar para a tela de login
                                    logout();
                                    redirectToLogin();
                                }
                            })
                            .setNegativeButton("Não", null)
                            .show();

                    return true;
                }

                return false;
            }
        });
    }

    private void logout() {
        // Limpar as informações de sessão usando o SessionManager
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.clearSession();
        // Outras ações de logout, se necessário
    }

    private void redirectToLogin() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}


