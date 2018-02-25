package com.nirtonsoft.mediaplayer.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;

/**
 * Created by ordin on 18-2-24.
 */

public class ExCircleWaveView extends View {

    private double offSetAngle = (double) 360 / 90;//每次角度偏移量
    private float deviationAngle = 0f;//初始角度

    private int mWaveColor = Color.argb(255,255,255,255);
    private int mLineNum = 45;//频谱线条数量
    private int mNormalHeight = 3;//频谱线条平常高度
    private long mStartHeight;
    private int mSpeed = 10;//绘制频谱的频率
    private int[] mAlpha = new int[45];//谱线透明度
    private int[] oldH = new int[45];
    private int[] oldH2 = new int[45];

    private PointF insideLeftPoint;
    private PointF outsideLeftPoint;
    private PointF insideRightPoint;
    private PointF outsideRightPoint;

    private PointF insideLeftPoint2;
    private PointF outsideLeftPoint2;
    private PointF insideRightPoint2;
    private PointF outsideRightPoint2;
    //private PointF centerPoint;

    private Paint mInsidePaint = new Paint();//画内圈频谱的画笔
    private Paint mOutsidePaint = new Paint();//画外圈频谱的画笔
    private Timer timer = new Timer();
    private byte[] mFFT;

    public ExCircleWaveView(Context context){
        super(context);
    }

    public ExCircleWaveView(Context context, AttributeSet attr){
        super(context,attr);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mOutsidePaint.setStrokeWidth(8.0f);
        mInsidePaint.setStrokeWidth(8.0f);
        mOutsidePaint.setColor(mWaveColor);
        mInsidePaint.setColor(mWaveColor);
        mOutsidePaint.setAntiAlias(true);
        mInsidePaint.setAntiAlias(true);
        drawWave(canvas);
        postInvalidateDelayed(mSpeed);
    }

    /**
     * 通过中点，半径和偏移角度获取终点的坐标
     * @param ceterPoint 中心点
     * @param r 半径
     * @param angle 偏转角
     * @return
     */
    private PointF getPointF(PointF ceterPoint, int r, double angle){
        double A = Math.PI * angle / 180;
        float _newX = ceterPoint.x + r * (float)Math.cos(A);
        float _newY = ceterPoint.y + r * (float)Math.sin(A);
        return (new PointF(_newX,_newY));
    }

    private void drawWave(Canvas canvas){
        int _Height = getHeight();
        for (int i = 0; i < mLineNum; i ++){
            if (mFFT == null){
                insideLeftPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),240,offSetAngle * i + deviationAngle);
                outsideLeftPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),245,offSetAngle * i + deviationAngle);
                insideRightPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),240,360 - offSetAngle * (i + 1) + deviationAngle);
                outsideRightPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),245,360 - offSetAngle * (i + 1) + deviationAngle);

                insideLeftPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),235,offSetAngle * i + deviationAngle);
                outsideLeftPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),230,offSetAngle * i + deviationAngle);
                insideRightPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),235,360 - offSetAngle * (i + 1) + deviationAngle);
                outsideRightPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),230,360 - offSetAngle * (i + 1) + deviationAngle);
            } else {
                mStartHeight = (int) mFFT[i] * 6000;
                oldH[i] = (int) Math.pow(mStartHeight,2/7f);
                oldH2[i] = (int) Math.pow(mStartHeight,2/9f);
                mAlpha[i] = 255;
                if (oldH[i] < 5){
                    oldH[i] = 5;
                }
                if (oldH2[i] < 1){
                    oldH2[i] = 1;
                }
                mOutsidePaint.setAlpha(mAlpha[i]);
                mInsidePaint.setAlpha(mAlpha[i]);
                insideLeftPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),240,offSetAngle * i + deviationAngle);
                outsideLeftPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),240 + oldH[i],offSetAngle * i + deviationAngle);
                insideRightPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),240,360 - offSetAngle * (i + 1) + deviationAngle);
                outsideRightPoint = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),240 + oldH[i],360 - offSetAngle * (i + 1) + deviationAngle);

                insideLeftPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),235,offSetAngle * i - deviationAngle);
                outsideLeftPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),230 - oldH2[i],offSetAngle * i - deviationAngle);
                insideRightPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),235,360 - offSetAngle * (i + 1) - deviationAngle);
                outsideRightPoint2 = getPointF(new PointF((float) getWidth()/2,(float) getHeight()/2),230 - oldH2[i],360 - offSetAngle * (i + 1) - deviationAngle);
            }
            canvas.drawLine(insideLeftPoint.x,insideLeftPoint.y,outsideLeftPoint.x,outsideLeftPoint.y,mOutsidePaint);
            canvas.drawLine(insideRightPoint.x,insideRightPoint.y,outsideRightPoint.x,outsideRightPoint.y,mOutsidePaint);
            canvas.drawLine(insideLeftPoint2.x,insideLeftPoint2.y,outsideLeftPoint2.x,outsideLeftPoint2.y,mInsidePaint);
            canvas.drawLine(insideRightPoint2.x,insideRightPoint2.y,outsideRightPoint2.x,outsideRightPoint2.y,mInsidePaint);
            if (oldH[i] > 6){
                oldH[i] -= 1;
                if (mAlpha[i] > 8)
                    mAlpha[i] -= 1;
            }
            if (oldH2[i] > 2)
                oldH2[i] -= 1;
            deviationAngle = deviationAngle >= 360 ? 0:deviationAngle + 0.004f;
        }
    }

    public void setFFT(byte[] bytes){
        mFFT = bytes;
    }
}
