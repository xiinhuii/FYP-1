package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter
{
    public OrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity)
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
                return new ScannerFragment();
            case 2:
                return new TransactionFragment();
            default:
                return new AccountFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }


}
