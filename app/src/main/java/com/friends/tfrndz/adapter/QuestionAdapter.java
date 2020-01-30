package com.friends.tfrndz.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.friends.tfrndz.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder> {
    Context context;
    String[] questions;
    static String[] answers;

    public QuestionAdapter(Context context){
        this.context = context;
        questions = context.getResources().getStringArray(R.array.questions);
        answers = new String[questions.length];


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item, parent, false);
        return new QuestionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.question.setText(questions[position]);
        holder.answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addToList(holder.answer.getText().toString().trim(), position);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void addToList(String ans, int position) {
//        databaseReference.child(userId).child(String.valueOf(position)).setValue(ans);
        answers[position] = ans;
    }

    public static String[] getAnswers(){
        return answers;
    }

    @Override
    public int getItemCount() {
        return questions.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private EditText answer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.quest);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}
