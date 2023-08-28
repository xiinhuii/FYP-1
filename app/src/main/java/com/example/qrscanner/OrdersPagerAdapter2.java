package com.example.qrscanner;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrdersPagerAdapter2 extends FragmentStateAdapter
{


    private boolean sessionStarted = false;


    public OrdersPagerAdapter2(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);



    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch(position)
        {
            case 0:
                return new HomeFragment();
            case 1:
                return new WalletFragment();
            case 2: {
                return new SessionFragment();

            }
            case 3:
                return new TransactionFragment();
            default:
                return new AccountFragment();
        }
    }


    /*
    private void getSessionStatus()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                sessionStarted = documentSnapshot.getBoolean("sessionStarted");
            }
        });
    }*/

    @Override
    public int getItemCount() {
        return 5;
    }


}
