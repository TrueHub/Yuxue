package vone.person.com.yuxue.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import vone.person.com.yuxue.R;


/**
 * Created by user on 2017/4/25.
 * 三层循环loading
 */

public class MyLoadingView extends View {
    public MyLoadingView(Context context) {
        this(context, null);
    }

    private static final String TAG = "MSL MyLoadingView";

    public MyLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int mFirstColor;//首层颜色
    private int mSecondColor;//另一层颜色
    private int mThirdColor;//第三层颜色
    private int mCircleWidth;//圆圈width熟悉
    private Paint mPaint;//paint画笔
    private Paint mCirclePaint;
    private int mProgress;//当前进度
    private int isNext;//是否进行下一圈


    public MyLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyLoadingView, 0, defStyleAttr);

        for (int i = 0; i < typedArray.length(); i++) {
            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.MyLoadingView_firstColor:
                    mFirstColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyLoadingView_secondColor:
                    mSecondColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.MyLoadingView_thirdColor:
                    mThirdColor = typedArray.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.MyLoadingView_circleWidth:
                    mCircleWidth = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())
                    );
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint = new Paint(mPaint);
        mCirclePaint.setColor(getResources().getColor(R.color.colorGray));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆弧的形状和大小的界限
        RectF rectF;
        int center;
        int radius;
        float x, y;
        if (getWidth() < getHeight()) {
            center = getWidth() / 2;
            radius = center - mCircleWidth / 2 - getPaddingLeft();
            int dif = getHeight() - getWidth();
            x = center;
            y = center + dif / 2;
            rectF = new RectF(x - radius, y - radius, x + radius, y + radius);
        } else {
            center = getHeight() / 2;
            radius = center - mCircleWidth / 2 - getPaddingLeft();
            int dif = getWidth() - getHeight();
            y = center;
            x = center + dif / 2;
            rectF = new RectF(x - radius, y - radius, x + radius, y + radius);
        }
        switch (isNext % 3) {
            case 0:
                if (isNext != 0) mCirclePaint.setColor(mThirdColor);
                mPaint.setColor(mFirstColor);//圆环的颜色
                Log.i(TAG, "onDraw: 0 :" + isNext);
                break;
            case 1:
                Log.i(TAG, "onDraw: 1 : " + isNext);
                mCirclePaint.setColor(mFirstColor);
                mPaint.setColor(mSecondColor);//圆环的颜色,为上一圈的圆弧的颜色
                break;
            case 2:
                mCirclePaint.setColor(mSecondColor);
                Log.i(TAG, "onDraw: 2 : " + isNext);
                mPaint.setColor(mThirdColor);//圆环的颜色,为上一圈的圆弧的颜色
                break;
        }
        canvas.drawArc(rectF, mProgress * 6f / 100 - 90, 360 - mProgress * 6f / 100, false, mCirclePaint);
        canvas.drawArc(rectF, -90, mProgress * 6f / 100, false, mPaint);
    }

    public void setmProgress(int mProgress) {
        if (mProgress % (100 * 60) == 0) {
            isNext++;
        }
        this.mProgress = mProgress % (100 * 60);
        postInvalidate();
    }

    public void setInit() {
        isNext = 0;
        mProgress = 0;
        mCirclePaint.setColor(getResources().getColor(R.color.colorGray));
        postInvalidate();
    }
}
