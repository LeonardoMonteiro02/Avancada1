/*********************************************************************

 Nome do arquivo: ActivityLogin.java

 Descrição: Esta classe implementa a funcionalidade de login, verificando as credenciais do usuário
            em um banco de dados local. Se as credenciais forem corretas, o usuário é redirecionado
            para a página inicial. Caso contrário, exibe uma mensagem de erro.

 Autor: Leonardo Monteiro sa Sé Pinto

 Data: 13/06/2023

 Histórico de modificações:

 [Data da modificação]: [Breve descrição da modificação realizada]
 [Data da modificação]: [Breve descrição da modificação realizada]
 ...
 **********************************************************************/

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
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializa os elementos da interface do usuário
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLoginActivityLogin);

        // Inicializa o gerenciador de sessão
        sessionManager = new SessionManager(this);

        // Verifica se o usuário já está logado
        boolean isLoggedIn = sessionManager.isLoggedIn();

        if (isLoggedIn) {
            // O usuário já está logado, ir para a página inicial diretamente
            abrirPaginaInicial();
            return;
        }

        // Configura o ouvinte de clique para o botão de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém o email e a senha inseridos nos campos de texto
                String email = editTextEmail.getText().toString().trim();
                String senha = editTextSenha.getText().toString().trim();

                // Chama o método para fazer o login
                fazerLogin(email, senha);
            }
        });
    }

    // Abre a página inicial
    private void abrirPaginaInicial() {
        Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
        startActivity(intent);
        finish(); // Finaliza a atividade de login para que o usuário não possa voltar a ela pressionando o botão "Voltar"
    }

    // Verifica as credenciais no banco de dados
    private boolean verificarCredenciais(String email, String senha) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        return databaseHelper.checkAccount(email, senha);
    }

    // Faz o login
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
