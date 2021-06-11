package com.c323proj9.kmangrum;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
//    Initialize Variables
    private List<Expense> expenseList;
    private Activity context;
    private AppDatabase database;

//    Create constructor
    public MainAdapter(Activity context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Initialize View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//      Initialize expense Data
        Expense expense = expenseList.get(position);
//        Initialize database
        database = AppDatabase.getDBInstance(context);
//        set Text
        holder.tvExpenseName.setText(expense.getName());
        holder.tvExpenseCost.setText(expense.getCost());
        holder.tvExpenseCategory.setText(expense.getCategory());
        holder.tvExpenseDate.setText(expense.getDate());

//        Determine what image needs to be displayed based on the category
        if (holder.tvExpenseCategory.getText().toString().equals("Grocery")) {
            holder.ivCategoryIcon.setImageResource(R.drawable.grocery);
        }else if (holder.tvExpenseCategory.getText().toString().equals("Gas")) {
            holder.ivCategoryIcon.setImageResource(R.drawable.gas);
        }else if (holder.tvExpenseCategory.getText().toString().equals("Shopping")) {
            holder.ivCategoryIcon.setImageResource(R.drawable.shopping);
        }else if (holder.tvExpenseCategory.getText().toString().equals("Miscellaneous")){
            holder.ivCategoryIcon.setImageResource(R.drawable.misc);
        }

//        I made it so that the user must first click on the edit icon, and then clicks the check mark icon to confirm the updates that they made

//        Set an onclick listener for the edit image
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          Allow the edittexts to be edited
                holder.tvExpenseName.setEnabled(true);
                holder.tvExpenseCost.setEnabled(true);
                holder.tvExpenseCategory.setEnabled(true);
                holder.tvExpenseDate.setEnabled(true);

            }
        });

//  Set an onclick listener for the update image
        holder.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Get the expense from the recyclerview and get each of its attributes
                Expense e = expenseList.get(holder.getAdapterPosition());
                String eName = e.getName();
                String eCost = e.getCost();
                String eDate = e.getDate();
                String eCategory = e.getCategory();

//          Update each of the expense attributes in the database
                database.getExpenseDao().updateName(eName, holder.tvExpenseName.getText().toString());
                database.getExpenseDao().updateCost(eName, eCost, holder.tvExpenseCost.getText().toString());
                database.getExpenseDao().updateCategory(eName, eCategory, holder.tvExpenseCategory.getText().toString());
                database.getExpenseDao().updateDate(eName, eDate, holder.tvExpenseDate.getText().toString());
//                Reset the expenseList and re-add all of the expenses in the database so that the most current expenses are shown
                expenseList.clear();
                expenseList.addAll(database.getExpenseDao().getAllExpenses());
                notifyDataSetChanged();

//                Make it so the edittexts cannot be edited again unless edit button is pressed
                holder.tvExpenseName.setEnabled(false);
                holder.tvExpenseCost.setEnabled(false);
                holder.tvExpenseCategory.setEnabled(false);
                holder.tvExpenseDate.setEnabled(false);
            }
        });

//        Set an onclick listener to delete expenses
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Expense e = expenseList.get(holder.getAdapterPosition());
//                Delete item
                database.getExpenseDao().delete(e);
//                Notify when expense is deleted
                int position = holder.getAdapterPosition();
                expenseList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, expenseList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        Initialize variable
        TextView tvExpenseName, tvExpenseCost, tvExpenseCategory, tvExpenseDate;
        ImageView ivCategoryIcon, ivEdit, ivUpdate, ivDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            Assign variables
            tvExpenseName = itemView.findViewById(R.id.tvExpenseName);
            tvExpenseCost = itemView.findViewById(R.id.tvExpenseCost);
            tvExpenseCategory = itemView.findViewById(R.id.tvExpenseCategory);
            tvExpenseDate = itemView.findViewById(R.id.tvExpenseDate);
            ivCategoryIcon = itemView.findViewById(R.id.ivExpense);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivUpdate = itemView.findViewById(R.id.ivUpdate);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }

}
