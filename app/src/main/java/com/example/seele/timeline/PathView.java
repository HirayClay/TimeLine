package com.example.seele.timeline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by CJJ on 2017/5/15.
 */

public class PathView extends View {

    private Paint mPaint;

    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ff00ff"));
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getPath(),mPaint);
    }

    Path getPath() {
        Path path = new Path();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        path.moveTo(10,10);
        path.rLineTo(width - 20,0);
        path.rLineTo(0,height-20);
        path.rLineTo(-width+20,0);
        path.rLineTo(0,-height+20);
        path.close();
        return path;
    }
}
