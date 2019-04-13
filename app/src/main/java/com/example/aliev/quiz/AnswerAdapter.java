package com.example.aliev.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aliev.quiz.decoration.Common;
import com.example.aliev.quiz.model.Questions_curr;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder> {

    Context context;
    List<Questions_curr> questions_currList;

    public AnswerAdapter(Context context, List<Questions_curr> questions_currList) {
        this.context = context;
        this.questions_currList = questions_currList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View iView = LayoutInflater.from(context).inflate(R.layout.layout_answer_sheet_item, viewGroup, false);
        return new MyViewHolder(iView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (questions_currList.get(i).getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
            myViewHolder.question.setBackgroundResource(R.drawable.question_right_answer);
        else if (questions_currList.get(i).getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
            myViewHolder.question.setBackgroundResource(R.drawable.question_wrong_answer);
        else if (questions_currList.get(i).getType() == Common.ANSWER_TYPE.NO_ANSWER)
            myViewHolder.question.setBackgroundResource(R.drawable.question_no_answer);
    }


    @Override
    public int getItemCount() {
        return questions_currList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View question;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.q_item);
        }
    }
}
