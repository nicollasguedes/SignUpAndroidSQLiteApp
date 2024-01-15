package com.example.signupsqliteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signupsqliteapp.databinding.ActivityLoginBinding;


public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isUserCredentialsValid = databaseHelper.checkEmailPassword(email, password);
                    boolean isUserRegistryValid = databaseHelper.checkRegistry(email, password);

                    if (isUserCredentialsValid) {
                        int userId = databaseHelper.getUserIdByEmail(email);
                        sessionManager.saveUserSession(email, userId);

                        if (isUserRegistryValid) {
                            Toast.makeText(LoginActivity.this, "Login efetuado com Sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login efetuado com Sucesso! Por favor preencha o formulario de registro", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), RegistryActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciais Invalidas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}