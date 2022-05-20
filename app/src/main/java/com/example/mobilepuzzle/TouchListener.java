package com.example.mobilepuzzle;

import static java.lang.Math.*;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.Random;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;

    private final int layoutWidth;
    private final int layoutHeight;

    private final PuzzleActivity activity;

    public TouchListener(PuzzleActivity activity, int layoutWidth, int layoutHeight) {
        this.activity = activity;
        this.layoutWidth = layoutWidth;
        this.layoutHeight = layoutHeight;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double tolerance = sqrt(pow(view.getWidth(), 2) + pow(view.getHeight(), 2)) / 10;

        PuzzlePiece piece = (PuzzlePiece) view;
        if (!piece.canMove) {
            return true;
        }
        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                setNewParams(piece, (int) (x - xDelta), (int) (y - yDelta));
                break;
            case MotionEvent.ACTION_UP:
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    setNewParams(piece, piece.xCoord, piece.yCoord);
                    piece.canMove = false;
                    sendViewToBack(piece);
                    activity.checkGameOver();
                } else {
                    setNewParams(piece, new Random().nextInt( layoutWidth - piece.pieceWidth),
                            layoutHeight - piece.pieceHeight);
                }
                break;
        }

        return true;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    private void setNewParams(PuzzlePiece piece, int left, int top) {
        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
        lParams.leftMargin = left;
        lParams.topMargin = top;
        piece.setLayoutParams(lParams);
    }
}