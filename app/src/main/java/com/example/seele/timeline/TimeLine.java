package com.example.seele.timeline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by CJJ on 2017/5/5..
 *
 */

public class TimeLine extends View implements View.OnTouchListener {
    private static final String TAG = "TimeLine";
    int mMax;
    int mUnitGapSize;
    boolean[] activeNodes;
    @ColorInt
    int barColor;
    @ColorInt
    int normalColor;
    @ColorInt
    int activeColor;
    @ColorInt
    int mTextColor;
    int mNodeSize;
    int mTextSize;
    int barThickness;

    Paint barPaint;
    TextPaint textPaint;

    private static final int DEFAULT_GAP_SIZE = 35;
    private static final int DEFAULT_PADDING = 20;
    private static final int DEFAULT_BAR_THICKNESS = 15;

    public TimeLine(Context context) {
        super(context);
    }

    public TimeLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeLine);
        mMax = array.getInteger(R.styleable.TimeLine_max, 0);
        activeColor = array.getColor(R.styleable.TimeLine_activeColor, Color.BLUE);
        normalColor = array.getColor(R.styleable.TimeLine_normalColor, Color.GREEN);
        barColor = array.getColor(R.styleable.TimeLine_barColor, Color.CYAN);
        mNodeSize = array.getDimensionPixelSize(R.styleable.TimeLine_nodeSize, 10);
        mUnitGapSize = array.getDimensionPixelSize(R.styleable.TimeLine_unitGapSize, 30);
        mTextSize = array.getDimensionPixelSize(R.styleable.TimeLine_textSize, DEFAULT_GAP_SIZE);
        barThickness = array.getDimensionPixelOffset(R.styleable.TimeLine_barThickness, DEFAULT_BAR_THICKNESS);
        mTextColor = array.getColor(R.styleable.TimeLine_textColor, Color.GREEN);

        activeNodes = new boolean[mMax];
        setOnTouchListener(this);
        initPaint();
    }

    private void initPaint() {
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        barPaint.setColor(barColor);
        barPaint.setStrokeWidth(5);
        barPaint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setColor(mTextColor);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(mTextSize);
        for (int i = 0; i < activeNodes.length; i++) {
            activeNodes[i] = true;
        }
    }

    public TimeLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        int measureW = 0;
        int measureH = mTextSize + DEFAULT_PADDING + 2 * mNodeSize + DEFAULT_PADDING;
        if (mUnitGapSize != DEFAULT_GAP_SIZE && mMax > 0) {
            Log.i(TAG, "onMeasure: ");
            measureW = ((mMax - 1) * mUnitGapSize) + 2 * mNodeSize;
            setMeasuredDimension(measureW, measureH);
            return;
        }
//        int modeW = MeasureSpec.getMode(widthMeasureSpec);
//        w = Math.min(w, measureW);
        setMeasuredDimension(Math.max(w, measureW), Math.max(h, measureH));
    }

    public void setUnitGapSize(int unitGapSize) {
        if (unitGapSize <= 0)
            throw new IllegalStateException("unitGapSize can not be <= 0, but is " + unitGapSize);
        this.mUnitGapSize = unitGapSize;
    }

    public int getUnitGapSize() {
        return mUnitGapSize;
    }

    public void perturbNode(int nodeIndex, boolean active) {
        if (nodeIndex <= mMax)
            activeNodes[nodeIndex] = active;
        invalidate();
    }

    public boolean[] getActiveNodes() {
        boolean[] activeNd = null;
        for (int i = 0; i < activeNodes.length; i++) {

        }
        return activeNd;
    }

    /**
     * set the node number
     *
     * @param max
     */
    public void setMax(int max) {
        this.mMax = max;
        activeNodes = new boolean[max];
        invalidate();
    }

    public void setNodeSize(int nodeSize) {
        this.mNodeSize = nodeSize;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        barPaint.setColor(barColor);
        canvas.drawRect(mNodeSize, DEFAULT_PADDING, getMeasuredWidth(), DEFAULT_PADDING + barThickness, barPaint);
        for (int i = 0; i < mMax; i++) {
            if (activeNodes[i]) {
                int x = mUnitGapSize * i + mNodeSize;
                barPaint.setColor(activeColor);
                canvas.drawCircle(x, barThickness / 2 + DEFAULT_PADDING, mNodeSize, barPaint);
                Paint.FontMetrics fm = textPaint.getFontMetrics();
                int baseline = (int) (-fm.top + barThickness + DEFAULT_PADDING + Math.max(barThickness, barThickness + mNodeSize - barThickness / 2));
                canvas.drawText(i + "day", x, baseline, textPaint);
            }
        }
    }

    int downPointIndex = -1;

    /**
     * 计算该点对应的point
     *
     * @param x x coordination
     * @param y y coordination
     * @return the pointer index the (x,y)
     */
    private int computePointIndex(int x, int y) {
        if (y > DEFAULT_PADDING - mNodeSize / 2 && y < DEFAULT_PADDING + barThickness + mNodeSize / 2) {
            int v = x / mUnitGapSize;
            return activeNodes[v] ? v : -1;
        }
        return -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPointIndex = computePointIndex((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP:
                int curX = (int) event.getX();
                int curY = (int) event.getY();
                int index = computePointIndex(curX, curY);
                if (index == downPointIndex & index != -1) {
                    // TODO: 2017/5/5
                    int[] loc = new int[2];
                    getLocationInWindow(loc);
                    int rx = index * mUnitGapSize;
                    int ry = barThickness / 2 + DEFAULT_PADDING;
                    if (onPointClickListener != null)
                        onPointClickListener.onPointerClick(index, (int) event.getRawX(), (int) event.getRawY());
                }
                downPointIndex = -1;
                break;
        }
        return true;
    }

    OnPointClickListener onPointClickListener;

    public void setOnPointClickListener(OnPointClickListener onPointClickListener) {
        this.onPointClickListener = onPointClickListener;
    }

    public interface OnPointClickListener {
        void onPointerClick(int index, int x, int y);
    }
}
