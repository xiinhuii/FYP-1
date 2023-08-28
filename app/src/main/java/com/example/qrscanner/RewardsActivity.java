package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class RewardsActivity extends AppCompatActivity implements RewardsListener {

    public static final String SHARED_PREFS = "sharedID";

    FirebaseFirestore firebaseFirestore;
    private CollectionReference userRef;
    private DocumentReference userDocRef;
    private String userid;
    private double userPoints;

    private Button redeemBtn;
    List<Rewards> rewardsList = new ArrayList<>();

    String name, description;
    Double rewardPoints, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rewards);

        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef  = firebaseFirestore.collection("user");


        //GET USERID
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userid = sharedPref.getString("USERID", "");
        userDocRef = userRef.document(userid);


        RecyclerView recyclerView = findViewById(R.id.rewardsRecyclerView);
        redeemBtn = findViewById(R.id.redeemBtn);

        fetchUserData();
        fetchRewardData_1();
        updatePoints();


        Rewards rewards1 = new Rewards();
        rewards1.title = "This is the reward";
        rewards1.description = "DESCRIPTION HERE";
        rewards1.points = "3054";
        rewards1.quantity = "433";
 /*
        Rewards rewards2 = new Rewards();
        rewards2.title = "This is the adadsareward";
        rewards2.description = "DESCRIPTION HEwrwrrwrewRE";
        rewards2.points = "30";
        rewards2.quantity = "433";

        Rewards rewards3 = new Rewards();
        rewards3.title = "This is the adadssdsdgsfgsdfareward";
        rewards3.description = "DESCRIPTION HEwrwrrfsfwrewRE";
        rewards3.points = "1130";
        rewards3.quantity = "433234";


        rewardsList.add(rewards2);
        rewardsList.add(rewards3);
        */
        rewardsList.add(rewards1);


        final RewardsAdapter rewardsAdapter = new RewardsAdapter(rewardsList, this);

        recyclerView.setAdapter(rewardsAdapter);

        redeemBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v)
            {
                System.out.println(rewardsList);
                List<Rewards> selectedRewards = rewardsAdapter.getSelectedRewards();

                int points = Integer.parseInt(selectedRewards.get(0).getPoints());
                System.out.println("Selected Points: " + points);

                if (userPoints >= points)
                {
                    userPoints -= points;
                    System.out.println("Updated points (USERPOINTS): " + userPoints);
                    updatePoints();

                    Toast.makeText(RewardsActivity.this, "Successfully redeemed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RewardsActivity.this, "Insufficient points", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    private void updatePoints()
    {
        userDocRef.update("points", userPoints);
    }


    @Override
    public void onRewardAction(Boolean isSelected) {

        if (isSelected)
        {
            redeemBtn.setVisibility(View.VISIBLE);
        }
        else
        {
            redeemBtn.setVisibility(View.GONE);
        }
    }

    //FIRESTORE
    private void fetchUserData()
    {
        DocumentReference document = firebaseFirestore.collection("user").document(userid);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userPoints = documentSnapshot.getDouble("points");
                System.out.println("Updated points (Fetch points): " + userPoints);
                //balanceText.setText(documentSnapshot.getString("balance"));
            }
        });
    }


    private void fetchRewardData_1()
    {
        DocumentReference document = firebaseFirestore.collection("reward").document("reward_1");
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("reward_name");
                description = documentSnapshot.getString("reward_description");
                rewardPoints = documentSnapshot.getDouble("reward_point");
                quantity = documentSnapshot.getDouble("reward_quantity");

                String points = rewardPoints.toString();
                String qty = quantity.toString();

                Rewards rewards = new Rewards();

                rewards.title = name;
                rewards.description = description;
                rewards.points = points;
                rewards.quantity = qty;

                rewardsList.add(rewards);

            }
        });
    }



}