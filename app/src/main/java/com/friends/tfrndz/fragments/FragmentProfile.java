package com.friends.tfrndz.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.friends.tfrndz.R;
import com.friends.tfrndz.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfile extends Fragment {

    private DatabaseReference databaseReference;
    private TextView email, name;
    private ImageView loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        email = view.findViewById(R.id.userEmail);
        name = view.findViewById(R.id.userName);
        loading = view.findViewById(R.id.loading);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(loading, "rotation", 0f, 180f);
        rotate.setRepeatCount(100);
        rotate.setDuration(500);
        rotate.start();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    String email = dataSnapshot.child("email").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();

                    setValuesInUI(name, email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), SignIn.class));
            }
        });


        return view;
    }

    private void setValuesInUI(String n, String e) {
        email.setText(e);
        name.setText(n);

        loading.setVisibility(View.GONE);
    }

}
