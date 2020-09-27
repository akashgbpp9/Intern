package com.example.intern;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    EditText fullname,email,address,number,buisness;
    Button save;
    String strfullname,stremail,straddress,strnumber,strbuisness;
    Snackbar snackbar;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(android.R.id.content);
        fullname=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        number=findViewById(R.id.phonenumber);
        buisness=findViewById(R.id.buisness);
        save=findViewById(R.id.button2);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strfullname=fullname.getText().toString().trim();

                stremail=email.getText().toString().trim();
                straddress=address.getText().toString().trim();
                strnumber=number.getText().toString().trim();
                Log.i("number",strnumber);
                strbuisness=buisness.getText().toString().trim();
                final boolean b = strfullname.isEmpty() || stremail.isEmpty() || straddress.isEmpty()|| strbuisness.isEmpty()|| strnumber.isEmpty();
                if (b) {
                    snackbar = Snackbar.make(view, "All fields are required", Snackbar.LENGTH_LONG);
                    setSnackbar();
                }  else if (!b) {
                    Intent i = new Intent(MainActivity.this,pdfActivity.class);
                    i.putExtra("fullname",strfullname);
                    i.putExtra("email",stremail);
                    i.putExtra("number",strnumber);
                    i.putExtra("address",straddress);
                    i.putExtra("buisness",strbuisness);
                    startActivity(i);
                }

            }
        });


    }
    private void setSnackbar(){
        View view = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        snackbar.getView().setBackgroundColor(Color.parseColor("#000000"));
        snackbar.show();
    }


}
