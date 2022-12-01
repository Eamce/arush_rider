package com.example.raiderdelivery_v4.ui.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.chatbox.ChatboxUserType;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.login.Login;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import de.hdodenhof.circleimageview.CircleImageView;

//import static com.example.raiderdelivery_v4.ui.global.Globalvars.offline_profile_image_link;
import static com.example.raiderdelivery_v4.ui.global.Globalvars.online_profile_image_link;

public class HomeActivity extends AppCompatActivity {
    Ajax mo;
    Globalvars globalvars;
    ProgressDialog pd1;
    private AppBarConfiguration mAppBarConfiguration;
    TextView tv_fullname;
    NavigationView NavView;
    View headerView;
    CircleImageView iv_image;
    //public static String image_link = "http://172.16.46.130/olstore_serv/assets/images/";
    //public static String image_link = "http://172.16.43.239:8000/";
    //Create these objects above OnCreate()of your main activity
    TextView history, transaction;
    public Handler handler;
    public Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        globalvars = new Globalvars((Context)this,(Activity)this);
        setTitle("Home(" + globalvars.get("category") + ")");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, ChatboxUserType.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_transaction, R.id.nav_repots,R.id.nav_history, R.id.nav_settings, R.id.nav_account, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavView = (NavigationView)findViewById(R.id.nav_view); //Used to call TextView to the other XML file...
        headerView = NavView.getHeaderView(0); //Used to call TextView to the other XML file...
        tv_fullname = (TextView)headerView.findViewById(R.id.tv_fullname);
        tv_fullname.setText(globalvars.get("fname"));
        iv_image = (CircleImageView)headerView.findViewById(R.id.civ_image);

        Picasso.get()
                .load(online_profile_image_link + globalvars.get("image"))
                .resize(500, 500)
                .placeholder(R.drawable.ic_account_circle_300)
                .error(R.drawable.ic_account_circle_300)
                .centerCrop()
                .into(iv_image);

        //These lines should be added in the OnCreate() of your main activity
        transaction = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_transaction));
        history = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().
                findItem(R.id.nav_history));

        //CountTransactionAndHistory();
        //This method will initialize the count value

        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub


                if(!isFinishing())
                {
                    //Logout user...
                    globalvars.logout();

                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setCancelable(false);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);

                    //finally creating the alert dialog and displaying it
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Button btn_ok = dialogView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                            Intent intent = new Intent(HomeActivity.this, Login.class);
                            startActivity(intent);
                        }
                    });
                }

            }
        };
        startHandler();
    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 30*60*1000); //Set Session timeout for 10 minutes...
    }

    public void CountTransactionAndHistory(){
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                //pd1.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }
            @Override
            public void onsuccess(String data)
            {
                JSONArray thedata;
                try
                {
                    thedata = new JSONArray(data);
                    if(thedata.length() > 0)
                    {
                        for (int a = 0; a < thedata.length(); a++)
                        {
                            JSONArray row = thedata.getJSONArray(a);
                            if(a!=0) {
                                globalvars.set("history_count",row.getString(0));
                            }else{
                                globalvars.set("trans_count",row.getString(0));
                            }
                        }
                    }
                    //pd1.dismiss();

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                finally {
                    initializeCountDrawer();
                }
            }
        });

        mo.adddata("r_id_num",globalvars.get("r_id_num"));
        mo.adddata("bunit_code", globalvars.get("bunit_code"));
        mo.execute(Globalvars.online_link + "count_transactions_and_history");
    }

    private void initializeCountDrawer(){
        //Gravity property aligns the text
        transaction.setGravity(Gravity.CENTER_VERTICAL);
        transaction.setTypeface(null, Typeface.BOLD);
        transaction.setTextColor(getResources().getColor(R.color.colorAccent));
        //transaction.setText(globalvars.get("trans_count"));

//        final int sdk = android.os.Build.VERSION.SDK_INT;
//        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            transaction.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.rounded_textview) );
//        } else {
//            transaction.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_textview));
//        }
        //transaction.setBackground(getDrawable(R.drawable.rounded_textview));

        history.setGravity(Gravity.CENTER_VERTICAL);
        history.setTypeface(null,Typeface.BOLD);
        history.setTextColor(getResources().getColor(R.color.colorAccent));
        //count is added
        //history.setText(globalvars.get("history_count"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }

}
