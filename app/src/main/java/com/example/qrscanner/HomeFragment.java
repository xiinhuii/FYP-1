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

    public static final String SHARED_PREFS = "sharedID";
    FirebaseFirestore firebaseFirestore;
    private String userid;


    private static double balanceValue;
    private String username;
    private String header, description, rates;

    private static boolean firstRun = true;
    TextView balanceText;
    TextView usernameText;
    TextView rate_1, rate_2, rate_3;

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


        //GET USERID
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        System.out.println("My userid (home): " + userid);




        usernameText = view.findViewById(R.id.usernameText);
        rate_1 = view.findViewById(R.id.rate_1);
        rate_2 = view.findViewById(R.id.rate_2);
        rate_3 = view.findViewById(R.id.rate_3);

        //usernameText.setTextColor(Color.GREEN);

        fetchUserData();
        fetchRatesData_1();
        fetchRatesData_2();
        fetchRatesData_3();
        //loadFromDBToMemory();
        //updateBalance();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {


    }
    @Override
    public void onResume()
    {
        super.onResume();
        fetchUserData();
        fetchRatesData_1();
        fetchRatesData_2();
        fetchRatesData_3();

    }


    //FIRESTORE
    private void fetchUserData()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username = documentSnapshot.getString("username");
                usernameText.setText("Hi " + username);
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }

    private void fetchRatesData_1()
    {
        DocumentReference document = firebaseFirestore.collection("rate").document("rate_1");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("rate_type");
                description = documentSnapshot.getString("rate_description");
                rates = documentSnapshot.getString("rate_price");

                rate_1.setText(header + "\n" + rates + "\n" + description);
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }

    private void fetchRatesData_2()
    {
        DocumentReference document = firebaseFirestore.collection("rate").document("rate_2");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("rate_type");
                description = documentSnapshot.getString("rate_description");
                rates = documentSnapshot.getString("rate_price");

                rate_2.setText(header + "\n" + rates + "\n" + description);
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }

    private void fetchRatesData_3()
    {
        DocumentReference document = firebaseFirestore.collection("rate").document("rate_3");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("rate_type");
                description = documentSnapshot.getString("rate_description");
                rates = documentSnapshot.getString("rate_price");

                rate_3.setText(header + "\n" + rates + "\n" + description);
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }






}