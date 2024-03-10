package com.golobon.g_chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private final static int SIGN_IN_CODE = 1;
    private RelativeLayout activityMain;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private FriendlyMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = findViewById(R.id.activity_main);

        auth = FirebaseAuth.getInstance();

        //Если пользователь не авторизован
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            Snackbar.make(activityMain, "Вы авторизованы",
                    Snackbar.LENGTH_LONG).show();
            displayAllMessages();
        }

        db = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = db.getReference();

        FirebaseRecyclerOptions.Builder<Message> options =
                new FirebaseRecyclerOptions.Builder<>();
        options.setQuery(databaseReference, Message.class);
        options.build();

        adapter = new FriendlyMessageAdapter(options, "Boo");

        binding.progressBar.setVisibility(View.INVISIBLE);
        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        binding.messageRecyclerView.setLayoutManager(manager);
        binding.messageRecyclerView.setAdapter(adapter);

// Scroll down when a new message arrives
// See MyScrollToBottomObserver for details
        adapter.registerAdapterDataObserver(
                new MyScrollToBottomObserver(binding.me ssageRecyclerView, adapter, manager)
        );

        FloatingActionButton fbtnSendMess = (FloatingActionButton) findViewById(R.id.btn_send);
        fbtnSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText tv_input = (EditText) MainActivity.this.findViewById(R.id.tv_input);
//                if (tv_input == null) return;
//                FirebaseDatabase.getInstance()
//                        .getReference()
//                        .push()
//                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
//                                tv_input.getText().toString())
//                        );
//                tv_input.setText("");
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
    }
    private void signOut() {
        auth.signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activityMain, "Вы авторизованы",
                        Snackbar.LENGTH_LONG)
                        .show();
                displayAllMessages();
            } else {
                Snackbar.make(activityMain, "Вы не авторизованы",
                        Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }
    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.lv_messages);
        FirebaseListAdapter<Message> firebaseLAdapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messUser, messTime, messText;
                messUser = v.findViewById(R.id.tv_user_name);
                messTime = v.findViewById(R.id.tv_message_time);
                messText = v.findViewById(R.id.tv_message_text);
                messUser.setText(model.getUserName());
                messText.setText(model.getTextMessage());
                messTime.setText(DateFormat.format(
                        "dd-mm-yyy HH:m:ss", model.getMessageTime()));
            }
        };
        listOfMessages.setAdapter(firebaseLAdapter);
    }
}