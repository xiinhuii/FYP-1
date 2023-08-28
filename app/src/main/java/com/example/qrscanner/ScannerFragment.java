package com.example.qrscanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

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

    public static final String SHARED_PREFS = "sharedID";

    FirebaseFirestore firebaseFirestore;
    private CollectionReference userRef;
    private DocumentReference userDocRef;
    private String userid;


    TextView transactionData;

    private Double numOfTransaction;


    Button btn_scan;

    private Boolean transactionMade = false;

    private static boolean firstRun = true;

    private static double balanceValue;

    private String currentDate, currentTime, endTime;
    private Double amountDeducted;
    
    private static final String TAG = "ScannerFragment";

    //public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String MYBALANCE = "myBalance";


    SimpleDateFormat sdfDate;
    SimpleDateFormat sdfTime;

    private boolean sessionStarted;

    String verifyText = "Click OK to verify";
    String transactionText;




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
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef  = firebaseFirestore.collection("user");

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

        //SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        userDocRef = userRef.document(userid);





        System.out.println("My userid (scanner): " + userid);

        System.out.println("Scanner Balance: " + balanceValue);

        fetchData();
        fetchNumOfTransaction();
        //loadTransactions();

        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null)
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if (documentSnapshot.exists())
                {
                    MyTransaction transaction = documentSnapshot.toObject(MyTransaction.class);

                    int id = transaction.getId();
                    String date = transaction.getDate();
                    String time = transaction.getTime();
                    String duration = transaction.getDuration();
                    Double amt = transaction.getTotalAmt();


                }

            }
        });



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
        sdfDate = new SimpleDateFormat("dd MMM yy", Locale.getDefault());
        currentDate = sdfDate.format(new Date());

        sdfTime = new SimpleDateFormat("KK:mm aaa", Locale.getDefault());
        currentTime = sdfTime.format(new Date());



    }

    private void InitiateScan()
    {


        btn_scan = getView().findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {

            //promptBiometric();
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

        if (transactionMade == false)
        {
            barLauncher.launch(options);
            System.out.println("Scanning....");
            System.out.println("^Transaction made: " + transactionMade.toString());
            transactionMade = true;

        }
        else
        {
            System.out.println("Transaction made: " + transactionMade.toString());
        }


    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Verification");
            //builder.setMessage(result.getContents());
            builder.setMessage(transaction(result.getContents()));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (transactionMade == true)
                    {
                        if (transactionText.equals(verifyText))
                        {
                            updateTransactionNum();
                            addTransaction();
                            System.out.println("Transaction DONE!");

                            promptBiometric();

                            //updateSessionStatus();

                            //Intent refresh = new Intent(getActivity(), MainActivity.class);
                            //refresh.putExtra("status", true);
                            //startActivity(refresh);
                        }
                        transactionMade = false;





                    }
                    dialogInterface.dismiss();


                }
            }).show();

        }
    });

    private String transaction(String context)
    {
        //Check the name of the QRCODE
        if (context.equals("deductFive"))
        {
            if (balanceValue >= 5)
            {
                //SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                //balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

                //Testing
                balanceValue = balanceValue - 5;

                //Testing
                System.out.println("Deduct value: 5");
                transactionMade = true;
                fetchUserSessionStatus();


                //SharedPreferences.Editor editor = sharedPrefBalance.edit();
                //editor.putFloat("MYBALANCE", balanceValue);
                //editor.commit();
                Map<String, Object> user = new HashMap<>();
                user.put("balance", balanceValue);

                userDocRef.update("balance", balanceValue);

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
        transactionText = verifyText;
        return transactionText;
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
        /*
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

        Map<String, Object> transaction = new HashMap<>();
        transaction.put("id", id);
        transaction.put("date", currentDate);
        transaction.put("amount", 20.80);
        */
        //addTransaction();
        //userRef.document(userid).collection("transaction").document().set(transaction);
    }



    //add to transaction history
    public void addTransaction()
    {
        DateTime();
        //int id = MyTransaction.transactionArrayList.size();
        int id = numOfTransaction.intValue();
        String date = currentDate;
        String duration = "1 hour 30mins";
        calculateEndTime(90); //to be changed
        amountDeducted = 10.00; //to be changed

        amountDeducted = (double) Math.round(amountDeducted * 100) / 100;
        //forumla
        //amountDeducted = durationValue * rates;


        String time = (currentTime + " - " + endTime);

        System.out.println("No. of documents: " + id);

        MyTransaction transaction = new MyTransaction(id, date, time, duration, amountDeducted);
        MyTransaction.transactionArrayList.add(transaction);
        userRef.document(userid).collection("transaction").document(String.valueOf(id)).set(transaction);
    }

    private void calculateEndTime(int minute)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        c.add(Calendar.MINUTE, minute);
        endTime = sdfTime.format(c.getTime());
    }

    private void updateTransactionNum()
    {
        numOfTransaction++;
        userDocRef.update("numOfTransaction", numOfTransaction);
    }
    private void fetchNumOfTransaction()
    {
        DocumentReference document = userRef.document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                numOfTransaction = documentSnapshot.getDouble("numOfTransaction");

            }
        });
    }
    public void loadTransaction()
    {
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            MyTransaction transaction = documentSnapshot.toObject(MyTransaction.class);

                            int id = transaction.getId();
                            String date = transaction.getDate();
                            String time = transaction.getTime();
                            String duration = transaction.getDuration();
                            Double amt = transaction.getTotalAmt();

                            data += (id + "\n" + date + "\n" + time + "\n" + duration + "\n" + amt);
                        }

                        transactionData.setText(data);


                    }
                });
    }

    private void fetchData()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                balanceValue = documentSnapshot.getDouble("balance");
                //balanceText.setText(String.format("Available Balance\n$%.2f", balanceValue));
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }

    public void loadTransactions()
    {
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            Transaction transaction = documentSnapshot.toObject(Transaction.class);

                            String DateTime = currentDate + " " + currentTime;
                            String balance = String.format("%.2f", balanceValue);
                            int id = Transaction.transactionArrayList.size();

                            //int id = transaction.getId();
                            //String title = transaction.getTitle();
                            //String description = transaction.getDescription();
                            transaction = new Transaction(id , DateTime, balance);
                            Transaction.transactionArrayList.add(transaction);

                        }
                    }
                });
    }

    private void fetchUserSessionStatus()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sessionStarted = documentSnapshot.getBoolean("sessionStarted");

            }
        });
    }

    //UPDATE
    private void updateSessionStatus()
    {
        if (sessionStarted == false)
            userDocRef.update("sessionStarted", true);
        else
            userDocRef.update("sessionStarted", false);
    }


    private void promptBiometric()
    {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Please Verify")
                .setDescription("User Authentication is required to proceed")
                .setNegativeButtonText("Cancel")
                .build();

        getPrompt().authenticate(promptInfo);
    }


    private BiometricPrompt getPrompt()
    {
        Executor executor = ContextCompat.getMainExecutor(this.getActivity());
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString)
            {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result)
            {
                super.onAuthenticationSucceeded(result);
                notifyUser("Authentication Succeeded!");
                //Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                //startActivity(intent);

                startSession();


            }

            @Override
            public void onAuthenticationFailed()
            {
                super.onAuthenticationFailed();
                notifyUser("Authentication Failed!");
            }
        };

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, callback);
        return biometricPrompt;
    }

    private void notifyUser(String message)
    {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void startSession()
    {
        updateSessionStatus();

        Intent refresh = new Intent(getActivity(), MainActivity.class);
        refresh.putExtra("status", true);
        startActivity(refresh);
    }




}