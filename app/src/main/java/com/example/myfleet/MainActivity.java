package com.example.myfleet;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView carImage;
    private ImageView logoImage;
    private LinearLayout buttonLayout;
    private Button createButton;
    private Button loginButton;
    private TextView textBemvindo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carImage = findViewById(R.id.car);
        logoImage = findViewById(R.id.logo);
        buttonLayout = findViewById(R.id.button_layout);
        createButton = findViewById(R.id.create_account);
        loginButton = findViewById(R.id.login);
        textBemvindo = findViewById(R.id.textBemvindo);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/LTC Kennerley W00 Small Caps.ttf");
        textBemvindo.setTypeface(customFont);

        createButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);

        animateCar();
    }

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
            // Adicione aqui a lógica para a ação do botão "Login"
            Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
            startActivity(intent);
        }
    }
}