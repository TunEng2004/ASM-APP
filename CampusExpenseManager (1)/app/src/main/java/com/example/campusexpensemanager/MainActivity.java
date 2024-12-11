package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SQLiteHelper dbHelper;
    private double totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo SQLiteHelper để quản lý cơ sở dữ liệu
        dbHelper = new SQLiteHelper(this);
        totalExpense = dbHelper.getTotalExpense();  // Lấy tổng chi tiêu ban đầu từ cơ sở dữ liệu

        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        // Mặc định hiển thị HomeFragment
        if (savedInstanceState == null) {
            // Hiển thị HomeFragment với tổng chi tiêu hiện tại
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment.newInstance(totalExpense))
                    .commit();
        }
    }

    // Listener cho BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = HomeFragment.newInstance(totalExpense);
            } else if (item.getItemId() == R.id.nav_calendar) {
                selectedFragment = new CalendarFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            } else if (item.getItemId() == R.id.nav_add) {
                selectedFragment = new AddExpenseFragment();
            } else {
                Toast.makeText(MainActivity.this, "Unknown navigation item", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Thay thế fragment hiện tại bằng fragment được chọn
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Khi quay lại MainActivity, chúng ta cập nhật tổng chi phí từ cơ sở dữ liệu
        totalExpense = dbHelper.getTotalExpense();  // Lấy tổng chi tiêu từ cơ sở dữ liệu
        // Thay thế HomeFragment với tổng chi tiêu mới
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
        if (homeFragment != null) {
            // Nếu fragment đã có, cập nhật lại tổng chi tiêu
            homeFragment.updateTotalExpense(totalExpense);
        } else {
            // Nếu fragment chưa có, tạo mới và hiển thị
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment.newInstance(totalExpense), HomeFragment.class.getSimpleName())
                    .commit();
        }
    }

    // Cập nhật totalExpense và HomeFragment khi có sự thay đổi
    public void updateTotalExpense(double newTotalExpense) {
        this.totalExpense = newTotalExpense;
        // Gọi phương thức cập nhật trong HomeFragment nếu cần
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName());
        if (homeFragment != null) {
            homeFragment.updateTotalExpense(newTotalExpense);
        }
    }
}
