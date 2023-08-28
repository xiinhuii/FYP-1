package com.example.qrscanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardsHolder> {

    private List<Rewards> rewardsList;
    private RewardsListener rewardsListener;


    public RewardsAdapter(List<Rewards> rewardsList, RewardsListener rewardsListener) {
        this.rewardsList = rewardsList;
        this.rewardsListener = rewardsListener;
    }

    @NonNull
    @Override
    public RewardsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new RewardsHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.reward_cell,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RewardsHolder holder, int position) {
        holder.bindRewards(rewardsList.get(position));
    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }

    public List<Rewards> getSelectedRewards()
    {
        List<Rewards> selectedRewards = new ArrayList<>();
        for (Rewards rewards : rewardsList)
        {
            if (rewards.isSelected)
            {
                selectedRewards.add(rewards);
            }
        }

        return selectedRewards;
    }

    class RewardsHolder extends RecyclerView.ViewHolder
    {



        ConstraintLayout rewardLayout;
        View viewBackground;
        TextView title, description, quantity, points;

        ImageView rewardSelected;



        public RewardsHolder(@NonNull View itemView) {
            super(itemView);
            rewardLayout = itemView.findViewById(R.id.layoutRewards);
            viewBackground = itemView.findViewById(R.id.viewBackground);
            title = itemView.findViewById(R.id.rewardName);
            description = itemView.findViewById(R.id.rewardDescription);
            quantity = itemView.findViewById(R.id.rewardQuantity);
            points = itemView.findViewById(R.id.rewardPoints);
            //rewardSelected = itemView.findViewById(R.id.rewardSelected);

        }


        void bindRewards(final Rewards rewards)
        {
            title.setText(rewards.title);
            description.setText(rewards.description);
            quantity.setText(rewards.quantity);
            points.setText(rewards.points);


            if (rewards.isSelected)
            {
                viewBackground.setBackgroundResource(R.drawable.selected_background);
                //rewardSelected.setVisibility(View.VISIBLE);
                rewards.isSelected = true;



            }
            else
            {
                viewBackground.setBackgroundResource(R.drawable.unselected_background);
                //rewardSelected.setVisibility(View.GONE);
                rewards.isSelected = false;


            }

            rewardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rewards.isSelected)
                    {

                        viewBackground.setBackgroundResource(R.drawable.unselected_background);
                        //rewardSelected.setVisibility(View.GONE);
                        rewards.isSelected = false;



                        if (getSelectedRewards().size() == 0)
                        {
                            rewardsListener.onRewardAction(false);
                        }

                    }
                    else
                    {
                        if (getSelectedRewards().size() < 1) {
                            viewBackground.setBackgroundResource(R.drawable.selected_background);
                            //rewardSelected.setVisibility(View.VISIBLE);
                            rewards.isSelected = true;
                            rewardsListener.onRewardAction(true);
                        }
                    }
                }
            });


        }
    }
}
