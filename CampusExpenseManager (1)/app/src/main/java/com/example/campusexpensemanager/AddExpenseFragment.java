package com.example.campusexpensemanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AddExpenseFragment extends Fragment {

    private EditText editTextDescription, editTextAmount, editTextCategory;
    private Button buttonAddExpense;
    private SQLiteHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_expense, container, false);

        // Initialize SQLiteHelper to interact with the database
        dbHelper = new SQLiteHelper(getContext());

        // Find EditText and Button in the layout
        editTextDescription = rootView.findViewById(R.id.editTextDescription);
        editTextAmount = rootView.findViewById(R.id.editTextAmount);
        editTextCategory = rootView.findViewById(R.id.editTextCategory);
        buttonAddExpense = rootView.findViewById(R.id.buttonAddExpense);

        // Set onClickListener for Add Expense button
        buttonAddExpense.setOnClickListener(v -> {
            // Get expense information from EditText fields
            String description = editTextDescription.getText().toString().trim();
            String amountStr = editTextAmount.getText().toString().trim();
            String category = editTextCategory.getText().toString().trim();

            // Validate input fields
            if (TextUtils.isEmpty(description) || TextUtils.isEmpty(amountStr) || TextUtils.isEmpty(category)) {
                Toast.makeText(getContext(), "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Try to parse the amount as a double
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter a valid amount!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add expense to the database
            dbHelper.addExpense(description, amount, category);

            // Update the total expense in the main activity
            double newTotalExpense = dbHelper.getTotalExpense();
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.updateTotalExpense(newTotalExpense);  // Update total expense
            }

            // Provide feedback to the user
            Toast.makeText(getContext(), "Expense added successfully!", Toast.LENGTH_SHORT).show();

            // Clear input fields after submission
            clearFields();

            // Close the keyboard
            closeKeyboard();

            // Go back to the previous fragment
            getFragmentManager().popBackStack();
        });

        return rootView;
    }

    // Clear the input fields after submission
    private void clearFields() {
        editTextDescription.setText("");
        editTextAmount.setText("");
        editTextCategory.setText("");
    }

    // Close the keyboard
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }
}
