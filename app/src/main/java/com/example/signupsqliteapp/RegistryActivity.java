package com.example.signupsqliteapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signupsqliteapp.databinding.ActivityRegistryBinding;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class RegistryActivity extends AppCompatActivity {
    ActivityRegistryBinding binding;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;

    private EditText userNameEditText;
    private EditText nameEditText;
    private EditText birthDateEditText;
    private CheckBox tipoPfCheckBox;
    private CheckBox tipoPjCheckBox;
    private EditText cpfCnpjEditText;
    private EditText countryEditText;
    private EditText cityEditText;
    private EditText stateEditText;
    private EditText addressEditText;
    private Spinner sexoSpinner;
    private ImageView userImageView;
    private Button selectImageButton;
    private String base64Image;
    private User sessionUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        initializeViews();
        loadUserDetailsById(sessionManager.getUserId());
        setupUserMail();
        setupImage();
        setupDateField();
        setupCpfField();
        setupGenderField();
        setupSaveButton();
    }

    private void initializeViews() {
        userNameEditText = findViewById(R.id.res_user_name);
        nameEditText = findViewById(R.id.res_name);
        birthDateEditText = findViewById(R.id.res_birth_date);
        tipoPfCheckBox = findViewById(R.id.res_tipo_pf);
        tipoPjCheckBox = findViewById(R.id.res_tipo_pj);
        cpfCnpjEditText = findViewById(R.id.res_cpf_cnpj);
        sexoSpinner = findViewById(R.id.res_sex_spinner);
        countryEditText = findViewById(R.id.res_country);
        cityEditText = findViewById(R.id.res_address_city);
        stateEditText = findViewById(R.id.res_address_state);
        addressEditText = findViewById(R.id.res_address);
        userImageView = findViewById(R.id.res_user_image);
        selectImageButton = findViewById(R.id.res_select_image_button);
    }

    public void loadUserDetailsById(int userId) {
        User user = databaseHelper.getUserById(userId);

        if (user != null) {
            if (user.getFoto() != null && !user.getFoto().isEmpty()) {
                ImageView userImageView = findViewById(R.id.res_user_image);
                Bitmap userBitmap = decodeBase64ToBitmap(user.getFoto());
                userImageView.setImageBitmap(userBitmap);
            }

            if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                userNameEditText.setText(user.getUsername());
            }

            if (user.getNome() != null && !user.getNome().isEmpty()) {
                nameEditText.setText(user.getNome());
            }

            if (user.getDataNascimento() != null && !user.getDataNascimento().isEmpty()) {
                birthDateEditText.setText(user.getDataNascimento());
            }

            if (user.getTipo() != null && !user.getTipo().isEmpty()) {
                if ("Pessoa Física".equals(user.getTipo())) {
                    tipoPfCheckBox.setChecked(true);
                } else {
                    tipoPjCheckBox.setChecked(true);
                }
            }

            if (user.getCpfCnpj() != null && !user.getCpfCnpj().isEmpty()) {
                cpfCnpjEditText.setText(user.getCpfCnpj());
            }

            String[] sexoOptions = getResources().getStringArray(R.array.sex_options);
            int genderPosition = Arrays.asList(sexoOptions).indexOf((user.getSexo() == 1) ? "Masculino"
                    : (user.getSexo() == 2) ? "Feminino" : "");
            if (genderPosition != -1) {
                sexoSpinner.setSelection(genderPosition);
            }

            if (user.getEndereco() != null && !user.getEndereco().isEmpty()) {
                String[] addressComponents = user.getEndereco().split(" - ");
                if (addressComponents.length == 4) {
                    if (addressComponents[0] != null && !addressComponents[0].isEmpty()) {
                        countryEditText.setText(addressComponents[0]);
                    }

                    if (addressComponents[1] != null && !addressComponents[1].isEmpty()) {
                        stateEditText.setText(addressComponents[1]);
                    }

                    if (addressComponents[2] != null && !addressComponents[2].isEmpty()) {
                        cityEditText.setText(addressComponents[2]);
                    }

                    if (addressComponents[3] != null && !addressComponents[3].isEmpty()) {
                        addressEditText.setText(addressComponents[3]);
                    }
                }
            }
        }
    }

    void setupUserMail() {
        if (sessionManager.isLoggedIn()) {
            String userEmail = sessionManager.getUserEmail();
            TextView emailText = findViewById(R.id.res_email);
            emailText.setText(userEmail);
        } else {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void setupImage() {
        selectImageButton.setOnClickListener(view -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                userImageView.setImageBitmap(bitmap);

                base64Image = convertBitmapToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    void setupDateField() {
        EditText dataNascimentoEditText = findViewById(R.id.res_birth_date);
        SimpleMaskFormatter dataNascimentoMask = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher dataNascimentoMaskWatcher = new MaskTextWatcher(dataNascimentoEditText, dataNascimentoMask);
        dataNascimentoEditText.addTextChangedListener(dataNascimentoMaskWatcher);
    }

    void setupCpfField() {
        CheckBox tipoPfCheckBox = findViewById(R.id.res_tipo_pf);
        CheckBox tipoPjCheckBox = findViewById(R.id.res_tipo_pj);
        EditText cpfCnpjEditText = findViewById(R.id.res_cpf_cnpj);

        tipoPfCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tipoPjCheckBox.setChecked(false);
                setupCpfCnpjField(true);
                cpfCnpjEditText.setEnabled(true);
            } else {
                cpfCnpjEditText.getText().clear();
                cpfCnpjEditText.setEnabled(false);
            }
        });

        tipoPjCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tipoPfCheckBox.setChecked(false);
                setupCpfCnpjField(false);
                cpfCnpjEditText.setEnabled(true);
            } else {
                cpfCnpjEditText.getText().clear();
                cpfCnpjEditText.setEnabled(false);
            }
        });
        cpfCnpjEditText.setEnabled(false);
    }

    void setupCpfCnpjField(boolean isPessoaFisica) {
        EditText cpfCnpjEditText = findViewById(R.id.res_cpf_cnpj);
        SimpleMaskFormatter cpfMask = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        SimpleMaskFormatter cnpjMask = new SimpleMaskFormatter("NN.NNN.NNN/NNNN-NN");

        MaskTextWatcher cpfCnpjMaskWatcher = new MaskTextWatcher(cpfCnpjEditText, isPessoaFisica ? cpfMask : cnpjMask);
        cpfCnpjEditText.removeTextChangedListener(cpfCnpjMaskWatcher);
        cpfCnpjEditText.addTextChangedListener(cpfCnpjMaskWatcher);
    }

    void setupGenderField() {
        Spinner sexoSpinner = findViewById(R.id.res_sex_spinner);

        String[] sexoOptions = getResources().getStringArray(R.array.sex_options);

        ArrayAdapter<String> sexoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sexoOptions);

        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sexoSpinner.setAdapter(sexoAdapter);
    }

    private boolean validateFields() {

        if (userNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome de usuário.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira o nome.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (birthDateEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira a data de nascimento.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(tipoPfCheckBox.isChecked() || tipoPjCheckBox.isChecked())) {
            Toast.makeText(this, "Por favor, selecione o tipo (Pessoa Física ou Pessoa Jurídica).", Toast.LENGTH_SHORT).show();
            return false;
        }

        String cpfCnpj = cpfCnpjEditText.getText().toString();
        if (cpfCnpj.isEmpty()) {
            Toast.makeText(this, "Por favor, insira o CPF/CNPJ.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tipoPfCheckBox.isChecked() && !isValidCPF(cpfCnpj)) {
            Toast.makeText(this, "CPF inválido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tipoPjCheckBox.isChecked() && !isValidCNPJ(cpfCnpj)) {
            Toast.makeText(this, "CNPJ inválido.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String generoSelecionado = sexoSpinner.getSelectedItem().toString();
        if (generoSelecionado.equals("Selecionar")) {
            Toast.makeText(this, "Por favor, selecione o gênero.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (countryEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira o país.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cityEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira a cidade.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (stateEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira o estado.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (addressEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Por favor, insira o endereço.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    private boolean isValidCPF(String cpf) {
        String numbersOnly = cpf.replaceAll("[^0-9]", "");

        if (numbersOnly.length() != 11) {
            return false;
        }

        if (numbersOnly.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(numbersOnly.charAt(i)) * (10 - i);
        }
        int remainder = sum % 11;
        int firstDigit = (remainder < 2) ? 0 : 11 - remainder;

        if (Character.getNumericValue(numbersOnly.charAt(9)) != firstDigit) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(numbersOnly.charAt(i)) * (11 - i);
        }
        remainder = sum % 11;
        int secondDigit = (remainder < 2) ? 0 : 11 - remainder;

        return Character.getNumericValue(numbersOnly.charAt(10)) == secondDigit;
    }

    private boolean isValidCNPJ(String cnpj) {
        String numbersOnly = cnpj.replaceAll("[^0-9]", "");

        if (numbersOnly.length() != 14) {
            return false;
        }

        int sum = 0;
        int[] weights = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(numbersOnly.charAt(i)) * weights[i];
        }
        int remainder = sum % 11;
        int firstDigit = (remainder < 2) ? 0 : 11 - remainder;

        if (Character.getNumericValue(numbersOnly.charAt(12)) != firstDigit) {
            return false;
        }

        sum = 0;
        weights = new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 13; i++) {
            sum += Character.getNumericValue(numbersOnly.charAt(i)) * weights[i];
        }
        remainder = sum % 11;
        int secondDigit = (remainder < 2) ? 0 : 11 - remainder;

        return Character.getNumericValue(numbersOnly.charAt(13)) == secondDigit;
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.res_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    User user = createUserObject();
                    boolean isSaved = databaseHelper.updateData(user, String.valueOf(sessionManager.getUserId()));
                    if (isSaved) {
                        Toast.makeText(RegistryActivity.this, "dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegistryActivity.this, "Erro ao salvar dados.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private User createUserObject() {
        User user = new User();
        user.setUsername(userNameEditText.getText().toString());
        user.setNome(nameEditText.getText().toString());
        user.setDataNascimento(birthDateEditText.getText().toString());
        user.setTipo(tipoPfCheckBox.isChecked() ? "Pessoa Física" : "Pessoa Jurídica");
        user.setCpfCnpj(cpfCnpjEditText.getText().toString());

        user.setEndereco(countryEditText.getText().toString()
                + " - " + stateEditText.getText().toString()
                + " - " + cityEditText.getText().toString()
                + " - " + addressEditText.getText().toString());

        String selectedGender = sexoSpinner.getSelectedItem().toString();
        int genderCode = getGenderCode(selectedGender);
        user.setSexo(genderCode);
        user.setFoto(base64Image);
        return user;
    }

    private int getGenderCode(String selectedGender) {
        if (selectedGender.equals("Masculino")) {
            return 1;
        } else if (selectedGender.equals("Feminino")) {
            return 2;
        } else {
            return 0;
        }

    }
}