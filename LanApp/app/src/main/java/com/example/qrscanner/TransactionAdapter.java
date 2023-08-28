package com.example.qrscanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class TransactionAdapter extends ArrayAdapter<Transaction>
{
    public TransactionAdapter (Context context, List<Transaction>transactions)
    {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Transaction transaction = getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_cell, parent, false);
        }

        TextView title = convertView.findViewById(R.id.cellTitle);
        TextView desc = convertView.findViewById(R.id.cellDesc);

        title.setText(transaction.getTitle());
        desc.setText("$" + transaction.getDescription());

        return convertView;

    }
}
