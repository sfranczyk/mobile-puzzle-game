package com.example.mobilepuzzle;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class MenuFragment extends Fragment {

    Button btnExit;
    Button btnNewGame;
    ImageView imgViewLogo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        btnExit = view.findViewById(R.id.btnExit);
        btnNewGame = view.findViewById(R.id.btnNewGame);
        imgViewLogo = (ImageView)view.findViewById(R.id.imageViewLogo);

        imgViewLogo.setImageResource(R.drawable.puzzle_logo);

        setButtonsOnClick();

        return view;
    }

    private void setButtonsOnClick() {
        btnExit.setOnClickListener(this::onClickBtnExit);
        btnNewGame.setOnClickListener(this::onClickBtnNewGame);
    }

    private void onClickBtnExit(View l) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void onClickBtnNewGame(View l) {
        Navigation.findNavController(l).navigate(R.id.navigate_to_selectPictureFragment);
    }
}