package com.example.qrscanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScannerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn_scan;

    private Boolean transactionMade = false;

    private static boolean firstRun = true;

    private static float balanceValue;

    private String currentDate, currentTime;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String MYBALANCE = "myBalance";

    public ScannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScannerFragment newInstance(String param1, String param2) {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_scanner, container, false);

        SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

        System.out.println("Scanner Balance: " + balanceValue);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        InitiateScan();
        DateTime();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("Scanner Balance: " + balanceValue);
    }

    private void DateTime()
    {
        //Date and Time
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDate = sdfDate.format(new Date());

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        currentTime = sdfTime.format(new Date());
    }

    private void InitiateScan()
    {


        btn_scan = getView().findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Result");
            //builder.setMessage(result.getContents());
            builder.setMessage(transaction(result.getContents()));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (transactionMade == true)
                    {
                        newTransaction();
                        transactionMade = false;

                    }
                    dialogInterface.dismiss();

                }
            }).show();

        }
    });

    private String transaction(String context)
    {
        if (context.equals("deductFive"))
        {
            if (balanceValue >= 5)
            {
                SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

                balanceValue = balanceValue - 5;
                transactionMade = true;

                SharedPreferences.Editor editor = sharedPrefBalance.edit();
                editor.putFloat("MYBALANCE", balanceValue);
                editor.commit();


            }
            else
            {
                return "Insufficient balance";
            }

        }
        else
        {
            return "Invalid QR Code";
        }

        updateBalance();

        return "Balance deducted";
    }

    private void updateBalance()
    {

        //HomeFragment homeFragment = (HomeFragment)getFragmentManager().findFragmentById(R.id.balanceText);
        //homeFragment.balanceText.setText(String.format("Balance: $%.2f", balanceValue));

        /*
        Fragment updateBalance = new Fragment();
        Bundle args = new Bundle();
        args.putFloat("UpdateBalance", balanceValue);
        updateBalance.setArguments(args);

        getParentFragmentManager().beginTransaction().add(R.id.balanceText, updateBalance).commit();

         */
        //getParentFragmentManager().setFragmentResult("key", args);
        //balanceText.setText(String.format("Balance: $%.2f", balanceValue));
    }

    public void newTransaction()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getActivity());
        //Intent newTransactionIntent = new Intent(this, TransactionDetailActivity.class);
        //startActivity(newTransactionIntent);
        String DateTime = currentDate + " " + currentTime;
        String balance = String.format("%.2f", balanceValue);
        int id = Transaction.transactionArrayList.size();
        Transaction newTransaction = new Transaction(id , DateTime, balance);
        Transaction.transactionArrayList.add(newTransaction);

        sqLiteManager.addTransactionToDatabase(newTransaction);
        //finish();
    }




}