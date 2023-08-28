package com.example.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrscanner.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    //BiometricPrompt biometricPrompt;
    //androidx.biometric.BiometricPrompt.PromptInfo promptInfo;
    //BiometricPrompt.PromptInfo promptInfo;
    //ConstraintLayout mMainLayout;

    ActivityMainBinding binding;
    String email, username;
    float balance;
    FirebaseDatabase fdb;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    //public static final String SHARED_PREFS = "sharedPrefs";
    //public static final String MYBALANCE = "myBalance";


    //Button btn_scan;
    TextView balanceText;

    private static float balanceValue;

    private static float testBalance;
    //ListView
    private ListView transactionListView;

    //Date and Time
    private String currentDate, currentTime;

    //Transaction
    private Boolean transactionMade = false;

    private static boolean firstRun = true;

    private ItemViewModel viewModel;

    public static final String SHARED_PREFS = "sharedID";
    public static final String SHARED_PREFS_STATUS = "sharedStatus";
    private String userid;

    private boolean sessionStarted;

    private String hasSession = "0";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //==========================================================================

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //==========================================================================
        /*
        testBalance = 30f;
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.setData(testBalance);
        System.out.println("MainActivity TestBalance: " + testBalance);*/
        //==========================================================================
        /*
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle balanceData = new Bundle();
        balanceData.putFloat("balanceData", balanceValue);
        homeFragment.setArguments(balanceData);
        fragmentTransaction.replace(R.id.homeFragment, homeFragment).commit();
        */
        /*
        balanceText = findViewById(R.id.balanceText2);
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getBalanceData().observe(this, data -> {
                balanceText.setText(data.toString());
        });*/

        //==========================================================================
        //sessionStarted = false;

        //SharedPreferences sharedPrefBalance = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

        SharedPreferences sharedPref_status = getSharedPreferences(SHARED_PREFS_STATUS, Context.MODE_PRIVATE);
        sessionStarted = sharedPref_status.getBoolean("SHARED_STATUS", false);
        System.out.println("My status: " + sessionStarted);


        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        System.out.println("My userid (Main): " + userid);




        Bundle status = getIntent().getExtras();

        if (status != null)
        {
            sessionStarted = status.getBoolean("status");
            System.out.println("Intented Status: " + sessionStarted);
        }
        fetchUserSessionStatus();
        System.out.println("Session status (Main): " + sessionStarted);
        //==========================================================================
        //Pager
        if (sessionStarted == false)
            createPager();
        else
            createPager2();

        //==========================================================================
        //retrieveCurrentRegistrationToken();
        /*
        Button btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                sendMessage("HELLLLOOO", "HOW ARE youuu? :D");
            }
        });*/
                //==========================================================================
        //SHARED PREFERENCES




        //==========================================================================
        //loadFromDBToMemory();
        //==========================================================================
        //Date and Time
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        currentDate = sdfDate.format(new Date());

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        currentTime = sdfTime.format(new Date());

        //==========================================================================
        //Button btn_transactionHistory = findViewById(R.id.btn_transactionHistory);

        /*
        btn_transactionHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (transactionMade == true)
                {
                    newTransaction();
                    transactionMade = false;

                }


                Intent intent = new Intent(MainActivity.this, TransactionHistory.class);
                startActivity(intent);
            }
        });
        */
        //==========================================================================
        /*
        balanceText = findViewById(R.id.balanceText);
        balanceText.setTextColor(Color.GREEN);
        updateBalance();*/
        //==========================================================================
        /*
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v->
        {
            scanCode();
        });
        */
        //Biometric
        Button btn_authenticate = findViewById(R.id.btn_authenticate);
        btn_authenticate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Please Verify")
                        .setDescription("User Authentication is required to proceed")
                        .setNegativeButtonText("Cancel")
                        .build();

                getPrompt().authenticate(promptInfo);
            }
        });

    }
    @Override
    protected void onStart() {

        super.onStart();

        fetchUserSessionStatus();
        System.out.println("Session status (onStart): " + sessionStarted);



    }

    private BiometricPrompt getPrompt()
    {
        Executor executor = ContextCompat.getMainExecutor(this);
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
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("My userid (Main Resume): " + userid);
        fetchUserSessionStatus();
        System.out.println("Session status (Resume): " + sessionStarted);
    }

    /*
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLauncher.launch(options);

    }*/
    /*
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result->
    {
        if(result.getContents() != null)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                SharedPreferences sharedPrefBalance = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
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
        balanceText.setText(String.format("Balance: $%.2f", balanceValue));
    }

    //TransactionListView
    private void initWidgets()
    {
        transactionListView = findViewById(R.id.transactionListView);

    }


    //New transaction and save the transaction to db
    public void newTransaction()
    {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
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
*/
    private void loadFromDBToMemory()
    {
        if(firstRun)
        {
            /*
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            sqLiteManager.populateTransactionListArray();
            sqLiteManager.close();*/
        }
        firstRun = false;
    }

    private void sendMessage(String myTitle, String myMessage)
    {
        String title = myTitle;
        String message = myMessage;

        if(!title.equals("") && !message.equals(""))
        {
            MessageSend.pushNotification(
                    MainActivity.this,
                    "fnfAn33EQQmdv52G9nuDzH:APA91bESwffa8uw8u7Pkue_7KHK0EijbXkyCpbisgt1kvJERy2uGdlfTX3msU8hbmoEp0lULFm5gcKOtyNc0eybkNkiVawtGv5XwHFNZ-pUJSRxZ2JbAkGCy2haEN9qfeF81KLBHAeWh",
                    title,
                    message

            );
        }
    }

    //Firebase
    private void retrieveCurrentRegistrationToken()
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        System.out.println("TOKEN " + token);
                    }
                });
    }



    //Pager 1
    private void createPager()
    {
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new OrdersPagerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0: {
                        tab.setText("Home");
                        tab.setIcon(R.drawable.ic_home);
                        break;
                    }
                    case 1: {
                        tab.setText("Wallet");
                        tab.setIcon(R.drawable.ic_wallet);
                        break;
                    }
                    case 2: {
                        tab.setText("Scan");
                        tab.setIcon(R.drawable.ic_scanner);
                        break;
                    }
                    case 3: {
                        tab.setText("History");
                        tab.setIcon(R.drawable.ic_transaction);
                        break;
                    }
                    case 4: {
                        tab.setText("Account");
                        tab.setIcon(R.drawable.ic_account);
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();
    }

    //Pager 2
    private void createPager2()
    {
        ViewPager2 viewPager2 = findViewById(R.id.viewPager);
        viewPager2.setAdapter(new OrdersPagerAdapter2(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position)
                {
                    case 0: {
                        tab.setText("Home");
                        tab.setIcon(R.drawable.ic_home);
                        break;
                    }
                    case 1: {
                        tab.setText("Wallet");
                        tab.setIcon(R.drawable.ic_wallet);
                        break;
                    }
                    case 2: {
                        tab.setText("Current Session");
                        tab.setIcon(R.drawable.ic_current_session);
                        break;
                    }
                    case 3: {
                        tab.setText("History");
                        tab.setIcon(R.drawable.ic_transaction);
                        break;
                    }
                    case 4: {
                        tab.setText("Account");
                        tab.setIcon(R.drawable.ic_account);
                        break;
                    }
                }
            }
        }
        );
        tabLayoutMediator.attach();
    }
    private void fetchUserSessionStatus()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sessionStarted = Boolean.TRUE.equals(documentSnapshot.getBoolean("sessionStarted"));

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        //nth
    }
    


}






