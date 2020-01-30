package com.friends.tfrndz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.tfrndz.R;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {
    Context context;
    String[] questions;
    String[] answers;

    public AnswerAdapter(Context context, String[] answers){
        this.context = context;
        this.answers = answers;
        questions = context.getResources().getStringArray(R.array.questions);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item, parent, false);
        return new AnswerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.question.setText(questions[position]);
        holder.answer.setText(answers[position]);
    }

    @Override
    public int getItemCount() {
        return questions.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private TextView answer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.quest);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
