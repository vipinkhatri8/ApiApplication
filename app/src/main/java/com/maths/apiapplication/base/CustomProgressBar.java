package com.maths.apiapplication.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomProgressBar extends View {
    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width=getWidth();
        float height=getHeight();
        float firstDrawable=width/22;
        // setValuesforWidth(width);
        Paint myPaint = new Paint();
        myPaint.setColor(Color.parseColor("#9F041B"));
        myPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(0,0,firstDrawable,height,myPaint);
        canvas.drawRect(firstDrawable,0,2*firstDrawable,height,myPaint);
        canvas.drawRect(2*firstDrawable,0,3*firstDrawable,height,myPaint);
        Paint box2=new Paint();
        box2.setColor(Color.parseColor("#BF2135"));
        box2.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(3*firstDrawable,0,4*firstDrawable,height,box2);
        canvas.drawRect(4*firstDrawable,0,5*firstDrawable,height,box2);
        canvas.drawRect(5*firstDrawable,0,6*firstDrawable,height,box2);
        Paint box7=new Paint(Color.parseColor("#EF4B5A"));
        box7.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(6*firstDrawable,0,7*firstDrawable,height,box2);
//
//
        Paint box3=new Paint();
        box3.setColor(Color.parseColor("#EF4B5A"));
        box3.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(7*firstDrawable,0,8*firstDrawable,height,box3);
        canvas.drawRect(8*firstDrawable,0,9*firstDrawable,height,box3);
        Paint box8=new Paint(Color.parseColor("#F5515F"));
        box8.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(9*firstDrawable,0,10*firstDrawable,height,box3);
        canvas.drawRect(10*firstDrawable,0,11*firstDrawable,height,box3);
//        Log.d("testing",""+10*firstDrawable+" "+f11);
        Paint box4=new Paint();
        box4.setStyle(Paint.Style.FILL_AND_STROKE);
        box4.setColor(Color.parseColor("#FF9200"));
        canvas.drawRect(11*firstDrawable,0,12*firstDrawable,height,box4);
        Log.d("testing",""+11*firstDrawable+" "+12);
        canvas.drawRect(12*firstDrawable,0,13*firstDrawable,height,box4);

        canvas.drawRect(13*firstDrawable,0,14*firstDrawable,height,box4);

        canvas.drawRect(14*firstDrawable,0,15*firstDrawable,height,box4);
//
        Paint box5=new Paint();

        box5.setColor(Color.parseColor("#FFE500"));
        box5.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(15*firstDrawable,0,16*firstDrawable,height,box5);
        canvas.drawRect(16*firstDrawable,0,17*firstDrawable,height,box5);
        canvas.drawRect(17*firstDrawable,0,18*firstDrawable,height,box5);
        Paint box10=new Paint();
        box10.setStyle(Paint.Style.FILL_AND_STROKE);
        box10.setColor(Color.parseColor("#56FF00"));
        canvas.drawRect(18*firstDrawable,0,19*firstDrawable,height,box5);

        Paint box6=new Paint();
        box6.setStyle(Paint.Style.FILL_AND_STROKE);
        box6.setColor(Color.parseColor("#56FF00"));
        canvas.drawRect(19*firstDrawable,0,20*firstDrawable,height,box6);

        canvas.drawRect(20*firstDrawable,0,21*firstDrawable,height,box6);
        canvas.drawRect(21*firstDrawable,0,width,height,box6);

        invalidate();
    }
}
