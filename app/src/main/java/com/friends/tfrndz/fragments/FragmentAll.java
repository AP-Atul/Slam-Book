package com.friends.tfrndz.fragments;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.tfrndz.R;
import com.friends.tfrndz.adapter.SlamAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FragmentAll extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ImageView loading;

    private static List<String[]> ans = new ArrayList<>();
    private static List<String> keys = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all, container, false);

        loading = view.findViewById(R.id.loading);
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ObjectAnimator rotate = ObjectAnimator.ofFloat(loading, "rotation", 0f, 180f);
        rotate.setRepeatCount(10);
        rotate.setDuration(500);
        rotate.start();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Slam");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String[] answers = new String[16];
                String key = dataSnapshot.getKey();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    answers[Integer.parseInt(snapshot.getKey())] = snapshot.getValue(String.class);
                }
                ans.add(answers);

                Log.d("sizes", String.valueOf(ans.size()));
                keys.add(key);
                getNameFromDB(key);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void getNameFromDB(String key) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User/"+ key+"/name");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.getValue() != null)) {
                    names.add(dataSnapshot.getValue().toString());
                    adapter = new SlamAdapter(getContext(), names);
                    recyclerView.setAdapter(adapter);

                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static String[] getAnswers(int pos){
        return ans.get(pos);
    }

    public static String getKey(int pos){
        return keys.get(pos);
    }
}
