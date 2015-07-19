package com.example.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.R;
import com.example.activities.ApplicationNavigator;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoiceFragment extends Fragment {

    private ApplicationNavigator mNavigator;

    public static ChoiceFragment newInstance() {
        return new ChoiceFragment();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mNavigator = (ApplicationNavigator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choice, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick({R.id.fragment_choice_beer, R.id.fragment_choice_wine, R.id.fragment_choice_liquor})
    protected void makeChoice(final TextView view) {
        final String choice = view.getText().toString().toLowerCase();
        mNavigator.navigateTo(MainFragment.newInstance(choice));
    }
}
