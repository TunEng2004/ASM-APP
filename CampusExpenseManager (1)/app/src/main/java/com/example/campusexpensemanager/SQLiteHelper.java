package com.example.campusexpensemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EXPENSES = "expenses";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng expenses
        String CREATE_TABLE = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_AMOUNT + " REAL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_TIMESTAMP + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Thêm chi tiêu mới vào cơ sở dữ liệu
    public void addExpense(String description, double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_TIMESTAMP, System.currentTimeMillis()); // Lưu thời gian chi tiêu

        try {
            db.insert(TABLE_EXPENSES, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public double getTotalExpense() {
        double totalExpense = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") as Total FROM " + TABLE_EXPENSES, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("Total");
            if (columnIndex >= 0) {
                totalExpense = cursor.getDouble(columnIndex);
            } else {
                // Handle the case when the column is not found
                Log.e("SQLiteHelper", "Column 'Total' not found in query result.");
            }
        }

        cursor.close();
        db.close();
        return totalExpense;
    }


    public List<Expense> getAllExpenses() {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");

        if (cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
                int descriptionColumnIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int amountColumnIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
                int categoryColumnIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                int timestampColumnIndex = cursor.getColumnIndex(COLUMN_TIMESTAMP);

                // Check that the column indices are valid
                if (idColumnIndex >= 0 && descriptionColumnIndex >= 0 && amountColumnIndex >= 0 &&
                        categoryColumnIndex >= 0 && timestampColumnIndex >= 0) {

                    int id = cursor.getInt(idColumnIndex);
                    String description = cursor.getString(descriptionColumnIndex);
                    double amount = cursor.getDouble(amountColumnIndex);
                    String category = cursor.getString(categoryColumnIndex);
                    long timestamp = cursor.getLong(timestampColumnIndex);

                    expenses.add(new Expense(id, description, amount, category, timestamp));
                } else {
                    // Handle the case where one or more columns are missing
                    Log.e("SQLiteHelper", "Some required columns are missing in the query result.");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenses;
    }
    public List<Expense> getExpensesByDate(String selectedDate) {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Assuming selectedDate is in "yyyy-MM-dd" format
        // Convert the selected date to a timestamp (beginning of the day)
        String[] dateParts = selectedDate.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Month is 0-based
        int day = Integer.parseInt(dateParts[2]);

        // Get the start of the selected date in timestamp (00:00:00)
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        long startOfDayTimestamp = calendar.getTimeInMillis();

        // Get the end of the selected date in timestamp (23:59:59)
        calendar.set(year, month, day, 23, 59, 59);
        long endOfDayTimestamp = calendar.getTimeInMillis();

        // Query expenses within the selected date range (startOfDayTimestamp to endOfDayTimestamp)
        String selection = COLUMN_TIMESTAMP + " BETWEEN ? AND ?";
        String[] selectionArgs = {String.valueOf(startOfDayTimestamp), String.valueOf(endOfDayTimestamp)};

        Cursor cursor = db.query(TABLE_EXPENSES, null, selection, selectionArgs, null, null, COLUMN_TIMESTAMP + " DESC");
        if (cursor.moveToFirst()) {
            // Get column names dynamically for debugging
            String[] columnNames = cursor.getColumnNames();
            Log.d("SQLiteHelper", "Columns: " + Arrays.toString(columnNames));

            do {
                int idColumnIndex = cursor.getColumnIndex(COLUMN_ID);
                int descriptionColumnIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int amountColumnIndex = cursor.getColumnIndex(COLUMN_AMOUNT);
                int categoryColumnIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
                int timestampColumnIndex = cursor.getColumnIndex(COLUMN_TIMESTAMP);

                // Log the column indexes for debugging
                Log.d("SQLiteHelper", "Column Indexes: " +
                        "ID: " + idColumnIndex +
                        ", Description: " + descriptionColumnIndex +
                        ", Amount: " + amountColumnIndex +
                        ", Category: " + categoryColumnIndex +
                        ", Timestamp: " + timestampColumnIndex);

                if (idColumnIndex >= 0 && descriptionColumnIndex >= 0 && amountColumnIndex >= 0 &&
                        categoryColumnIndex >= 0 && timestampColumnIndex >= 0) {
                    int id = cursor.getInt(idColumnIndex);
                    String description = cursor.getString(descriptionColumnIndex);
                    double amount = cursor.getDouble(amountColumnIndex);
                    String category = cursor.getString(categoryColumnIndex);
                    long timestamp = cursor.getLong(timestampColumnIndex);

                    expenses.add(new Expense(id, description, amount, category, timestamp));
                } else {
                    // Log error if any column is missing
                    Log.e("SQLiteHelper", "One or more columns are missing in the cursor.");
                }
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return expenses;
    }

    // Đặt lại tổng chi tiêu (Xóa tất cả chi tiêu)
    public void resetTotalExpense() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_EXPENSES, null, null); // Xóa toàn bộ dữ liệu trong bảng expenses
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
    List<Expense> getRecentExpenses() {
        SQLiteHelper dbHelper = null;
        return dbHelper.getRecentExpenses(); // This will now work
    }


}

