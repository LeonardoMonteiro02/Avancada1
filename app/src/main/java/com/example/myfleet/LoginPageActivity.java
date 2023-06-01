package com.example.myfleet;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myfleet.ActivityLogin;
import com.example.myfleet.R;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginEmailButton;
    private Button loginFacebookButton;
    private Button loginButton;
    private TextView createAccountTextView;

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

        loginEmailButton.setOnClickListener(this);
        loginFacebookButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        createAccountTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLoginEmail) {
            Toast.makeText(this, "Login com Gmail", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.buttonFacebook) {
            Toast.makeText(this, "Login com Facebook", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.buttonLoginLoginPage) {
            Intent intent = new Intent(LoginPageActivity.this, ActivityLogin.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textViewCreateAccount) {
            Toast.makeText(this, "Problemas com login?", Toast.LENGTH_SHORT).show();
            // Adicione aqui a lógica para a ação do texto "Problemas com login?"
        }
    }
}
