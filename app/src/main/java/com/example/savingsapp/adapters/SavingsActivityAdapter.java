package com.example.savingsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.savingsapp.R;
import com.example.savingsapp.data.SavingsData;
import com.example.savingsapp.db.entities.TransactionHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SavingsActivityAdapter extends RecyclerView.Adapter<SavingsActivityAdapter.SavingsViewHolder> {
   // List of savings data
    ArrayList<TransactionHistory> transactionHistoryList;
    // Context
   Context context;

    /**
     * Constructor
     * @param transactionHistoryList List of savings data
     */
   public SavingsActivityAdapter(ArrayList<TransactionHistory> transactionHistoryList){
       this.transactionHistoryList = transactionHistoryList;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @Override
    public SavingsActivityAdapter.SavingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_layout, parent, false);
        context = parent.getContext();
        return new SavingsViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(SavingsActivityAdapter.SavingsViewHolder holder, int position) {
        TransactionHistory transactionHistory = transactionHistoryList.get(position);
         holder.description.setText(transactionHistory.description);
         holder.amount.setText(context.getString(R.string.amount, transactionHistory.amount));
         holder.dateTextView.setText(context.getString(R.string.transaction_date, transactionHistory.date));
            if(TransactionHistory.TYPE_DEPOSIT.equals(transactionHistory.type) || TransactionHistory.TYPE_INTEREST.equals(transactionHistory.type)){
                holder.dotImage.setImageResource(R.drawable.circle_green);
                holder.description.setTextColor(ContextCompat.getColor(context,R.color.black));
                holder.amount.setTextColor(ContextCompat.getColor(context,R.color.black));
            } else {
                holder.dotImage.setImageResource(R.drawable.circle_red);
                holder.description.setTextColor(ContextCompat.getColor(context,R.color.red));
                holder.amount.setTextColor(ContextCompat.getColor(context,R.color.red));
            }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     * @return
     */
    @Override
    public int getItemCount() {
        return transactionHistoryList.size();
    }

    /**
     * ViewHolder class
     */
    public static class SavingsViewHolder extends RecyclerView.ViewHolder {
       ImageView dotImage;
       TextView description;
       TextView amount;
       TextView dateTextView;
        /**
         * Constructor
         * @param itemView The view that will be used to display the data at the specified position.
         */
        public SavingsViewHolder(View itemView) {
            super(itemView);
            // Initialize views
            dotImage = itemView.findViewById(R.id.dot_image);
            description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            dateTextView = itemView.findViewById(R.id.date_text);
        }
    }
}
