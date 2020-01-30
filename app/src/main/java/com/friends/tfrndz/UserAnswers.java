package com.friends.tfrndz;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.tfrndz.adapter.AnswerAdapter;
import com.friends.tfrndz.fragments.FragmentAll;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserAnswers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView image1;


    private String[] answers;
    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_answers);

        image1 = findViewById(R.id.image1);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int pos = getIntent().getIntExtra("position", -1);
        if(pos != -1) {
            answers = FragmentAll.getAnswers(pos);
            userId = FragmentAll.getKey(pos);
        }

        if(answers != null){
            adapter = new AnswerAdapter(getApplicationContext(), answers);
            recyclerView.setAdapter(adapter);
        }

        if(userId != null){
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserImages/" + userId + "/image1");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot != null)
                        addToUI(dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void addToUI(String url) {
        Picasso.get().load(url).into(image1);
    }
}
