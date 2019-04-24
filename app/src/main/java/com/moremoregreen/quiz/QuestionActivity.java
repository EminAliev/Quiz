package com.moremoregreen.quiz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.moremoregreen.quiz.Adapter.AnswerSheetAdapter;
import com.moremoregreen.quiz.Adapter.QuestionFragmentAdapter;
import com.moremoregreen.quiz.DBHelper.DBHelper;
import com.moremoregreen.quiz.Model.CurrentQuestion;
import com.moremoregreen.quiz.Model.Question;
import com.moremoregreen.quiz.common.Common;

public class QuestionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean isAnswerModeView = false;
    TextView txt_right_answer, txt_wrong_answer;
    RecyclerView answer_sheet_view;
    AnswerSheetAdapter answerSheetAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        takeQuestion();

        if (Common.questionList.size() > 0) {

            txt_right_answer = (TextView) findViewById(R.id.txt_ques_right);
            txt_right_answer.setVisibility(View.VISIBLE);

            txt_right_answer.setText(new StringBuilder(String.format("%d/%d", Common.right_answer_count, Common.questionList.size())));


            answer_sheet_view = (RecyclerView) findViewById(R.id.grid_answer);
            answer_sheet_view.setHasFixedSize(true);

            if (Common.questionList.size() > 5) {
                answer_sheet_view.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
            }
            answerSheetAdapter = new AnswerSheetAdapter(this, Common.answerSheetList);
            answer_sheet_view.setAdapter(answerSheetAdapter);

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            tabLayout = (TabLayout) findViewById(R.id.slid_tab);


            genFragmentList();

            QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(),
                    this,
                    Common.fragmentsList);

            viewPager.setAdapter(questionFragmentAdapter);

            tabLayout.setupWithViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                int SCROLLING_RIGHT = 0;
                int SCROLLING_LEFT = 1;
                int SCROLLING_UNDETERMINED = 2;

                int currentScrollDirection = 2;

                private void setScrollingDirection(float positionOffset) {
                    if ((1 - positionOffset) >= 0.5)
                        this.currentScrollDirection = SCROLLING_RIGHT;
                    else if ((1 - positionOffset) <= 0.5)
                        this.currentScrollDirection = SCROLLING_LEFT;
                }

                private boolean isScrollingDirectionUndetermined() {
                    return currentScrollDirection == SCROLLING_UNDETERMINED;
                }

                private boolean isScrollingRight() {
                    return currentScrollDirection == SCROLLING_RIGHT;
                }

                private boolean isScrollingLeft() {
                    return currentScrollDirection == SCROLLING_LEFT;
                }




                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    if (isScrollingDirectionUndetermined())
                        setScrollingDirection(v);
                }

                @Override
                public void onPageSelected(int i) {

                    QuestionFragment questionFragment;
                    int position = 0;
                    if (i > 0) {
                        if (isScrollingRight()) {
                            questionFragment = Common.fragmentsList.get(i-1);
                            position = i - 1;
                        }
                        else if (isScrollingLeft()) {
                            questionFragment = Common.fragmentsList.get(i+1);
                            position = i + 1;
                        }
                        else {
                            questionFragment = Common.fragmentsList.get(position);
                        }
                    }
                    else {
                        questionFragment = Common.fragmentsList.get(0);
                        position = 0;
                    }

                    CurrentQuestion question_state = questionFragment.getSelectedAnswer();
                    Common.answerSheetList.set(position, question_state);
                    answerSheetAdapter.notifyDataSetChanged();

                    countCorrectAnswer();

                    txt_right_answer.setText(new StringBuilder(String.format("%d", Common.right_answer_count))
                            .append("/")
                            .append(String.format("%d", Common.questionList.size())).toString());

                    txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));

                    if (question_state.getType() == Common.ANSWER_TYPE.NO_ANSWER) {
                        questionFragment.showCorrectAnswer();
                        questionFragment.disableAnswer();
                    }

                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE)
                        this.currentScrollDirection = SCROLLING_UNDETERMINED;
                }
            });
        }
    }

    private void finishGame() {
        int position = viewPager.getCurrentItem();
        QuestionFragment questionFragment = Common.fragmentsList.get(position);
        CurrentQuestion question_state = questionFragment.getSelectedAnswer();
        Common.answerSheetList.set(position, question_state);
        answerSheetAdapter.notifyDataSetChanged();



        countCorrectAnswer();



        txt_right_answer.setText(new StringBuilder(String.format("%d", Common.right_answer_count))
                .append("/")
                .append(String.format("%d", Common.questionList.size())).toString());

        txt_wrong_answer.setText(String.valueOf(Common.wrong_answer_count));

        if (question_state.getType() == Common.ANSWER_TYPE.NO_ANSWER) {
            questionFragment.showCorrectAnswer();
            questionFragment.disableAnswer();
        }

    }




    private void countCorrectAnswer() {
        Common.right_answer_count = Common.wrong_answer_count = 0;
        for (CurrentQuestion item: Common.answerSheetList) {
            if (item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.right_answer_count++;

            else if (item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrong_answer_count++;
        }


    }

    private void genFragmentList() {
        for (int i = 0; i < Common.questionList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            QuestionFragment fragment = new QuestionFragment();
            fragment.setArguments(bundle);

            Common.fragmentsList.add(fragment);
        }
    }


    private void takeQuestion() {
        Common.questionList = DBHelper.getInstance(this).getQuestion(Common.selectedCategory.getId());
        if (Common.questionList.size() == 0) {
            new MaterialStyledDialog.Builder(this)
                    .setTitle("-_-")
                    .setIcon(R.drawable.ic_sentiment_dissatisfied_black_24dp)
                    .setDescription("Нет вопросов в этой " + Common.selectedCategory.getName() + " категории")
                    .setPositiveText("ОК")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        } else {
            if (Common.answerSheetList.size() > 0)
                Common.answerSheetList.clear();

            for (int i = 0; i < Common.questionList.size(); i++) {
                Common.answerSheetList.add(new CurrentQuestion(i, Common.ANSWER_TYPE.NO_ANSWER));
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_wrong_anwer);
        ConstraintLayout constraintLayout = (ConstraintLayout)item.getActionView();
        txt_wrong_answer = (TextView)constraintLayout.findViewById(R.id.txt_wrong_answer);
        txt_wrong_answer.setText(String.valueOf(0));

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_finish_game) {
            if (!isAnswerModeView) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Всё? ")
                        .setIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                        .setDescription("Ты хочешь завершить игру? ")
                        .setNegativeText("Нет")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("Да")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                finishGame();
                            }
                        }).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
