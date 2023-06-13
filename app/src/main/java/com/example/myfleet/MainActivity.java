/*********************************************************************

 Nome do arquivo: MainActivity.java

 Descrição: Este código implementa a atividade principal de um aplicativo Android.
            Ele exibe uma animação de carro, seguida pela exibição do logo e botões.
            Se o usuário já estiver logado, ele será redirecionado para a atividade Home.
            Caso contrário, poderá criar uma conta ou fazer login.

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Elementos visuais
    private ImageView carImage;
    private ImageView logoImage;
    private LinearLayout buttonLayout;
    private Button createButton;
    private Button loginButton;
    private TextView textBemvindo;

    private SessionManager sessionManager; // Gerenciador de sessão do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(getApplicationContext());

        // Verifica se o usuário já está logado
        if (sessionManager.isLoggedIn()) {
            // Redireciona para a próxima atividade após o login
            Intent intent = new Intent(MainActivity.this, ActivityHome.class);
            startActivity(intent);
            finish(); // Fecha a atividade atual
        }

        // Inicialização dos elementos visuais
        carImage = findViewById(R.id.car);
        logoImage = findViewById(R.id.logo);
        buttonLayout = findViewById(R.id.button_layout);
        createButton = findViewById(R.id.create_account);
        loginButton = findViewById(R.id.login);
        textBemvindo = findViewById(R.id.textBemvindo);

        // Definição da fonte personalizada para o texto de boas-vindas
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        textBemvindo.setTypeface(customFont);

        createButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        animateCar(); // Inicia a animação do carro
    }

    // Método responsável por animar a imagem do carro
    private void animateCar() {
        Animation animationCar = AnimationUtils.loadAnimation(this, R.anim.animacao);
        carImage.startAnimation(animationCar);

        animationCar.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // A animação do carro começou
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // A animação do carro terminou
                carImage.setVisibility(View.GONE);

                // Adicione um atraso de 1 segundo antes de exibir a logo e os botões
                logoImage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateLogo();
                    }
                }, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // A animação do carro se repetiu
            }
        });
    }

    // Método responsável por animar a imagem do logo e exibir os botões
    private void animateLogo() {
        logoImage.setVisibility(View.VISIBLE);

        Animation animationLogo = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        logoImage.startAnimation(animationLogo);

        animationLogo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // A animação da logo começou
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // A animação da logo terminou
                buttonLayout.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                createButton.setVisibility(View.VISIBLE);
                textBemvindo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // A animação da logo se repetiu
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_account) {
            // Botão "Criar Conta" pressionado
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.login) {
            // Botão "Login" pressionado
            Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
            startActivity(intent);
        }
    }
}
