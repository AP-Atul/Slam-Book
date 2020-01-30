package com.friends.tfrndz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.tfrndz.R;
import com.friends.tfrndz.UserAnswers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlamAdapter extends RecyclerView.Adapter<SlamAdapter.MyViewHolder> {
    Context context;
    List<String> names;
    List<Integer> colors = new ArrayList<Integer>()
    {
        {
            add(R.color.a);
            add(R.color.b);
            add(R.color.c);
            add(R.color.d);
            add(R.color.e);
            add(R.color.f);
            add(R.color.g);
            add(R.color.h);
            add(R.color.i);
        }
    };

    public SlamAdapter(Context context, List<String> names){
        this.context = context;
        this.names = names;
    }

    @NonNull
    @Override
    public SlamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slam_list_item, parent, false);
        return new SlamAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SlamAdapter.MyViewHolder holder, final int position) {
        holder.user.setText(names.get(position));

        int id = new Random().nextInt(colors.size());
        holder.cardView.setBackgroundColor(context.getResources().getColor(colors.get(id)));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserAnswers.class).putExtra("position", position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView user;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            cardView = itemView.findViewById(R.id.cv);
        }
    }
}
