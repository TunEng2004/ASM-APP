package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment {

    private TextView totalExpenseTextView;
    private RecyclerView expenseRecyclerView;
    private SQLiteHelper dbHelper;
    private ExpenseAdapter expenseAdapter;

    private double totalExpense;

    public static HomeFragment newInstance(double initialExpense) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putDouble("totalExpense", initialExpense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new SQLiteHelper(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        totalExpenseTextView = view.findViewById(R.id.total_expense_text_view);
        Button resetButton = view.findViewById(R.id.reset_button);

        // Set up RecyclerView
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter(getRecentExpenses());
        expenseRecyclerView.setAdapter(expenseAdapter);

        // Hiển thị tổng chi tiêu ban đầu từ đối số truyền vào
        if (getArguments() != null) {
            totalExpense = getArguments().getDouble("totalExpense", 0.0);
        }
        updateTotalExpense(totalExpense);

        // Xử lý sự kiện khi nhấn nút Reset
        resetButton.setOnClickListener(v -> {
            dbHelper.resetTotalExpense(); // Reset dữ liệu trong database
            totalExpense = 0.0; // Đặt lại tổng chi tiêu trong code
            updateTotalExpense(totalExpense); // Cập nhật giao diện
            expenseAdapter.updateExpenseList(getRecentExpenses()); // Update RecyclerView
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Cập nhật lại tổng chi tiêu khi quay lại HomeFragment
        totalExpense = dbHelper.getTotalExpense();
        updateTotalExpense(totalExpense); // Cập nhật giao diện khi quay lại
        expenseAdapter.updateExpenseList(getRecentExpenses()); // Update RecyclerView
    }

    // Cập nhật TextView hiển thị tổng chi tiêu
    public void updateTotalExpense(double newTotalExpense) {
        totalExpense = newTotalExpense;
        if (totalExpenseTextView != null) {
            totalExpenseTextView.setText(String.format("Total Expense: %.2f", totalExpense));
        }
    }

    // Get the most recent expenses from the database
    private List<Expense> getRecentExpenses() {
        return dbHelper.getRecentExpenses(); // This method should return a list of recent expenses from the database
    }
}
