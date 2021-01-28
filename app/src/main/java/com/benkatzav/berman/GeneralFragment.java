package com.benkatzav.berman;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class GeneralFragment extends Fragment {
    protected View rootView;
    protected Context context;
    protected FirebaseAuth mAuth;
    private static GeneralFragment currentFragment;
    private GeneralFragment previousFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        context = getContext();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void callFragment(GeneralFragment next_fragment, GeneralFragment previous_fagment) {
        currentFragment = next_fragment;
        if (previous_fagment != null) {
            next_fragment.setPreviousFragment(previous_fagment);
        } else {
            next_fragment.setPreviousFragment(previousFragment);
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, (Fragment) next_fragment, next_fragment.getClass().getSimpleName())
                .commit();
    }

    public static GeneralFragment getCurrentFragment() {
        return currentFragment;
    }

    public GeneralFragment getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(GeneralFragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public static void setCurrentFragment(GeneralFragment currentFragment) {
        GeneralFragment.currentFragment = currentFragment;
    }
}
