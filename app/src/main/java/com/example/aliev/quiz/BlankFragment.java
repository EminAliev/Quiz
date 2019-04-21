package com.example.aliev.quiz;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aliev.quiz.decoration.Common;
import com.example.aliev.quiz.model.Questions;
import com.example.aliev.quiz.model.Questions_curr;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.nio.charset.CharsetEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment implements QuestionIterface {

    TextView text_question;
    CheckBox check_A;
    CheckBox check_B;
    CheckBox check_C;
    CheckBox check_D;
    FrameLayout frameLayout_image;
    ProgressBar progressBar;

    Questions questions;
    int question_id = -1;

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_blank, container, false);
        question_id = getArguments().getInt("index", -1);
        questions = Common.questionList.get(question_id);
        final TextView textView = (TextView)itemView.findViewById(R.id.text_view);

        if (questions != null) {
            frameLayout_image = (FrameLayout) itemView.findViewById(R.id.lay_image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            if (questions.isImageQ()) {
                ImageView imageView_question = (ImageView) itemView.findViewById(R.id.image_questions);
                Picasso.get().load(questions.getqImage()).into(imageView_question, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                frameLayout_image.setVisibility(View.GONE);
            }
            text_question = (TextView) itemView.findViewById(R.id.text_question);
            text_question.setText(questions.getqText());
            check_A = (CheckBox) itemView.findViewById(R.id.check_A);
            check_A.setText(questions.getAnsA());
            check_A.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.select_val.add(check_A.getText().toString());
                    else
                        Common.select_val.remove(check_A.getText().toString());

                }
            });
            check_B = (CheckBox) itemView.findViewById(R.id.check_B);
            check_B.setText(questions.getAnsB());
            check_B.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.select_val.add(check_B.getText().toString());
                    else
                        Common.select_val.remove(check_B.getText().toString());
                }
            });
            check_C = (CheckBox) itemView.findViewById(R.id.check_C);
            check_C.setText(questions.getAnsC());
            check_C.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.select_val.add(check_C.getText().toString());
                    else
                        Common.select_val.remove(check_C.getText().toString());
                }
            });
            check_D = (CheckBox) itemView.findViewById(R.id.check_D);
            check_D.setText(questions.getAnsD());
            check_D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        Common.select_val.add(check_D.getText().toString());
                    else
                        Common.select_val.remove(check_D.getText().toString());
                }
            });


        }
        return itemView;
    }

    @Override
    public Questions_curr getSelectAns() {
        Questions_curr questions_curr = new Questions_curr(question_id, Common.ANSWER_TYPE.NO_ANSWER);
        StringBuilder res = new StringBuilder();
        if (Common.select_val.size() > 1) {
            Object[] arrAnswer = Common.select_val.toArray();
            for (int i = 0; i < arrAnswer.length; i++) {
                if (i < arrAnswer.length - 1)
                    res.append(new StringBuilder(((String) arrAnswer[i]).substring(0, 1)).append(","));
                else
                    res.append(new StringBuilder((String) arrAnswer[i]).substring(0, 1));
            }
        }
        else if (Common.select_val.size() == 1) {
            Object[] arrAnswer = Common.select_val.toArray();
            res.append((String) arrAnswer[0]).substring(0, 1);
        }

        if (questions != null) {
            if (!TextUtils.isEmpty(res)) {
                if (res.toString().equals(questions.getCorretcly_answer()))
                    questions_curr.setType(Common.ANSWER_TYPE.RIGHT_ANSWER);
                else
                    questions_curr.setType(Common.ANSWER_TYPE.WRONG_ANSWER);
            }
            else
                questions_curr.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        else {
            Toast.makeText(getContext(), "Не могу получить вопрос", Toast.LENGTH_SHORT).show();
            questions_curr.setType(Common.ANSWER_TYPE.NO_ANSWER);
        }
        Common.select_val.clear();
        return questions_curr;
    }

    @Override
    public void show_correct() {
        String [] correctly = questions.getCorretcly_answer().split(",");
        for(String ans: correctly) {
            if (ans.equals("A")) {
                check_A.setTypeface(null, Typeface.BOLD);
                check_A.setTextColor(Color.RED);
            }
             else if (ans.equals("B")) {
                check_B.setTypeface(null, Typeface.BOLD);
                check_B.setTextColor(Color.RED);
            }
            else if (ans.equals("C")) {
                check_C.setTypeface(null, Typeface.BOLD);
                check_C.setTextColor(Color.RED);
            }
            else if (ans.equals("D")) {
                check_D.setTypeface(null, Typeface.BOLD);
                check_D.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public void disable_anw() {
        check_A.setEnabled(false);
        check_B.setEnabled(false);
        check_C.setEnabled(false);
        check_D.setEnabled(false);
    }

    @Override
    public void reset_question() {
        check_A.setEnabled(true);
        check_B.setEnabled(true);
        check_C.setEnabled(true);
        check_D.setEnabled(true);

        check_A.setChecked(false);
        check_B.setChecked(false);
        check_C.setChecked(false);
        check_D.setChecked(false);

        check_A.setTypeface(null, Typeface.NORMAL);
        check_A.setTextColor(Color.BLACK);
        check_B.setTypeface(null, Typeface.NORMAL);
        check_B.setTextColor(Color.BLACK);
        check_C.setTypeface(null, Typeface.NORMAL);
        check_C.setTextColor(Color.BLACK);
        check_D.setTypeface(null, Typeface.NORMAL);
        check_D.setTextColor(Color.BLACK);
    }

}
