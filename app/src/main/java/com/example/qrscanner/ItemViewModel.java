package com.example.qrscanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel
{
    private final MutableLiveData<Float> balanceData = new MutableLiveData<Float>();

    public void setData(Float data)
    {
        balanceData.setValue(data);
    }

    public LiveData<Float> getBalanceData()
    {
        return balanceData;
    }
}
