package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Lấy nút "Quay lại đăng nhập"
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Xử lý sự kiện khi nhấn nút "Quay lại đăng nhập"
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại trang đăng nhập
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng AdminActivity
            }
        });
    }
}
