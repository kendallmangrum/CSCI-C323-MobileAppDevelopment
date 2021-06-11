package com.c323proj9.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    Create variables that will be needed
    Spinner spinner;
    ArrayAdapter<CharSequence> dropdownAdapter;
    EditText etExpenseName, etMoneySpent;
    Button bttnAddExpense, bttnViewExpenses;
    ImageView ivProfile;
    DatePicker datePicker;
    AppDatabase database;
    ExpenseDao expenseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Get the spinner element and fill it with options
        spinner = findViewById(R.id.spinner);
        dropdownAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownAdapter);

//        Get the edittexts and datepicker
        etExpenseName = findViewById(R.id.etExpense);
        etMoneySpent = findViewById(R.id.etMoneySpent);
        datePicker = findViewById(R.id.datePicker);

//        Get the buttons and image view to set profile picture
        bttnAddExpense = findViewById(R.id.bttnAddExpense);
        bttnViewExpenses = findViewById(R.id.bttnViewExpenses);
        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setImageResource(R.drawable.profile_picture);

//        Build database
        database = Room.databaseBuilder(MainActivity.this, AppDatabase.class, "expenses").allowMainThreadQueries().build();
        expenseDao = database.getExpenseDao();
    }

//    Method to add expense to the database
    public void addExpense(View view) {
//      Create new expense instance and give it the information the uesr input
        Expense expense = new Expense();
        expense.setName(etExpenseName.getText().toString());
        expense.setCost(etMoneySpent.getText().toString());
        expense.setCategory(spinner.getSelectedItem().toString());
        expense.setDate(datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear());

//        Clear the editTexts and insert the expense into the database
        etExpenseName.setText("");
        etMoneySpent.setText("");
        expenseDao.insert(expense);
    }

//    Onclick method for button to view expenses; take user to second activity
    public void viewExpenses(View view) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }

}