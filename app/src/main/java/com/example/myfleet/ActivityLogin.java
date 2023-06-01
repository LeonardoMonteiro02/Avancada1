package com.example.myfleet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityLogin extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonLogin;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLoginActivityLogin);

        sessionManager = new SessionManager(this);

        boolean isLoggedIn = sessionManager.isLoggedIn();

        if (isLoggedIn) {
            // O usuário já está logado, ir para a página inicial diretamente
            abrirPaginaInicial();
            return;
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String senha = editTextSenha.getText().toString().trim();

                fazerLogin(email, senha);
            }
        });
    }

    private void abrirPaginaInicial() {
        Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
        startActivity(intent);
        finish(); // Finaliza a atividade de login para que o usuário não possa voltar a ela pressionando o botão "Voltar"
    }

    private boolean verificarCredenciais(String email, String senha) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.checkAccount(email, senha);
    }

    private void fazerLogin(String email, String senha) {
        if (verificarCredenciais(email, senha)) {
            // Credenciais corretas, fazer o login

            // Salvar o status do login usando a classe SessionManager
            sessionManager.setLoggedIn(true);

            // Abrir a página Home
            abrirPaginaInicial();
        } else {
            // Credenciais incorretas, exibir mensagem de erro
            Toast.makeText(ActivityLogin.this, "Credenciais inválidas. Verifique seu e-mail e senha.", Toast.LENGTH_SHORT).show();
        }
    }
}