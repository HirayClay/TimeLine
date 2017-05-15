package com.example.seele.timeline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by CJJ on 2017/5/12 mm
 */

public class Bubble extends FrameLayout {

    private static final String TAG = "Bubble";
    private boolean mIsFloating;//是否作为悬浮窗口
    private ArrowAlign mArrowAlign;
    private float mArrowPosition;//arrow的起始位置
    private float mArrowAnglePostion;//arrow 尖距离arrow其实位置的距离
    private float mArrowWidth;//arrow宽度
    private float mArrowHeight;//arrow 高度
    //圆角半径
    private float mLtCorner;
    private float mRtCorner;
    private float mLbCorner;
    private float mRbCorner;

    //the drawable which draw self into a arrow also as "background"
    ArrowDrawable mArrowDrawable;

    private ArrayList<View> mMatchParentChildren = new ArrayList<>(1);

    private boolean mMeasureAllChildren = false;

    public Bubble(Context context) {
        super(context);
    }

    public Bubble(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Bubble);
        mIsFloating = array.getBoolean(R.styleable.Bubble_is_floating, false);
        mLtCorner = array.getDimensionPixelSize(R.styleable.Bubble_left_top_corner, 0);
        mRtCorner = array.getDimensionPixelSize(R.styleable.Bubble_right_top_corner, 0);
        mLbCorner = array.getDimensionPixelSize(R.styleable.Bubble_left_bottom_corner, 0);
        mRbCorner = array.getDimensionPixelSize(R.styleable.Bubble_right_bottom_corner, 0);
        mArrowHeight = array.getDimensionPixelSize(R.styleable.Bubble_arrow_height, 0);
        mArrowPosition = array.getDimensionPixelSize(R.styleable.Bubble_arrow_start_position, 0);
        mArrowAnglePostion = array.getDimensionPixelSize(R.styleable.Bubble_arrow_angle_position,0);
        mArrowWidth = array.getDimensionPixelSize(R.styleable.Bubble_arrow_width, 0);
        mArrowAlign = ArrowAlign.parseInt(array.getInteger(R.styleable.Bubble_arrow_direction, 0));

        Log.i(TAG,
                "Bubble: mIsFloating:" + mIsFloating + "\n"
                        + "mArrowAlign:" + mArrowAlign + "\n"
                        + "LtCorner:" + mLtCorner + "\n"
                        + "mRtCorner:" + mRtCorner + "\n"
                        + "mRbCorner" + mRbCorner + "\n"
                        + "mArrowHeight" + mArrowHeight + "\n"
                        + "mArrowPosition：" + mArrowPosition + "\n"
                        + "mArrowWidth:" + mArrowWidth);
        setPadding();
    }

    private void setPadding() {
        int left = getPaddingLeft();
        int right = getPaddingRight();
        int top = getPaddingTop();
        int bottom = getPaddingBottom();
        switch (mArrowAlign) {
            case LEFT:
                left += mArrowHeight;
                break;
            case RIGHT:
                right += mArrowHeight;
                break;
            case TOP:
                top += mArrowHeight;
                break;
            case BOTTOM:
                bottom += mArrowHeight;
                break;
        }
        setPadding(left, top, right, bottom);
    }

    public Bubble(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mArrowDrawable = new ArrowDrawable(mArrowAlign, mArrowWidth, mArrowHeight, mArrowPosition, mArrowAnglePostion, mLtCorner, mRtCorner, mLbCorner, mRbCorner, 0, 0, getWidth(), getHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mArrowDrawable != null)
            mArrowDrawable.draw(canvas);
    }
}
