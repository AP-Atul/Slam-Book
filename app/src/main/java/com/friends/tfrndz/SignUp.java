package com.friends.tfrndz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.friends.tfrndz.util.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText name, email, password;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        findViewById(R.id.singinLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });

        findViewById(R.id.signupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator.validateEditText(name, "Please enter proper name") && Validator.validateEmail(email, "Please enter correct Email Address") && Validator.validateEditText(password, "Please enter proper 6 character password"))
                    createAccount();
            }
        });
    }

    private void createAccount() {
        final String nameText = name.getText().toString().trim();
        final String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            addToDatabase(emailText, nameText);
                        } else {
                            Toast.makeText(getApplicationContext(), "Problem occurred!" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addToDatabase(String emailText, String nameText) {
        String userId = firebaseAuth.getCurrentUser().getUid();

        databaseReference.child(userId).child("email").setValue(emailText);
        databaseReference.child(userId).child("name").setValue(nameText);
        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignUp.this, SignIn.class));
    }
}
