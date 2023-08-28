package com.example.qrscanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SessionFragment extends Fragment {

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

    private String username;
    TextView usernameText;

    Button btn_endSession;

    private boolean sessionStarted;

    private String currentDate, currentTime, endTime;

    SimpleDateFormat sdfDate;
    SimpleDateFormat sdfTime;

    private void DateTime()
    {
        //Date and Time
        sdfDate = new SimpleDateFormat("dd MMM yy", Locale.getDefault());
        currentDate = sdfDate.format(new Date());

        sdfTime = new SimpleDateFormat("KK:mm aaa", Locale.getDefault());
        currentTime = sdfTime.format(new Date());

    }



    public SessionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SessionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SessionFragment newInstance(String param1, String param2) {
        SessionFragment fragment = new SessionFragment();
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
        View view = inflater.inflate(R.layout.fragment_session, container, false);

        //GET USERID
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        userDocRef = userRef.document(userid);

        usernameText = view.findViewById(R.id.usernameText);

        fetchUserData();




        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        Initiate();
        //DateTime();
    }

    private void Initiate()
    {
        btn_endSession = getView().findViewById(R.id.btn_endSession);
        btn_endSession.setOnClickListener(v->
        {
            endSession();

        });
    }

    private void endSession()
    {

        fetchUserSessionStatus();
        updateSessionStatus();

        Intent refresh = new Intent(getActivity(), MainActivity.class);
        refresh.putExtra("status", false);
        userDocRef.update("sessionStarted", false);
        startActivity(refresh);

    }

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

    //UPDATE
    private void updateSessionStatus()
    {
        if (sessionStarted == false)
            userDocRef.update("sessionStarted", true);
        else
            userDocRef.update("sessionStarted", false);


        System.out.println("Updated session: " + sessionStarted);
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

}