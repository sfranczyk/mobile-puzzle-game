package com.example.mobilepuzzle;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

public class PuzzlePiece extends AppCompatImageView {
    public int xCoord = 0;
    public int yCoord = 0;
    public int pieceWidth = 0;
    public int pieceHeight = 0;
    public boolean canMove = true;

    public PuzzlePiece(@NonNull Context context) {
        super(context);
    }
}
