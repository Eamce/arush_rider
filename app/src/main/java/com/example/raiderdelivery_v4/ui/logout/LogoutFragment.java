package com.example.raiderdelivery_v4.ui.logout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.raiderdelivery_v4.ui.account.ForgotPassword;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.home.HomeActivity;
import com.example.raiderdelivery_v4.ui.login.Login;
import com.example.raiderdelivery_v4.R;

import org.json.JSONArray;
import org.json.JSONException;

public class LogoutFragment extends Fragment {
    Globalvars globalvars;
    Ajax mo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        globalvars = new Globalvars((Context)getContext(),(Activity)getActivity());

        confirmation();
        return root;
    }

    public void confirmation(){
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to logout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        update_online_status();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            }
                        }).show();
    }

    public void update_online_status()
    {
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                Toast toast = Toast.makeText(getContext(),"Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }
            @Override
            public void onsuccess(String data)
            {
                globalvars.logout();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });

        mo.adddata("rider_id", globalvars.get("id"));
        mo.execute(Globalvars.online_link + "update_online_status_to_offline");
    }
}