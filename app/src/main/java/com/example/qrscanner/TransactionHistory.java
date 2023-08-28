package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class TransactionHistory extends AppCompatActivity {

    private static boolean firstRun = true;

    private ListView transactionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        Button backButton1 = (Button) findViewById(R.id.btn_back1);
        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        TransactionHistory.this, MainActivity.class);

                startActivity(intent);
            }
        });

        initWidgets();
        //loadFromDBToMemory();

        setTransactionAdapter();

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(
                TransactionHistory.this, MainActivity.class);

        startActivity(intent);
    }
    private void initWidgets() {
        transactionListView = findViewById(R.id.transactionListView);
    }

    private void loadFromDBToMemory()
    {
        if(firstRun)
        {
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.populateTransactionListArray();
            sqLiteManager.close();
        }
        firstRun = false;
    }

    private void setTransactionAdapter()
    {
        //TransactionAdapter transactionAdapter = new TransactionAdapter(getApplicationContext(), Transaction.transactionArrayList);
        //transactionListView.setAdapter(transactionAdapter);
    }


}