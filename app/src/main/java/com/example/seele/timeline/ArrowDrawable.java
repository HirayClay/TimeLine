package com.example.seele.timeline;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by CJJ on 2017/5/12.
 */

public class ArrowDrawable extends Drawable {

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
    private Paint mPaint;
    Rect rect;
    Path mPath;


    public ArrowDrawable(ArrowAlign mArrowAlign, float mArrowWidth, float mArrowHeight, float mArrowPosition,
                         float mArrowAnglePosition,
                         float ltCorner, float rtCorner, float lbCorner, float rbCorner,
                         int left, int top, int right, int bottom) {
        this.mArrowAlign = mArrowAlign;
        this.mArrowPosition = mArrowPosition;
        this.mArrowHeight = mArrowHeight;
        this.mArrowWidth = mArrowWidth;
        this.mArrowAnglePostion = mArrowAnglePosition;
        this.mLtCorner = ltCorner;
        this.mRtCorner = rtCorner;
        this.mLbCorner = lbCorner;
        this.mRbCorner = rbCorner;
        this.rect = new Rect(left, top, right, bottom);
        init();

    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#ff00ff"));
        mPaint.setStrokeWidth(2);

        mPath = new Path();
        mPath.moveTo(rect.left + mArrowHeight, rect.top + mLtCorner);
        mPath.rQuadTo(0, -mLtCorner, mLtCorner, -mLtCorner);

        mPath.rLineTo(rect.width()-mArrowHeight - mLtCorner - mRtCorner, 0);
        mPath.rQuadTo(mRtCorner, 0, mRtCorner, mRtCorner);

        mPath.rLineTo(0, rect.height() - mRtCorner - mRbCorner);
        mPath.rQuadTo(0, mRbCorner, -mRbCorner, mRbCorner);

        mPath.rLineTo(-(rect.width()-mArrowHeight - mLbCorner - mRbCorner), 0);
        mPath.rQuadTo(-mLbCorner, 0, -mLbCorner, -mLbCorner);

        mPath.rLineTo(0, -(rect.height() - mArrowPosition  - mArrowWidth- mLbCorner));
        mPath.rLineTo(-mArrowHeight, -(mArrowWidth - mArrowAnglePostion));
        mPath.rLineTo(mArrowHeight, -mArrowAnglePostion);

        float space = mArrowPosition - mLtCorner > 0 ? mArrowPosition - mLtCorner : 0;
        mPath.rLineTo(0, -space);
        mPath.close();
    }

    @Override
    public void setBounds(@NonNull Rect bounds) {
        super.setBounds(rect);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mPath != null&&!mPath.isEmpty())
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getIntrinsicHeight() {
        return rect.height();
    }

    @Override
    public int getIntrinsicWidth() {
        return rect.width();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
