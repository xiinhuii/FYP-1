package com.example.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

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
    TextView usernameText;
    TextView pointsText;
    private String username;
    private Double points;

    private boolean sessionStarted;

    Button detailsBtn, rewardsBtn, faqBtn, logoutBtn;


    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        //GET USERID
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        System.out.println("My userid (account): " + userid);

        usernameText = view.findViewById(R.id.usernameText);
        pointsText = view.findViewById(R.id.points);

        detailsBtn = view.findViewById(R.id.btn_personal_details);

        rewardsBtn = view.findViewById(R.id.btn_rewards);

        faqBtn = view.findViewById(R.id.btn_FAQ);

        logoutBtn = view.findViewById(R.id.btn_logout);

        initiateButtons();
        fetchUserData();
        fetchPointsData();





        return view;
    }

    public void initiateButtons()
    {


        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), PersonalDetailsActivity.class);
                startActivity(intent);
            }
        });

        rewardsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), RewardsActivity.class);
                startActivity(intent);
            }
        });


        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), FAQ_Activity.class);
                startActivity(intent);
            }
        });


        logoutBtn.setOnClickListener(v->
        {
            fetchUserSessionStatusAndLogout();

            System.out.println("Logout~");
        });
    }


    @Override
    public void onResume()
    {
        super.onResume();
        fetchUserData();
        fetchPointsData();



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

            }
        });
    }

    private void fetchPointsData()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                points = documentSnapshot.getDouble("points");
                pointsText.setText(String.format("%.0f points", points));

            }
        });
    }


    private void fetchUserSessionStatusAndLogout()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sessionStarted = Boolean.TRUE.equals(documentSnapshot.getBoolean("sessionStarted"));
                System.out.println("Current session status (account): " + sessionStarted);

                if (sessionStarted == false)
                {
                    //logoutBtn.setClickable(true);
                    Toast.makeText(getActivity(), "Logging out..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //logoutBtn.setClickable(false);
                    Toast.makeText(getActivity(), "Please end your session before logging out.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}