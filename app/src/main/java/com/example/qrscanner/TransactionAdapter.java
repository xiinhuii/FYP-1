package com.example.qrscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;


public class TransactionAdapter extends ArrayAdapter<MyTransaction>
{
    public TransactionAdapter (Context context, List<MyTransaction>transactions)
    {
        super(context, 0, transactions);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        MyTransaction transaction = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_cell, parent, false);
        }

        TextView date = convertView.findViewById(R.id.cellDate);
        TextView time = convertView.findViewById(R.id.cellTime);
        TextView duration = convertView.findViewById(R.id.cellDuration);
        TextView amt = convertView.findViewById(R.id.cellAmt);

        date.setText(transaction.getDate());
        time.setText(transaction.getTime());
        duration.setText(transaction.getDuration());
        amt.setText("$" + transaction.getTotalAmt());

        return convertView;

    }
}
