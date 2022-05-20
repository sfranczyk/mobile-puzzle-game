package com.example.mobilepuzzle;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilepuzzle.adapter.SelectImageAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class PuzzleActivity extends AppCompatActivity {
    ArrayList<PuzzlePiece> pieces;
    private int actionBarHeight;
    private ConstraintLayout layoutBottom;
    private Date startTime;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        hideActivityBar();

        final RelativeLayout layout = findViewById(R.id.layout);
        layoutBottom = findViewById(R.id.constraintLayoutBottom);
        ImageView imageView = findViewById(R.id.imageViewPuzzleBoard);

        layoutBottom.setVisibility(View.GONE);

        Intent i = getIntent();
        if (i.hasExtra("id")) {
            int imgId = i.getExtras().getInt("id");
            SelectImageAdapter adapter = new SelectImageAdapter(this);
            imageView.setImageResource((Integer) adapter.getItem(imgId));
        }

        imageView.post(() -> {
            pieces = splitImage();
            TouchListener touchListener = new TouchListener(PuzzleActivity.this, layout.getWidth(), layout.getHeight());
            Collections.shuffle(pieces);
            for (PuzzlePiece piece : pieces) {
                piece.setOnTouchListener(touchListener);
                layout.addView(piece);
                // randomize position, on the bottom of the screen
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                lParams.topMargin = layout.getHeight() - piece.pieceHeight;
                piece.setLayoutParams(lParams);
            }
        });

        startTime = Calendar.getInstance().getTime();
    }

    private void hideActivityBar() {
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics()) / 2 - 16;
        }

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private ArrayList<PuzzlePiece> splitImage() {
        int rows = 4;
        int cols = 3;

        ImageView imageView = findViewById(R.id.imageViewPuzzleBoard);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(rows * cols);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3] + actionBarHeight;

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord + imageView.getLeft();
                piece.yCoord = yCoord + imageView.getTop();
                piece.pieceWidth = pieceWidth;
                piece.pieceHeight = pieceHeight;
                pieces.add(piece);

                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (imgViewH - actH) / 2;
        int left = (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public void checkGameOver() {
        if (isGameOver()) {
            Button btnBack = findViewById(R.id.btnBackToMenu);
            btnBack.setOnClickListener((l) -> finish());

            TextView textViewTime = findViewById(R.id.textViewTime);
            Date currentTime = Calendar.getInstance().getTime();
            long gameTime = (currentTime.getTime() - startTime.getTime()) / 1000;
            long gameMinutes = gameTime / 60;
            long gameSeconds = gameTime - gameMinutes * 60;
            textViewTime.setText(String.format("Game time:\n%s:%s", gameMinutes,
                    gameSeconds < 10 ? "0" + gameSeconds : gameSeconds));

            layoutBottom.setVisibility(View.VISIBLE);
        }
    }

    private boolean isGameOver() {
        for (PuzzlePiece piece : pieces) {
            if (piece.canMove) {
                return false;
            }
        }
        return true;
    }
}