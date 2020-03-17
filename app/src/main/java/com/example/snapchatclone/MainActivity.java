package com.example.snapchatclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText emailEdittext;
    EditText passwordEdittext;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEdittext=findViewById(R.id.emailEditText);
        passwordEdittext=findViewById(R.id.passwordEditText);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            login();
        }
    }

    private void login() {
        //Move to next Activity
        Intent intent = new Intent(MainActivity.this,SnapsActivity.class);
        startActivity(intent);
    }

    public void goClicked(View view)
    {
        //check if we can log in the user
        mAuth.signInWithEmailAndPassword(emailEdittext.getText().toString(), passwordEdittext.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            login();
                        } else {
                            // If sign in fails, SignUp the User.
                            mAuth.createUserWithEmailAndPassword(emailEdittext.getText().toString(), passwordEdittext.getText().toString())
                                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign Up success
                                                FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(emailEdittext.getText().toString());
                                                login();
                                                Log.d(TAG, "createUserWithEmail:success");
                                                FirebaseUser user = mAuth.getCurrentUser();

                                            } else {
                                                // If sign in fails, display a message to the user.

                                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                            // ...
                                        }
                                    });

                        }
                    }
                });
    }
}
