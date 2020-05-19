package com.example.kelompok1.ui.akun;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AkunViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AkunViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is Akun Fragment");
    }

    public LiveData<String> getText(){
        return mText;
    }
}
