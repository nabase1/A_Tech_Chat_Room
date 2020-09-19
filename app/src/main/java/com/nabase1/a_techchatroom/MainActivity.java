package com.nabase1.a_techchatroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nabase1.a_techchatroom.models.User;
import com.nabase1.a_techchatroom.utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseUtils.firebaseDatabase;
        mDatabaseReference = FirebaseUtils.databaseReference;

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtils.openFirebaseUtils(getString(R.string.db_node_user), this);
        FirebaseUtils.attachListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtils.detachListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.MY_CODE && resultCode == RESULT_OK){
            mFirebaseUser = mAuth.getCurrentUser();
            User user = new User();

            user.setName(mFirebaseUser.getDisplayName());
            user.setPhone(mFirebaseUser.getPhoneNumber());
            user.setUser_id(mFirebaseUser.getUid());
            user.setProfile_image("");
            user.setSecurity_level("1");

            mDatabaseReference.child(mFirebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "Welcome to the chat room!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseUtils.attachListener();
                    Toast.makeText(MainActivity.this, "Please Try again!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return true;
    }
}