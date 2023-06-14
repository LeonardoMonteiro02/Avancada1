/*********************************************************************

 Nome do arquivo: LoginPageActivity.java

 Descrição: Este código representa a atividade de login em um aplicativo Android.
            A classe LoginPageActivity é responsável por exibir a tela de login aos usuários.
            Nessa tela, são fornecidas opções para fazer login usando e-mail, Facebook ou criar uma nova conta.

 Autor: Leonardo Monteiro sa Sé Pinto

 Data: 13/06/2023

 Histórico de modificações:

 [Data da modificação]: [Breve descrição da modificação realizada]
 [Data da modificação]: [Breve descrição da modificação realizada]
 ...
 **********************************************************************/

package com.example.myfleet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginEmailButton;
    private Button loginFacebookButton;
    private Button loginButton;
    private TextView createAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Inicialização dos elementos da interface do usuário
        loginEmailButton = findViewById(R.id.buttonLoginEmail);
        loginFacebookButton = findViewById(R.id.buttonFacebook);
        loginButton = findViewById(R.id.buttonLoginLoginPage);
        createAccountTextView = findViewById(R.id.textViewCreateAccount);

        // Definição da fonte personalizada para o TextView
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        createAccountTextView.setTypeface(customFont);

        // Atribuição dos ouvintes de clique aos elementos
        loginEmailButton.setOnClickListener(this);
        loginFacebookButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        createAccountTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLoginEmail) {
            // Ação para o botão de login com email
            Toast.makeText(this, "Login com Gmail", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.buttonFacebook) {
            // Ação para o botão de login com Facebook
            Toast.makeText(this, "Login com Facebook", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.buttonLoginLoginPage) {
            // Ação para o botão de login da página de login
            Intent intent = new Intent(LoginPageActivity.this, ActivityLogin.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textViewCreateAccount) {
            // Ação para o TextView de criação de conta
            Toast.makeText(this, "Problemas com login?", Toast.LENGTH_SHORT).show();
            // Adicione aqui a lógica para a ação do texto "Problemas com login?"
        }
    }
}
