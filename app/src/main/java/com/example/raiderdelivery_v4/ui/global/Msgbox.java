package com.example.raiderdelivery_v4.ui.global;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Msgbox {
    AlertDialog.Builder builder;
    Context thecontext;
    public interface MsgboxListener{
        public void onyes();
        public void onno();
    }
    public MsgboxListener listener;
    public void setMsgboxListener(MsgboxListener listener){
        this.listener = listener;
    }
    public Msgbox(Context context){
        thecontext = context;
        builder = new AlertDialog.Builder(thecontext);
    }
    public void show(Object thetitle,Object message){
        builder.setTitle(thetitle.toString());
        builder.setMessage(message.toString());
        builder.show();
    }
    public void showyesno(Object thetitle,Object message){
        builder.setTitle(thetitle.toString());
        builder.setMessage(message.toString());
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onyes();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onno();
            }
        });
        builder.show();
    }
    public void showyes(Object thetitle,Object message){
        builder.setTitle(thetitle.toString());
        builder.setMessage(message.toString());
        builder.setCancelable(false);
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onyes();
            }
        });
        builder.setNegativeButton(".", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onno();
            }
        });
        builder.show();
    }
}