package com.example.campusexpensemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.descriptionTextView.setText(expense.getDescription());
        holder.amountTextView.setText(String.format("$%.2f", expense.getAmount()));
        holder.categoryTextView.setText(expense.getCategory());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public void updateExpenseList(List<Expense> newExpenseList) {
        this.expenseList = newExpenseList;
        notifyDataSetChanged();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView, amountTextView, categoryTextView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.description_text_view);
            amountTextView = itemView.findViewById(R.id.amount_text_view);
            categoryTextView = itemView.findViewById(R.id.category_text_view);
        }
    }
}
