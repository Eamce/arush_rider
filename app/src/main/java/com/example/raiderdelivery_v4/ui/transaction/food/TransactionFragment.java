package com.example.raiderdelivery_v4.ui.transaction.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.transaction.grocery.GCTransactionActivity;

public class TransactionFragment extends Fragment {
    Globalvars globalvars;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transaction, container, false);
        globalvars = new Globalvars((getContext()),(getActivity()));
        String a = globalvars.get("category");
        if(globalvars.get("category").equalsIgnoreCase("Food"))
        {
            Intent intent = new Intent(getActivity(), TransactionActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getActivity(), GCTransactionActivity.class);
            startActivity(intent);
        }
        return root;
    }
}