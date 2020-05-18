package com.example.kelompok1.ui.promosi;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PromosiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PromosiViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is promosi fragment");
    }

    public LiveData<String> getText(){
        return mText;
    }
}
