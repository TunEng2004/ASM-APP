package com.example.campusexpensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_data";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng điền tên đăng nhập và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkLogin(username, password)) {
                        Intent intent;
                        if (username.equalsIgnoreCase("admin")) {
                            intent = new Intent(LoginActivity.this, AdminActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkLogin(String username, String password) {
        String storedUsername = sharedPreferences.getString(USERNAME_KEY, null);
        String storedPassword = sharedPreferences.getString(PASSWORD_KEY, null);

        // Kiểm tra thông tin trong SharedPreferences trước
        if (storedUsername != null && storedPassword != null) {
            return username.equals(storedUsername) && password.equals(storedPassword);
        }

        // Tài khoản mặc định (nếu không có thông tin lưu trong SharedPreferences)
        return (username.equals("user") && password.equals("1234")) ||
                (username.equals("admin") && password.equals("admin123"));
    }
}
