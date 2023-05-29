package com.example.myfleet;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLoginActivityLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String senha = editTextSenha.getText().toString().trim();

                if (verificarCredenciais(email, senha)) {
                    // Credenciais corretas, fazer o login
                    Toast.makeText(ActivityLogin.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                    // Aqui você pode adicionar o código para abrir a próxima tela ou realizar outras ações após o login
                } else {
                    // Credenciais incorretas, exibir mensagem de erro
                    Toast.makeText(ActivityLogin.this, "Credenciais inválidas. Verifique seu e-mail e senha.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean verificarCredenciais(String email, String senha) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.checkAccount(email, senha);
    }
}
