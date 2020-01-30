package com.friends.tfrndz;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.friends.tfrndz.util.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button signIn;
    private TextView register;
    private ImageView loading;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailAddress);
        password = findViewById(R.id.passwordSignin);
        signIn = findViewById(R.id.siginButton);
        register = findViewById(R.id.registerLink);
        loading = findViewById(R.id.loading);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(loading, "rotation", 0f, 180f);
        rotate.setRepeatCount(10);
        rotate.setDuration(500);
        rotate.start();

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator.validateEmail(email, "Please enter correct Email Address") && Validator.validateEditText(password, "Please enter proper password")) {
                    loading.setVisibility(View.VISIBLE);
                    sigInUser();
                }
            }
        });
    }

    private void sigInUser() {
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loading.setVisibility(View.GONE);
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                        } else {
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            startActivity(new Intent(SignIn.this, MainActivity.class));
        }
    }
}
