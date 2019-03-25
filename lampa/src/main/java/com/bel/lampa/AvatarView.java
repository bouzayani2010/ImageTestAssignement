package com.bel.lampa;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class AvatarView extends FrameLayout {

    private CircleImageView circleImageView;
    private CustomProgressBar progressBar;

    public AvatarView(Context context) {
        super(context);
        //initView(context, attrs);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        // applyCustomAttr(context, attrs);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = inflate(getContext(), R.layout.custom_avatar_layout, null);
        circleImageView = view.findViewById(R.id.circleImageView);
        progressBar = (CustomProgressBar) view.findViewById(R.id.circular_progress_bar);


        applyCustomAttr(context, attrs);

        addView(view);
    }

    private void applyCustomAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Avatar,
                0, 0);
        int mDiamater = 0;
        try {

            mDiamater = a.getInteger(R.styleable.Avatar_diamater, 0);
            resizeView(context, mDiamater);


            int circlecolorProgress = a.getColor(R.styleable.Avatar_colorProgress, 0);//global var and a is the typed array
            Log.i("circlecolor", "::" + circlecolorProgress);
            progressBar.setColor_progress(circlecolorProgress);

            int circlecolor = a.getColor(R.styleable.Avatar_colorCircle, 0);//global var and a is the typed array
            Log.i("circlecolor", "::" + circlecolor);
            progressBar.setColor(circlecolor);

        } finally {
            a.recycle();
        }
    }

    private void resizeView(Context context, int mDiamater) {
        int mdpDiamater = Utils.dpTopx(context, mDiamater);

        RelativeLayout.LayoutParams progress_layoutParams = new RelativeLayout.LayoutParams(mdpDiamater, mdpDiamater);
        progress_layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        progressBar.setLayoutParams(progress_layoutParams);
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }

    public CustomProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(CustomProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setDiameter(Context context, int diamter) {
        resizeView(context, diamter);
    }

    public void setColor(Context context, int color) {
        progressBar.setColor(color);

    }

    public void setColorProgress(Context context, int color) {
        progressBar.setColor_progress(color);

    }
}
