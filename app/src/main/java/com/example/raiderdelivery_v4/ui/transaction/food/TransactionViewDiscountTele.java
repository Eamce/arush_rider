package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;

import java.io.ByteArrayOutputStream;

public class TransactionViewDiscountTele extends AppCompatActivity {
    ImageView iv_image;
    Button btn_save, btn_camera;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Ajax mo;
    ProgressDialog pd;
    String ticket, discount_id, discount_type;
    Bitmap bitmap1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_view_discount_tele);

        iv_image = findViewById(R.id.iv_image);
        btn_camera = findViewById(R.id.btn_camera);
        btn_save = findViewById(R.id.btn_save);
        Intent intent = getIntent();
        ticket = intent.getExtras().getString("ticket_id");
        discount_id = intent.getExtras().getString("ID#");
        discount_type = intent.getExtras().getString("Discount_type");

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_image.getDrawable() != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Bitmap bitmap = ((BitmapDrawable) iv_image.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    SaveImage(imageString);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Please take photo first before you upload.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                    toast.show();
                }
            }
        });
    }

    public void SaveImage(String imageString){
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {

                pd.dismiss();

                //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(TransactionViewDiscountTele.this).inflate(R.layout.my_dialog_success, viewGroup, false);
                TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_success_message);
                tv_dialog_message.setText("Image has been successfully uploaded.");
                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionViewDiscountTele.this);
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
//                        Intent i = new Intent(TransactionViewDiscountTele.this, TransactionViewDiscountTele2.class);
//                        i.putExtra("ticket_id", ticket);
//                        startActivity(i);
                        finish();
                        alertDialog.cancel();
                    }
                });
            }
        });
        String discount_id1 = discount_id;
        String discount_id2 = discount_id;
        mo.adddata("ticket", ticket);
        mo.adddata("discount_id", discount_id);
        mo.adddata("discount_type", discount_type);
        mo.adddata("imageString", imageString);
        mo.execute(Globalvars.online_link + "save_image");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = (Bitmap) extras.get("data");
//            Picasso.get()
//                    .load(imageBitmap.toString())
//                    //.load(R.drawable.ic_broken_image_gray_24dp)
//                    .placeholder(R.drawable.ic_broken_image_gray_24dp)
//                    .error(R.drawable.ic_broken_image_gray_24dp)
//                    .resize(500, 500)
//                    .centerCrop()
//                    .into(iv_image);

            iv_image.setImageBitmap(imageBitmap);
        }
    }
}
