package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {

    private SQLiteHelper dbHelper;
    private CalendarView calendarView;
    private TextView expensesTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize views
        calendarView = rootView.findViewById(R.id.calendarView);
        expensesTextView = rootView.findViewById(R.id.expensesTextView);

        // Initialize database helper
        dbHelper = new SQLiteHelper(getContext());

        // Set calendar date change listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Get the selected date
            String selectedDate = formatDate(year, month, dayOfMonth);

            // Fetch expenses for the selected date from the database
            List<Expense> expenses = dbHelper.getExpensesByDate(selectedDate);

            // Display expenses in the TextView
            if (expenses != null && !expenses.isEmpty()) {
                StringBuilder expenseDetails = new StringBuilder("Expenses on " + selectedDate + ":\n");
                for (Expense expense : expenses) {
                    expenseDetails.append(expense.getDescription())
                            .append(" - ")
                            .append(expense.getAmount())
                            .append(" VND\n");
                }
                expensesTextView.setText(expenseDetails.toString());
            } else {
                expensesTextView.setText("No expenses for selected date");
            }
        });

        return rootView;
    }

    // Format the date to match the format in the database (e.g., "yyyy-MM-dd")
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }
}
