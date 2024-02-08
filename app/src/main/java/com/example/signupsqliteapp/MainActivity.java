package com.example.signupsqliteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signupsqliteapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;

    private TableLayout userTable;
    private Button updateButton;
    private Button deleteAccountButton;
    private Button logoutButton;
    private Button sendDetailsButton;
    private Context context;
    private List<User> userList;
    private List<User> filteredUserList;
    private TableRow tableHeader;
    private EditText filterEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Inicializar os componentes da UI
        filterEditText = findViewById(R.id.filterEditText);
        userTable = findViewById(R.id.user_table);
        updateButton = findViewById(R.id.updateButton);
        deleteAccountButton = findViewById(R.id.deleteButton);
        logoutButton = findViewById(R.id.logoutButton);
        sendDetailsButton = findViewById(R.id.sendDetailsButton);
        context = this;

        // Carrega e exibe usuários na tabela
        initTableHeader();
        userList = databaseHelper.getAllUsers();
        setupTable(userList);

        // Configura listener para o botão de atualização
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para atualizar o usuário logado
                updateUser();
            }
        });

        // Configura listener para o botão de deletar conta
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lógica para deletar a conta do usuário logado
                deleteAccount();
            }
        });

        // Configura listener para o botão de logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // Configura listener para o botão de envio de requisicao
        sendDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendDetailsTask sendDetailsTask = new SendDetailsTask(context, databaseHelper, sessionManager);
                sendDetailsTask.execute();
            }
        });


        // Configura listener da caixa de Texto da filtragem do Nome
        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterUsers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void setupTable(List<User> userList) {
        for (User user : userList) {
            TableRow row = new TableRow(this);


            TextView idTextView = new TextView(this);
            idTextView.setText(String.valueOf(user.getId()));
            idTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));// Peso 1
            idTextView.setPadding(32, 32, 32, 32); // Adicione padding conforme necessário
            idTextView.setSingleLine(true);
            idTextView.setTextColor(Color.BLACK);
            row.addView(idTextView);


            TextView emailTextView = new TextView(this);
            String emailTextViewString = user.getEmail() != null ? user.getEmail() : "Nulo";
            emailTextView.setText(emailTextViewString);
            emailTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)); // Peso 2
            emailTextView.setPadding(32, 32, 32, 32);
            emailTextView.setSingleLine(true);
            emailTextView.setTextColor(Color.BLACK);
            row.addView(emailTextView);


            TextView usernameTextView = new TextView(this);
            String usernameTextViewString = user.getUsername() != null ? user.getUsername() : "Nulo";
            usernameTextView.setText(usernameTextViewString);
            usernameTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)); // Peso 2
            usernameTextView.setPadding(32, 32, 32, 32);
            usernameTextView.setSingleLine(true);
            usernameTextView.setTextColor(Color.BLACK);
            row.addView(usernameTextView);


            TextView nomeTextView = new TextView(this);
            String nomeTextViewString = user.getNome() != null ? user.getNome() : "Nulo";
            nomeTextView.setText(nomeTextViewString);
            nomeTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)); // Peso 2
            nomeTextView.setPadding(32, 32, 32, 32);
            nomeTextView.setSingleLine(true);
            nomeTextView.setTextColor(Color.BLACK);
            row.addView(nomeTextView);


            TextView sexoTextView = new TextView(this);
            String sexoTextViewString = user.getSexo() == 1 ? "Masculino" : (user.getSexo() == 2 ? "Feminino" : "Nulo");
            sexoTextView.setText(sexoTextViewString);
            sexoTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Peso 1
            sexoTextView.setPadding(32, 32, 32, 32);
            sexoTextView.setSingleLine(true);
            sexoTextView.setTextColor(Color.BLACK);
            row.addView(sexoTextView);


            userTable.addView(row);
        }
    }


    private void updateUser() {
        Intent intent = new Intent(getApplicationContext(), RegistryActivity.class);
        startActivity(intent);
    }

    private void deleteAccount() {
        // Cria uma janela de confirmação
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmação");
        builder.setMessage("Tem certeza que deseja excluir sua conta?");

        // Adiciona botões de confirmação e cancelamento
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int userId = sessionManager.getUserId(); // Obtenha o ID do usuário logado
                databaseHelper.deleteUser(userId);

                // Após excluir a conta, redirecione para a tela de login
                logout();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Mostra a janela de confirmação
        builder.create().show();
    }

    private void logout() {
        // Limpar as informações da sessão
        sessionManager.logout();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Certifique-se de chamar finish() para fechar a atividade atual
    }

    // Função para filtrar usuários
    private void filterUsers(String filterText) {
        filteredUserList = new ArrayList<>();

        // Se o texto do filtro estiver vazio, exiba todos os usuários
        if (filterText.isEmpty()) {
            filteredUserList.addAll(userList);
        } else {
            for (User user : userList) {
                if (user.getNome().toLowerCase().contains(filterText.toLowerCase())) {
                    filteredUserList.add(user);
                }
            }
        }

        // Limpa a tabela antes de adicionar as linhas filtradas, header vai junto pois esta dentro da table layout
        TableLayout tableLayout = findViewById(R.id.user_table);
        tableLayout.removeAllViews();

        //Reinicia o header
        tableLayout.addView(tableHeader);

        for (User user : filteredUserList) {
            addTableRow(user);
        }
    }

    // Função para adicionar uma linha à tabela
    private void addTableRow(User user) {
        TableLayout tableLayout = findViewById(R.id.user_table);

        TableRow row = new TableRow(this);

        TextView idTextView = new TextView(this);
        idTextView.setText(String.valueOf(user.getId()));
        idTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));// Peso 1
        idTextView.setPadding(32, 32, 32, 32); // Adicione padding conforme necessário
        idTextView.setSingleLine(true);
        idTextView.setTextColor(Color.BLACK);
        row.addView(idTextView);


        TextView emailTextView = new TextView(this);
        String emailTextViewString = user.getEmail() != null ? user.getEmail() : "Nulo";
        emailTextView.setText(emailTextViewString);
        emailTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)); // Peso 2
        emailTextView.setPadding(32, 32, 32, 32);
        emailTextView.setSingleLine(true);
        emailTextView.setTextColor(Color.BLACK);
        row.addView(emailTextView);


        TextView usernameTextView = new TextView(this);
        String usernameTextViewString = user.getUsername() != null ? user.getUsername() : "Nulo";
        usernameTextView.setText(usernameTextViewString);
        usernameTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)); // Peso 2
        usernameTextView.setPadding(32, 32, 32, 32);
        usernameTextView.setSingleLine(true);
        usernameTextView.setTextColor(Color.BLACK);
        row.addView(usernameTextView);


        TextView nomeTextView = new TextView(this);
        String nomeTextViewString = user.getNome() != null ? user.getNome() : "Nulo";
        nomeTextView.setText(nomeTextViewString);
        nomeTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 2f)); // Peso 2
        nomeTextView.setPadding(32, 32, 32, 32);
        nomeTextView.setSingleLine(true);
        nomeTextView.setTextColor(Color.BLACK);
        row.addView(nomeTextView);


        TextView sexoTextView = new TextView(this);
        String sexoTextViewString = user.getSexo() == 1 ? "Masculino" : (user.getSexo() == 2 ? "Feminino" : "Nulo");
        sexoTextView.setText(sexoTextViewString);
        sexoTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f)); // Peso 1
        sexoTextView.setPadding(32, 32, 32, 32);
        sexoTextView.setSingleLine(true);
        sexoTextView.setTextColor(Color.BLACK);
        row.addView(sexoTextView);
        tableLayout.addView(row);
    }

    private void initTableHeader() {
        TableLayout tableLayout = findViewById(R.id.user_table);

        tableHeader = new TableRow(this);

        TextView idHeader = createHeaderTextView("ID");
        tableHeader.addView(idHeader);
        TextView emailHeader = createHeaderTextView("E-mail");
        tableHeader.addView(emailHeader);

        TextView usuarioHeader = createHeaderTextView("Usuario");
        tableHeader.addView(usuarioHeader);

        TextView nomeHeader = createHeaderTextView("Nome");
        tableHeader.addView(nomeHeader);

        TextView Genero = createHeaderTextView("Genero");
        tableHeader.addView(Genero);

        tableLayout.addView(tableHeader);
    }

    // Função utilitária para criar TextViews de cabeçalho
    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView.setPadding(32, 32, 32, 32);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }
}
