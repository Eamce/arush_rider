package com.example.raiderdelivery_v4.ui.for_confirmation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.raiderdelivery_v4.R;

public class ForConfirmationFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_for_confirmation, container, false);

        Intent intent = new Intent(getActivity(), ForConfirmationActivity.class);
        startActivity(intent);
        return root;
    }

}
