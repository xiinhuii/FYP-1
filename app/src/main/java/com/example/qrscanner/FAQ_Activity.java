package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FAQ_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<FAQs> faqsList;

    FirebaseFirestore firebaseFirestore;

    private String header, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        recyclerView = findViewById(R.id.recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();


        initData();
        setRecyclerView();



    }

    private void setRecyclerView()
    {
        FAQAdapter faqAdapter = new FAQAdapter(faqsList);
        recyclerView.setAdapter(faqAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void initData()
    {
        faqsList = new ArrayList<>();
        FAQs test = new FAQs("FAQ", "Frequently Asked Questions");
        faqsList.add(test);

        fetchFAQData_1();
        fetchFAQData_2();
        fetchFAQData_3();
        fetchFAQData_4();
        fetchFAQData_5();
        fetchFAQData_6();


        System.out.println("Size: " + faqsList.size());

    }

    //FIRESTORE
    private void fetchFAQData_1()
    {
        DocumentReference document = firebaseFirestore.collection("FAQ").document("faq_1");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("header");
                description = documentSnapshot.getString("description");

                faqsList.add(new FAQs(header, description));
            }

        });
    }

    private void fetchFAQData_2()
    {
        DocumentReference document = firebaseFirestore.collection("FAQ").document("faq_2");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("header");
                description = documentSnapshot.getString("description");

                faqsList.add(new FAQs(header, description));

            }
        });
    }

    private void fetchFAQData_3()
    {
        DocumentReference document = firebaseFirestore.collection("FAQ").document("faq_3");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("header");
                description = documentSnapshot.getString("description");

                faqsList.add(new FAQs(header, description));

            }
        });
    }

    private void fetchFAQData_4()
    {
        DocumentReference document = firebaseFirestore.collection("FAQ").document("faq_4");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("header");
                description = documentSnapshot.getString("description");

                faqsList.add(new FAQs(header, description));

            }
        });
    }

    private void fetchFAQData_5()
    {
        DocumentReference document = firebaseFirestore.collection("FAQ").document("faq_5");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("header");
                description = documentSnapshot.getString("description");

                faqsList.add(new FAQs(header, description));

            }
        });
    }

    private void fetchFAQData_6()
    {
        DocumentReference document = firebaseFirestore.collection("FAQ").document("faq_6");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                header = documentSnapshot.getString("header");
                description = documentSnapshot.getString("description");

                faqsList.add(new FAQs(header, description));

            }
        });
    }
}