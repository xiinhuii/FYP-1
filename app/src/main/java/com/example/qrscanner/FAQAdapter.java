package com.example.qrscanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQVH> {


    List<FAQs> FAQList;

    public FAQAdapter(List<FAQs> FAQList) {
        this.FAQList = FAQList;
    }

    @NonNull
    @Override
    public FAQVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq, parent, false);
        return new FAQVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQVH holder, int position) {

        FAQs faqs = FAQList.get(position);
        holder.FAQ_Title.setText(faqs.getFAQ_Title());
        holder.description.setText(faqs.getDescription());

        boolean isExpanable = FAQList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpanable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return FAQList.size();
    }

    public class FAQVH extends RecyclerView.ViewHolder {


        TextView FAQ_Title, description;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;

        public FAQVH(@NonNull View itemView) {
            super(itemView);

            FAQ_Title = itemView.findViewById(R.id.FAQ_name);
            description = itemView.findViewById(R.id.description);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);



            linearLayout.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    FAQs faqs = FAQList.get(getAdapterPosition());
                    faqs.setExpandable(!faqs.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
