package com.golobon.g_chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private final static int SIGN_IN_CODE = 1;
    private RelativeLayout activityMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityMain = findViewById(R.id.text_input_layout);
        //Если пользователь не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            this.startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_CODE);
        } else {
            Snackbar.make(activityMain, "Вы авторизованы", Snackbar.LENGTH_LONG).show();
        }

    }
}