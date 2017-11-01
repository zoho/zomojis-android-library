package com.zoho.zomojis.model;

import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class Animoji
{

    String name ;
    private LevelListDrawable mDrawable;
    private int mMaxFrames;
    private int mRate;
    private Handler mHandler;
    private WeakHashMap<TextView, Integer> mTextViewMap;

    public Animoji(String name, LevelListDrawable drawable, int maxFrames, int rate)
    {
        mDrawable = (drawable);
        mMaxFrames = maxFrames;
        mRate = rate;
        mHandler = new Handler();
        this.name = name;
        mTextViewMap = new WeakHashMap<>();
    }

    public LevelListDrawable getDrawable()
    {
        return mDrawable;
    }

    public void addTextView(TextView textView, Integer currentFrame)
    {
        mTextViewMap.put(textView, currentFrame);
    }

    private Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {

            try
            {
                if (getDrawable() != null)
                {
                    Iterator iterator = mTextViewMap.entrySet().iterator();
                    if (!iterator.hasNext())
                    {
                        stop();
                    }
                    while (iterator.hasNext())
                    {
                        Map.Entry pair = (Map.Entry) iterator.next();
                        TextView textView = (TextView) pair.getKey();
                        Integer currentFrame = mTextViewMap.get(textView);

                        if (currentFrame == mMaxFrames)
                        {
                            currentFrame = 0;
                        }

                        getDrawable().setLevel(currentFrame);
                        currentFrame++;
                        textView.invalidate();
                        mTextViewMap.put(textView, currentFrame);
                    }

                    mHandler.postDelayed(this, mRate);
                }
                else
                {
                    stop();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    };

    public void start()
    {
        mHandler.post(mRunnable);
    }

    public void stop()
    {
        mHandler.removeMessages(0);
        mHandler.removeCallbacks(mRunnable);
        mHandler.removeCallbacksAndMessages(null);
    }

}
