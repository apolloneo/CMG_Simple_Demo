package com.example.cmg_simple_demo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cmg_simple_demo.R;
import com.example.cmg_simple_demo.SharedViewModel;

public class GraphicsFragment extends Fragment {
    //wedgets
    TextView value_W1, value_W2;
    //vars
    SharedViewModel sharedViewModel_graphics_fragement;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_graphic, container, false);
        value_W1 = rootview.findViewById(R.id.textView_graphics_w1);
        value_W2 = rootview.findViewById(R.id.textView_graphics_w2);
        return rootview;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewModel_graphics_fragement = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        setObserve();
    }

    private void setObserve() {
        sharedViewModel_graphics_fragement.getValue_W1().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                value_W1.setText(s);
            }
        });

        sharedViewModel_graphics_fragement.getValue_W2().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                value_W2.setText(s);
            }
        });
    }
}
