package com.example.seele.timeline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by CJJ on 2017/5/12 mm
 */

public class Bubble extends FrameLayout {


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
        mArrowWidth = array.getDimensionPixelSize(R.styleable.Bubble_arrow_width, 0);
        mArrowAlign = ArrowAlign.parseInt(array.getInteger(R.styleable.Bubble_arrow_direction, 0));
    }

    public Bubble(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        final boolean measureMatchParentChildren =
                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                        MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (mMeasureAllChildren || child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Check against our foreground's minimum height and width
        final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        int resolvedWidth = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        int resolvedHeight = resolveSizeAndState(maxHeight, heightMeasureSpec,
                childState << MEASURED_HEIGHT_STATE_SHIFT);
        switch (mArrowAlign) {
            default:
            case RIGHT:
            case LEFT:
                resolvedWidth += mArrowHeight;
                break;
            case TOP:
            case BOTTOM:
                resolvedHeight += mArrowHeight;
                break;
        }
        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mArrowDrawable == null)
            mArrowDrawable = new ArrowDrawable(mArrowAlign, mArrowWidth, mArrowHeight, mArrowPosition,mArrowAnglePostion, mLtCorner, mRtCorner, mLbCorner, mRbCorner, 0, 0, getWidth(), getHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mArrowDrawable != null)
            mArrowDrawable.draw(canvas);
    }
}
