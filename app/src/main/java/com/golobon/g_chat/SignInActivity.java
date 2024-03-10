package com.golobon.g_chat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        activityResultLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(), new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {

                        if (result.getResultCode() == RESULT_OK) {
                        Log.d("logTag", "Sign in successful!");
                        goToMainActivity();
                    }
                    else {
                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error signing in",
                                Toast.LENGTH_LONG).show();

                        IdpResponse response = result.getIdpResponse();

                        if (response == null) {
                            Log.w("logTag", "Sign in canceled");
                        } else {
                            Log.w("logTag", "Sign in error", response.getError());
                        }
                    }
                }});
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        // If there is no signed in user, launch FirebaseUI
        // Otherwise head to MainActivity
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Sign in with FirebaseUI, see docs for more details:
            // https://firebase.google.com/docs/auth/android/firebaseui
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setLogo(R.mipmap.ic_launcher)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build();
            activityResultLauncher.launch(signInIntent);
        } else {
            goToMainActivity();
        }
    }
}
