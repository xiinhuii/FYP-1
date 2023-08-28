package com.example.qrscanner;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.health.SystemHealthManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.annotation.NonNullApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import javax.security.auth.callback.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //public static final String SHARED_PREFS2 = "sharedPrefs";
    public static final String SHARED_PREFS = "sharedID";
    FirebaseFirestore firebaseFirestore;
    private String userid;



    //public static final String MYBALANCE = "myBalance";

    private static double balanceValue;

    private static boolean firstRun = true;
    TextView balanceText;

    DatabaseAccount db;

    ItemViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        Bundle balanceData = getArguments();

        if(balanceData != null)
        {
            balanceValue = balanceData.getFloat("balanceData");
        }
        balanceText.setText(String.format("Balance: $%.2f", balanceValue));
        */

        //GET USERID
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        System.out.println("My userid (home): " + userid);


        /*
        SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS2, Context.MODE_PRIVATE);
        balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

        SharedPreferences.Editor editor = sharedPrefBalance.edit();
        editor.putFloat("MYBALANCE", balanceValue);
        editor.commit();
        */


        balanceText = view.findViewById(R.id.balanceText);
        balanceText.setTextColor(Color.GREEN);

        fetchData();
        //loadFromDBToMemory();
        //updateBalance();


        //db = new DatabaseAccount(getActivity());
        System.out.println("Home Balance: " + balanceValue);

        /*
        viewModel = new ViewModelProvider(this.getActivity()).get(ItemViewModel.class);
        viewModel.getBalanceData().observe(this.getActivity(), data -> {
            balanceText.setText(data.toString());
        });*/


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        //SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS2, Context.MODE_PRIVATE);
        //balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

        //viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        //viewModel.setData(balanceValue);

        /*
        balanceText = getView().findViewById(R.id.balanceText);
        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.getBalanceData().observe(this.getActivity(), data -> {
                    balanceText.setText(data.toString());
                });*/
        //loadFromDBToMemory();
        //createBalance();

    }


    private void createBalance()
    {
        balanceText = getView().findViewById(R.id.balanceText);
        balanceText.setTextColor(Color.GREEN);
        updateBalance();
    }

    private void updateBalance()
    {
        //SharedPreferences sharedPrefBalance = this.getActivity().getSharedPreferences(SHARED_PREFS2, Context.MODE_PRIVATE);
        //balanceValue = sharedPrefBalance.getFloat("MYBALANCE", 50);

        balanceText.setText(String.format("Balance: $%.2f", balanceValue));
        System.out.println("Updated!");
    }

    private void loadFromDBToMemory()
    {
        if(firstRun)
        {
            /*
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(getActivity());
            sqLiteManager.populateTransactionListArray();
            sqLiteManager.close();*/
        }
        firstRun = false;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("HOME!!!");
        fetchData();
        updateBalance();
        System.out.println("Home Balance: " + balanceValue);
    }


    //FIRESTORE
    private void fetchData()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                balanceValue = documentSnapshot.getDouble("balance");
                balanceText.setText(String.format("Balance: $%.2f", balanceValue));
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }






}