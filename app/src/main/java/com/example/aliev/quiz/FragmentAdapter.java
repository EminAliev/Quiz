package com.example.aliev.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.aliev.quiz.decoration.Common;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    Context context;
    List<BlankFragment> fragmentList;

    public FragmentAdapter(FragmentManager fm, Context context, List<BlankFragment> fragmentList) {
        super(fm);
        this.context = context;
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return new StringBuilder("Вопрос ").append(position + 1).toString();
    }
}

