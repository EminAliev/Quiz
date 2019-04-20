package com.example.aliev.quiz;

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
import com.example.aliev.quiz.datebases.DataBase;
import com.example.aliev.quiz.decoration.Common;
import com.example.aliev.quiz.model.Questions;
import com.example.aliev.quiz.model.Questions_curr;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.w3c.dom.Text;

public class QActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean isAnswerView = false;
    TextView rightAnswer, wrongAnswer;
    RecyclerView recyclerView;
    AnswerAdapter answerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Common.select.getName());
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getQuestion();

        if (Common.questionList.size() > 0) {

            rightAnswer = (TextView) findViewById(R.id.txt_ques_r);
            rightAnswer.setVisibility(View.VISIBLE);

            rightAnswer.setText(new StringBuilder(String.format("%d/%d", Common.rightCount, Common.questionList.size())));

            recyclerView = (RecyclerView) findViewById(R.id.gr_ans);
            recyclerView.setHasFixedSize(true);
            if (Common.questionList.size() > 5)
                recyclerView.setLayoutManager(new GridLayoutManager(this, Common.questionList.size() / 2));
            answerAdapter = new AnswerAdapter(this, Common.answerList);
            recyclerView.setAdapter(answerAdapter);

            viewPager = (ViewPager) findViewById(R.id.viewpage);
            tabLayout = (TabLayout) findViewById(R.id.slid_tab);


            genFragment();

            FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),
                    this,
                    Common.fragList);

            viewPager.setAdapter(fragmentAdapter);

            tabLayout.setupWithViewPager(viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                int SCROLL_R = 0;
                int SCROLL_L = 1;
                int SCROLL_UNDETERMINED = 3;

                int currentScroll = 2;

                private void setScrollingDir(float positionOff) {
                    if ((1 - positionOff) >= 0.5) {
                        this.currentScroll = SCROLL_R;
                    } else if ((1 - positionOff) <= 0.5) {
                        this.currentScroll = SCROLL_L;
                    }
                }

                private boolean isScrollDirUndetermited() {
                    return currentScroll == SCROLL_UNDETERMINED;
                }

                private boolean isScrollR() {
                    return currentScroll == SCROLL_R;
                }

                private boolean isScrollL() {
                    return currentScroll == SCROLL_L;
                }


                @Override
                public void onPageScrolled(int i, float v, int i1) {
                    if (isScrollDirUndetermited())
                        setScrollingDir(v);
                }

                @Override
                public void onPageSelected(int i) {

                    BlankFragment blankFragment;
                    int position = 0;
                    if (i > 0) {
                        if (isScrollR()) {
                            blankFragment = Common.fragList.get(i - 1);
                            position = i - 1;
                        } else if (isScrollL()) {
                            blankFragment = Common.fragList.get(i + 1);
                            position = i + 1;
                        } else {
                            blankFragment = Common.fragList.get(position);
                        }

                    } else {
                        blankFragment = Common.fragList.get(0);
                        position = 0;
                    }

                    Questions_curr questions_st = blankFragment.getSelectAns();
                    Common.answerList.add(position, questions_st);
                    answerAdapter.notifyDataSetChanged();

                    countCorAns();

                    rightAnswer.setText(new StringBuilder(String.format("%d", Common.rightCount))
                    .append("/")
                    .append(String.format("%d", Common.questionList.size())).toString());
                    wrongAnswer.setText(String.valueOf(Common.wrongCount));
                    if (questions_st.getType() == Common.ANSWER_TYPE.NO_ANSWER) {
                        blankFragment.show_correct();
                        blankFragment.disable_anw();
                    }

                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if (i == ViewPager.SCROLL_STATE_IDLE)
                        this.currentScroll = SCROLL_UNDETERMINED;
                }
            });


        }
    }
    private void finishGame() {
        int pos = viewPager.getCurrentItem();
        BlankFragment blankFragment = Common.fragList.get(pos);
        Questions_curr questions_st = blankFragment.getSelectAns();
        Common.answerList.set(pos, questions_st);
        answerAdapter.notifyDataSetChanged();



        countCorAns();

        rightAnswer.setText(new StringBuilder(String.format("%d", Common.rightCount))
                .append("/")
                .append(String.format("%d", Common.questionList.size())).toString());

        wrongAnswer.setText(String.valueOf(Common.wrongCount));

        if (questions_st.getType() == Common.ANSWER_TYPE.NO_ANSWER) {
            blankFragment.show_correct();
            blankFragment.disable_anw();
        }
    }

    private void countCorAns() {
        Common.rightCount = Common.wrongCount = 0;
        for (Questions_curr item : Common.answerList) {
            if (item.getType() == Common.ANSWER_TYPE.RIGHT_ANSWER)
                Common.rightCount++;
            else if (item.getType() == Common.ANSWER_TYPE.WRONG_ANSWER)
                Common.wrongCount++;
        }

    }

    private void genFragment() {
        for (int i = 0; i < Common.questionList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            BlankFragment blankFragment = new BlankFragment();
            blankFragment.setArguments(bundle);

            Common.fragList.add(blankFragment);
        }
    }


    private void getQuestion() {
        Common.questionList = DataBase.getInst(this).getQuestion(Common.select.getId());
        if (Common.questionList.size() == 0) {
            new MaterialStyledDialog.Builder(this)
                    .setTitle("-_-")
                    .setIcon(R.drawable.ic_sentiment_dissatisfied_black_24dp)
                    .setDescription("Нет вопросов в этой " + Common.select.getName() + " категории")
                    .setPositiveText("ОК")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        } else {
            if (Common.answerList.size() > 0)
                Common.answerList.clear();

            for (int i = 0; i < Common.answerList.size(); i++) {
                Common.answerList.add(new Questions_curr(i, Common.ANSWER_TYPE.NO_ANSWER));
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_wrong);
        ConstraintLayout constraintLayout = (ConstraintLayout)item.getActionView();
        wrongAnswer = (TextView)constraintLayout.findViewById(R.id.txt_wrong_ans);
        wrongAnswer.setText(String.valueOf(0));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.q, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.finish_game) {
            if (!isAnswerView) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Заканчиваешь? ")
                        .setIcon(R.drawable.ic_sentiment_very_satisfied_black_24dp)
                        .setDescription("Ты хочешь завершить ?")
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
