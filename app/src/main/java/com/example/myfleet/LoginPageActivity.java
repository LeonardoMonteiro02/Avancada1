package com.example.myfleet;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;


public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginEmailButton;
    private Button loginFacebookButton;
    private Button loginButton;
    private TextView createAccountTextView;
    private static final int RC_SIGN_IN = 9001; // Código de solicitação de login
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginEmailButton = findViewById(R.id.buttonLoginEmail);
        loginFacebookButton = findViewById(R.id.buttonFacebook);
        loginButton = findViewById(R.id.buttonLoginLoginPage);
        createAccountTextView = findViewById(R.id.textViewCreateAccount);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        createAccountTextView.setTypeface(customFont);

        // Configurar opções de login pelo Gmail
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Configurar o CallbackManager para autenticação pelo Facebook
        callbackManager = CallbackManager.Factory.create();

        // Criar o cliente da API do Google
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // Tratar falhas na conexão
                        Toast.makeText(LoginPageActivity.this, "Falha na conexão com o Google Play Services", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginEmailButton.setOnClickListener(this);
        loginFacebookButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        createAccountTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLoginEmail) {
            // Iniciar a solicitação de login pelo Gmail
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else if (v.getId() == R.id.buttonFacebook) {
            // Solicitar permissões adicionais, se necessário
            LoginManager.getInstance().logInWithReadPermissions(LoginPageActivity.this, Arrays.asList("email"));

            // Registrar o callback para tratar o resultado da autenticação
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // Autenticação pelo Facebook bem-sucedida
                    // Obter informações do usuário a partir do loginResult
                    // Exemplo: obter o token de acesso
                    AccessToken accessToken = loginResult.getAccessToken();
                    String token = accessToken.getToken();

                    // Exemplo: exibir o token em um Toast
                    Toast.makeText(LoginPageActivity.this, "Token de acesso: " + token, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    // Autenticação pelo Facebook cancelada pelo usuário
                }

                @Override
                public void onError(FacebookException error) {
                    // Ocorreu um erro durante a autenticação pelo Facebook
                }
            });
        } else if (v.getId() == R.id.buttonLoginLoginPage) {
            Intent intent = new Intent(LoginPageActivity.this, ActivityLogin.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textViewCreateAccount) {
            Toast.makeText(this, "Problemas com login? pressionado", Toast.LENGTH_SHORT).show();
            // Adicione aqui a lógica para a ação do texto "Problemas com login?"
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar o código de solicitação para determinar a origem do resultado
        if (requestCode == RC_SIGN_IN) {
            // Resultado da autenticação do Google
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(googleSignInResult);
        } else {
            // Resultado da autenticação do Facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Login bem-sucedido, obter a conta do usuário
            GoogleSignInAccount account = result.getSignInAccount();

            // Aqui você pode realizar as ações necessárias com a conta do usuário
            String fullName = account.getDisplayName();
            String email = account.getEmail();

            // Exemplo: exibir o nome e o email em um Toast
            Toast.makeText(this, "Nome: " + fullName + "\nEmail: " + email, Toast.LENGTH_SHORT).show();
        } else {
            // Falha no login, exibir uma mensagem de erro
            Toast.makeText(this, "Falha no login pelo Gmail", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Login bem-sucedido, obter a conta do usuário
            GoogleSignInAccount account = result.getSignInAccount();

            // Aqui você pode realizar as ações necessárias com a conta do usuário (por exemplo, enviar para o servidor, iniciar a próxima atividade, etc.)
            String fullName = account.getDisplayName();
            String email = account.getEmail();

            // Exemplo: exibir o nome e o email em um Toast
            Toast.makeText(this, "Nome: " + fullName + "\nEmail: " + email, Toast.LENGTH_SHORT).show();
        } else {
            // Falha no login, exibir uma mensagem de erro
            Toast.makeText(this, "Falha no login pelo Gmail", Toast.LENGTH_SHORT).show();
        }
    }

}