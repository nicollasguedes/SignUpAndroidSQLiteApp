package com.example.signupsqliteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signupsqliteapp.databinding.ActivitySignUpBinding;


public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupConfirm.getText().toString();

                if (email.equals("") || password.equals("") || confirmPassword.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirmPassword)) {
                        if (isValidPassword(password)) {
                            Boolean checkUserEmail = databaseHelper.checkEmail(email);

                            if (!checkUserEmail) {
                                User user = new User();
                                user.setEmail(email);
                                user.setPassword(password);
                                Boolean insert = databaseHelper.insertData(user);

                                if (insert) {
                                    int userId = databaseHelper.getUserIdByEmail(email);
                                    sessionManager.saveUserSession(email, userId);
                                    Toast.makeText(SignUpActivity.this, "Inscrição efetuada com Sucesso! Por favor preencha o formulario de registro", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), RegistryActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Incrição Falhou!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUpActivity.this, "Usuario já existe, tente o login", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Senha inválida. A senha deve ter pelo menos 8 caracteres, um número e uma letra maiúscula.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Senhas precisam ser iguais", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
    // Função para verificar se a senha é válida
    private boolean isValidPassword(String password) {
        // Verifica se a senha tem pelo menos 8 caracteres
        if (password.length() < 8) {
            return false;
        }

        // Verifica se a senha contém pelo menos um número
        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }

        // Verifica se a senha contém pelo menos uma letra maiúscula
        boolean containsUpperCase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUpperCase = true;
                break;
            }
        }

        // A senha é válida se atender a todos os critérios
        return containsDigit && containsUpperCase;
    }
}