package com.example.qrscanner;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TransactionAdapter2 extends FirestoreRecyclerAdapter<MyTransaction, TransactionAdapter2.TransactionHolder>{


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TransactionAdapter2(@NonNull FirestoreRecyclerOptions<MyTransaction> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TransactionHolder holder, int position, @NonNull MyTransaction model) {
        holder.textDate.setText(model.getDate());
        holder.textTime.setText(model.getTime());
        holder.textDuration.setText(model.getDuration());
        holder.textAmount.setText(Double.toString(model.getTotalAmt()));
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_cell, parent, false);
        return new TransactionHolder(v);
    }

    class TransactionHolder extends RecyclerView.ViewHolder
    {
        TextView textId;
        TextView textDate;
        TextView textTime;
        TextView textDuration;
        TextView textAmount;

        public TransactionHolder(View itemView)
        {
            super(itemView);
            textDate = itemView.findViewById(R.id.cellDate);
            textTime = itemView.findViewById(R.id.cellTime);
            textDuration = itemView.findViewById(R.id.cellDuration);
            textAmount = itemView.findViewById(R.id.cellAmt);


        }

    }
}
