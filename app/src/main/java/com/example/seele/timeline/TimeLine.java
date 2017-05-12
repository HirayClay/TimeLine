package com.example.seele.timeline;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by CJJ on 2017/5/5..
 */

public class TimeLine extends View implements View.OnTouchListener {
    private static final String TAG = "TimeLine";
    int mMax;
    boolean[] activeNodes;
    @ColorInt
    int barColor;
    @ColorInt
    int normalColor;
    @ColorInt
    int activeColor;
    @ColorInt
    int mTextColor;

    int defaultPadding;
    int mNodeRadius;
    int mUnitGapSize;
    int mTextSize;
    int barThickness;

    Paint barPaint;
    TextPaint textPaint;
    String[] nodeText;

    private static final int DEFAULT_GAP_SIZE = 35;
    private static final int DEFAULT_PADDING = 20;
    private static final int DEFAULT_BAR_THICKNESS = 15;
    private int headerSpace;
    private int tailSpace;

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
        mNodeRadius = array.getDimensionPixelSize(R.styleable.TimeLine_nodeSize, 10);
        mUnitGapSize = array.getDimensionPixelSize(R.styleable.TimeLine_unitGapSize, 30);
        mTextSize = array.getDimensionPixelSize(R.styleable.TimeLine_textSize, DEFAULT_GAP_SIZE);
        barThickness = array.getDimensionPixelOffset(R.styleable.TimeLine_barThickness, DEFAULT_BAR_THICKNESS);
        defaultPadding = array.getDimensionPixelOffset(R.styleable.TimeLine_default_padding, DEFAULT_PADDING);
        mTextColor = array.getColor(R.styleable.TimeLine_textColor, Color.GREEN);

        array.recycle();
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
        int measureH = mTextSize + defaultPadding + 2 * mNodeRadius + defaultPadding;
        if (mMax > 0) {
            measureW = ((mMax - 1) * mUnitGapSize) + 2 * mNodeRadius;
            if (nodeText != null && nodeText.length > 0) {
                headerSpace = (int) Math.max(mNodeRadius, textPaint.measureText(nodeText[0]) / 2);
                tailSpace = nodeText.length >= mMax ? (int) Math.max(mNodeRadius, textPaint.measureText(nodeText[mMax - 1])) : 0;
                measureW += headerSpace + tailSpace;
                measureW -= 2 * mNodeRadius;
            }
            setMeasuredDimension(measureW, measureH);
        } else
            setMeasuredDimension(Math.max(w, measureW), Math.max(h, measureH));
    }

    public void setUnitGapSize(int unitGapSize) {
        if (unitGapSize <= 0)
            throw new IllegalStateException("unitGapSize can not be <= 0, but is " + unitGapSize);
        this.mUnitGapSize = unitGapSize;
        invalidate();
    }

    public int getUnitGapSize() {
        return mUnitGapSize;
    }

    public void perturbNode(int nodeIndex, boolean active) {
        if (nodeIndex <= mMax)
            activeNodes[nodeIndex] = active;
        invalidate();
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


    public void setNodeRadius(int nodeSize) {
        this.mNodeRadius = nodeSize;
        invalidate();
    }

    /**
     * the description below the node
     *
     * @param ndText
     */
    public void setNodeText(String[] ndText) {
        nodeText = ndText;
        invalidate();
    }

    public void setListNodeText(List<String> nodeText) {
        this.nodeText = (String[]) nodeText.toArray();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        barPaint.setColor(barColor);
        int space = headerSpace == 0 ? mNodeRadius : headerSpace;
        canvas.drawRect(space, defaultPadding, getMeasuredWidth() - tailSpace, defaultPadding + barThickness, barPaint);
        for (int i = 0; i < mMax; i++) {
            if (activeNodes[i]) {
                int x = mUnitGapSize * i + space;
                barPaint.setColor(activeColor);
                canvas.drawCircle(x, barThickness / 2 + defaultPadding, mNodeRadius, barPaint);
                Paint.FontMetrics fm = textPaint.getFontMetrics();
                int baseline = (int) (-fm.top + barThickness + defaultPadding + Math.max(barThickness, barThickness + mNodeRadius - barThickness / 2));
                Log.i(TAG, "onDraw: " + (nodeText == null));
                if (nodeText != null && i < nodeText.length && !TextUtils.isEmpty(nodeText[i]))
                    canvas.drawText(nodeText[i], x - textPaint.measureText(nodeText[i]) / 2, baseline, textPaint);
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
        if (y > defaultPadding - mNodeRadius / 2 && y < defaultPadding + barThickness + mNodeRadius / 2) {
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
                    int rx = index * mUnitGapSize;
                    int ry = barThickness / 2 + defaultPadding;
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
        /**
         * @param index the node index which is clicked
         * @param x     the x coordinate in screen of the clicked node
         * @param y     the y coordinate in screen of the clicked node
         */
        void onPointerClick(int index, int x, int y);
    }
}
