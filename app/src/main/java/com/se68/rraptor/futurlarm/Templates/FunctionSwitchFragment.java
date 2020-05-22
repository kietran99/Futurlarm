package com.se68.rraptor.futurlarm.Templates;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.se68.rraptor.futurlarm.Class.Constants;
import com.se68.rraptor.futurlarm.Class.FunctionSwitcher;
import com.se68.rraptor.futurlarm.R;

public class FunctionSwitchFragment extends Fragment {

    private ImageButton btnList, btnNewFuturlarm, btnAchievement, btnSocial;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);
        mapping();
        tabColorSettings(FunctionSwitcher.getCurrent());
        btnList_click();
        btnNewFuturlarm_click();
        btnAchievement_click();
        btnSocial_click();
        return view;
    }

    public void mapping(){
        btnList = view.findViewById(R.id.ButtonList);
        btnNewFuturlarm = view.findViewById(R.id.ButtonNewFuturLarm);
        btnAchievement = view.findViewById(R.id.ButtonAchievement);
        btnSocial = view.findViewById(R.id.ButtonSocial);
        LinearLayout layout = view.findViewById(R.id.LayoutFunctionSwitcher);
        if (Constants.DARK_MODE) layout.setBackgroundResource(R.color.colorPrimaryDark);
    }

    public void btnList_click(){
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionSwitcher.setCurrent(0);
                switchTab(0);
            }
        });
    }

    public void btnNewFuturlarm_click(){
        btnNewFuturlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionSwitcher.setCurrent(1);
                switchTab(1);
            }
        });
    }

    public void btnAchievement_click(){
        btnAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionSwitcher.setCurrent(2);
                switchTab(2);
            }
        });
    }

    public void btnSocial_click(){
        btnSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FunctionSwitcher.setCurrent(3);
                switchTab(3);
            }
        });
    }

    public void switchTab(int current){
        switch (FunctionSwitcher.getFunctionTabs()[current]){
            case LIST:
                startActivity(new Intent(".FuturlarmList.FuturlarmManagerActivity"));
                getActivity().finish(); break;
            case NEW:
                startActivity(new Intent(".FuturlarmNew.NewFuturlarmActivity"));
                getActivity().finish(); break;
            case ACHIEVEMENT:
                startActivity(new Intent(".FuturlarmAchievement.AchievementActivity"));
                getActivity().finish(); break;
            case SOCIAL:
                startActivity(new Intent(".FuturlarmSocial.UserProfileActivity"));
                getActivity().finish(); break;
        }
    }

    //Change selected color when starting a new activity
    public void tabColorSettings(int current){
        switch (FunctionSwitcher.getFunctionTabs()[current]){
            case LIST:
                if (!Constants.DARK_MODE) btnList.setImageResource(R.drawable.futurlarm_list_selected);
                break;
            case NEW:
                if (!Constants.DARK_MODE) btnNewFuturlarm.setImageResource(R.drawable.new_futurlarm_selected);
                break;
            case ACHIEVEMENT:
                if (!Constants.DARK_MODE) btnAchievement.setImageResource(R.drawable.achievement_selected);
                break;
            case SOCIAL:
                if (!Constants.DARK_MODE) btnSocial.setImageResource(R.drawable.account_manager_selected);
                break;
        }
    }

}
