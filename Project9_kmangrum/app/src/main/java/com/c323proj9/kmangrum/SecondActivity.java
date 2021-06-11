package com.c323proj9.kmangrum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

//    Create variables that will be needed
    Spinner spinner;
    ArrayAdapter<CharSequence> dropdownAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MainAdapter adapter;
    AppDatabase database;
    List<Expense> expenseList;
    EditText etSearchCriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        Set it so that the keyboard popup doesn't change the look of the screen/pushup the recyclerview
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//        Get the spinner, provide its options
        spinner = findViewById(R.id.spinner2);
        dropdownAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownAdapter);

//        Get the recyclerview
        recyclerView = findViewById(R.id.recyclerView);

//        Get a reference of the database
        database = AppDatabase.getDBInstance(this);

//      Get a list of all the expenses
        expenseList = database.getExpenseDao().getAllExpenses();

//        Set up the recyclerview
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

//        Sort the expenses by date. If they are edited, the activity needs to be reloaded to resort
        Collections.sort(expenseList, new Comparator<Expense>() {
            @Override
            public int compare(Expense o1, Expense o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        adapter = new MainAdapter(SecondActivity.this, expenseList);
        recyclerView.setAdapter(adapter);
    }

//  Method to search the expenses
    public void searchExpenses(View view) {
        etSearchCriteria = findViewById(R.id.etExpenseSearch);
        spinner = findViewById(R.id.spinner2);

//        Get the data from the search field and the spinner
        String criteria = etSearchCriteria.getText().toString();
        String category = spinner.getSelectedItem().toString();

//        If the search field is left empty, query the database to select everything that matches the selected category
        if (criteria.equals("")) {
            List<Expense> selectedCatExpenses;
            selectedCatExpenses = database.getExpenseDao().getSearchCategory(category);

//            If there are no matching results, display a toast that there are no matching results
            if (selectedCatExpenses.size() == 0) {
                Toast.makeText(this, "No matching expenses", Toast.LENGTH_LONG).show();
            }

//            Reset the recyclerview to display only those matching results
            MainAdapter adapter = new MainAdapter(this, selectedCatExpenses);
            recyclerView.setAdapter(null);
            recyclerView.setAdapter(adapter);

//            Check if the data contains a decimal, which means that the user is searching for the expense cost
        } else {
            if (criteria.contains(".")) {
                List<Expense> selectedCatExpenses;
                selectedCatExpenses = database.getExpenseDao().getSearchCost(criteria, category);

//            If there are no matching results, display a toast that there are no matching results
                if (selectedCatExpenses.size() == 0) {
                    Toast.makeText(this, "No matching expenses", Toast.LENGTH_LONG).show();
                }
//            Reset the recyclerview to display only those matching results
                MainAdapter adapter = new MainAdapter(this, selectedCatExpenses);
                recyclerView.setAdapter(null);
                recyclerView.setAdapter(adapter);

//            Check if the data contains a /, which means that the user is searching for the expense date
            } else if (criteria.contains("/")) {
                List<Expense> selectedCatExpenses;
                selectedCatExpenses = database.getExpenseDao().getSearchDate(criteria, category);

//            If there are no matching results, display a toast that there are no matching results
                if (selectedCatExpenses.size() == 0) {
                    Toast.makeText(this, "No matching expenses", Toast.LENGTH_LONG).show();
                }

//            Reset the recyclerview to display only those matching results
                MainAdapter adapter = new MainAdapter(this, selectedCatExpenses);
                recyclerView.setAdapter(null);
                recyclerView.setAdapter(adapter);
//                If the text doesn't contain a decimal or a backslash, we assume the user is just searching by a name
            } else {
                List<Expense> selectedCatExpenses;
                selectedCatExpenses = database.getExpenseDao().getSearchName(criteria, category);

//            If there are no matching results, display a toast that there are no matching results
                if (selectedCatExpenses.size() == 0) {
                    Toast.makeText(this, "No matching expenses", Toast.LENGTH_LONG).show();
                }

//            Reset the recyclerview to display only those matching results
                MainAdapter adapter = new MainAdapter(this, selectedCatExpenses);
                recyclerView.setAdapter(null);
                recyclerView.setAdapter(adapter);
            }
        }
    }

}