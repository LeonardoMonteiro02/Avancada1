package com.example.myfleet;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText birthDateEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button saveButton;
    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_page);

        databaseHelper = new DatabaseHelper(this);

        fullNameEditText = findViewById(R.id.full_name);
        birthDateEditText = findViewById(R.id.date_of_birth);
        phoneEditText = findViewById(R.id.phone);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        saveButton = findViewById(R.id.save_button);

        // Formatação da data de nascimento (DD/MM/AAAA)
        birthDateEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String cleanC = current.replaceAll("[^\\d]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    // Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int month = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        // Verificar e ajustar os valores de dia, mês e ano se necessário
                        if (month > 12) {
                            month = 12;
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.MONTH, month - 1);
                        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        if (day > maxDay) {
                            day = maxDay;
                        }
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        clean = String.format("%02d%02d%04d", day, month, year);
                    }

                    String formattedBirthDate = clean.substring(0, 2) + "/" + clean.substring(2, 4) + "/" + clean.substring(4, 8);

                    sel = sel < 0 ? 0 : sel;
                    current = formattedBirthDate;
                    birthDateEditText.setText(current);
                    birthDateEditText.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });

        // Formatação do telefone ((XX) X.XXXX-XXXX)
        phoneEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String phoneNumberFormat = "(XX) X.XXXX-XXXX";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    phoneEditText.removeTextChangedListener(this);

                    String clean = s.toString().replaceAll("[^\\d]", "");
                    String formatted = "";

                    int digitCount = 0;
                    for (int i = 0; i < clean.length(); i++) {
                        if (digitCount == 0) {
                            formatted += "(";
                        } else if (digitCount == 2) {
                            formatted += ") ";
                        } else if (digitCount == 7) {
                            formatted += "-";
                        }

                        formatted += clean.charAt(i);

                        digitCount++;
                    }

                    current = formatted;
                    phoneEditText.setText(formatted);
                    phoneEditText.setSelection(formatted.length());

                    phoneEditText.addTextChangedListener(this);
                }
            }
        });

        // Configurar o teclado numérico para o campo de telefone
        phoneEditText.setInputType(InputType.TYPE_CLASS_PHONE);

        // Configurar o teclado numérico para o campo de data
        birthDateEditText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccount();
            }
        });
    }

    private void saveAccount() {
        // Obtenha os valores inseridos pelo usuário
        String fullName = fullNameEditText.getText().toString();
        String birthDate = birthDateEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Exemplo: Verificar se os campos estão preenchidos corretamente
        boolean isValid = true;

        if (fullName.isEmpty()) {
            fullNameEditText.setError("Campo obrigatório");
            isValid = false;
        } else if (!isValidFullName(fullName)) {
            fullNameEditText.setError("Nome inválido");
            isValid = false;
        }
        if (fullName.isEmpty()) {
            fullNameEditText.setError("Campo obrigatório");
            isValid = false;
        }

        if (birthDate.isEmpty()) {
            birthDateEditText.setError("Campo obrigatório");
            isValid = false;
        } else if (!isValidDate(birthDate)) {
            birthDateEditText.setError("Data inválida");
            isValid = false;
        }

        if (phone.isEmpty()) {
            phoneEditText.setError("Campo obrigatório");
            isValid = false;
        } else if (!isValidPhone(phone)) {
            phoneEditText.setError("Número de telefone inválido");
            isValid = false;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Campo obrigatório");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailEditText.setError("Email inválido");
            isValid = false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Campo obrigatório");
            isValid = false;
        } else if (!isStrongPassword(password)) {
            passwordEditText.setError("Senha fraca utilize caracteres e numeros");
            isValid = false;
        }


        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Campo obrigatório");
            isValid = false;
        }

        // Exemplo: Verificar se a senha e a confirmação de senha correspondem
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("A senha e a confirmação de senha não correspondem");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Salvar os dados no banco de dados
        long rowId = databaseHelper.insertAccount(fullName, birthDate, phone, email, password);

        if (rowId != -1) {
            // Os dados foram salvos com sucesso
            Toast.makeText(this, "Conta criada com sucesso. ID da linha: " + rowId, Toast.LENGTH_SHORT).show();
        } else {
            // Ocorreu um erro ao salvar os dados
            Toast.makeText(this, "Erro ao salvar a conta", Toast.LENGTH_SHORT).show();
        }

        // Finalize a atividade e retorne à MainActivity
        finish();
    }


    private boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phoneNumber) {
        String phonePattern = "\\(\\d{2}\\) \\d{5}-\\d{4}";
        return phoneNumber.matches(phonePattern);
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date parsedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int year = calendar.get(Calendar.YEAR);
            return year >= 1900 && year <= Calendar.getInstance().get(Calendar.YEAR);
        } catch (ParseException e) {
            return false;
        }
    }
    private boolean isValidFullName(String fullName) {
        if (fullName.isEmpty()) {
            return false;
        }

        String[] nameParts = fullName.split(" ");
        if (nameParts.length < 2) {
            return false;
        }

        for (String part : nameParts) {
            if (part.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private boolean isStrongPassword(String password) {
        // Verificar o comprimento da senha
        if (password.length() < 8) {
            return false;
        }

        // Verificar se a senha contém letras maiúsculas, minúsculas e números
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUpperCase && hasLowerCase && hasDigit;
    }


}
