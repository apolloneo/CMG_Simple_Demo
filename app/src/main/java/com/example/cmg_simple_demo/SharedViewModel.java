package com.example.cmg_simple_demo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> value_W1 = new MutableLiveData<>();
    private final MutableLiveData<String> value_W2 = new MutableLiveData<>();

    public void setValue_W1(String s){
        value_W1.setValue(s);
    }
    public LiveData<String> getValue_W1(){
        return value_W1;
    }

    public void setValue_W2(String s){
        value_W2.setValue(s);
    }
    public LiveData<String> getValue_W2(){
        return value_W2;
    }
}
