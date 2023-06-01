package com.example.myfleet;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityHome extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private boolean isLoggedIn;
    private boolean doubleBackToExitPressedOnce = false;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // Usuário não está logado, redirecionar para a tela de login
            Intent intent = new Intent(ActivityHome.this, ActivityLogin.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Define o item selecionado inicialmente
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Define o listener de clique para a barra de navegação
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    // Ação para o item Home
                    Toast.makeText(ActivityHome.this, "Home", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_exit) {
                    // Exibir o diálogo de confirmação
                    new AlertDialog.Builder(ActivityHome.this)
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
    // Método para realizar a ação de logout
    private void logout() {
        // Limpar as informações de sessão usando o SessionManager
        SessionManager sessionManager = new SessionManager(ActivityHome.this);
        sessionManager.clearSession();
        // Outras ações de logout, se necessário
    }

    // Método para redirecionar para a tela Inicial
    private void redirectToLogin() {
        // Iniciar a atividade da tela de login ou navegar para a tela de login
        Intent intent = new Intent(ActivityHome.this, MainActivity.class);
        startActivity(intent);
        finish(); // Opcionalmente, encerrar a atividade atual para que o usuário não possa voltar à tela de home pressionando o botão "Voltar"
    }

}
