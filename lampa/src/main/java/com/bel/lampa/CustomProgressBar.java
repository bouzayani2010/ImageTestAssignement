package com.bel.lampa;

import android.annotation.TargetApi;

import android.content.Context;

import android.graphics.Canvas;

import android.graphics.Paint;

import android.graphics.Path;

import android.graphics.RectF;

import android.os.Build;

import android.util.AttributeSet;

import android.util.Log;
import android.view.View;

public class CustomProgressBar extends View {


    private int max = 100;

    private int progress;

    private Path path = new Path();

    int color = 0xffeaf210;
    int color_progress = 0xff333333;

    private Paint paint;

    private Paint mPaintProgress;

    private RectF mRectF;

    private int centerY;

    private int centerX;

    private int swipeAndgle = 0;


    public CustomProgressBar(Context context) {
        super(context);
        initUI();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initUI();
    }

    private void initUI() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(Utils.dpTopx(getContext(), 3));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);


        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeWidth(Utils.dpTopx(getContext(), 3));
        mPaintProgress.setColor(color_progress);
        Log.i("circlecolor 1", "::" + color_progress);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        int radius = (Math.min(viewWidth, viewHeight) - Utils.dpTopx(getContext(), 3)) / 2;

        path.reset();

        centerX = viewWidth / 2;
        centerY = viewHeight / 2;
        path.addCircle(centerX, centerY, radius, Path.Direction.CW);


        mRectF = new RectF(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);

    }


    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);

        canvas.drawPath(path, paint);

        canvas.drawArc(mRectF, 270, swipeAndgle, false, mPaintProgress);


    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        int percentage = progress * 100 / max;

        swipeAndgle = percentage * 360 / 100;

        invalidate();
    }

    public void setColor(int color) {
        this.color = color;
        initUI();
    }

    public void setColor_progress(int color_progress) {
        this.color_progress = color_progress;
        initUI();
    }
}