package com.example.raiderdelivery_v4.ui.history.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.history.grocery.GCHistoryActivity;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionActivity;
import com.example.raiderdelivery_v4.ui.transaction.grocery.GCTransactionActivity;

public class HistoryFragment extends Fragment {
    Globalvars globalvars;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        globalvars = new Globalvars((getContext()),(getActivity()));
        String a = globalvars.get("category");
        if(globalvars.get("category").equalsIgnoreCase("Food"))
        {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getActivity(), GCHistoryActivity.class);
            startActivity(intent);
        }
        return root;
    }
}