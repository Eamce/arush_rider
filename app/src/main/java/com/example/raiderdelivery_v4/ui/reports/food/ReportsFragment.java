package com.example.raiderdelivery_v4.ui.reports.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.raiderdelivery_v4.R;

public class ReportsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);
        Intent intent = new Intent(getActivity(), ReportsActivity.class);
        startActivity(intent);
        return root;
    }
}
