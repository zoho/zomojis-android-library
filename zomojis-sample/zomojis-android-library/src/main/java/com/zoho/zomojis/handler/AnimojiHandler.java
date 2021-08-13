package com.zoho.zomojis.handler;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.LruCache;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zoho.zomojis.helper.ZomojiImageSpan;
import com.zoho.zomojis.model.Animoji;
import com.zoho.zomojis.model.PendingAnimoji;
import com.zoho.zomojis.utils.BlockingLifoQueue;

public class AnimojiHandler {

    private Context mContext;
    private LruCache<String, Animoji> mAnimojiCache;
    private HashMap<String, ArrayList<PendingAnimoji>> mPendingMap;

    private static final int MIN_FRAME = 24;
    private static final float SPEED = 10f;
    private static final float MAX_SPEED = (int) Math.ceil((MIN_FRAME/SPEED*1000/SPEED) / (MIN_FRAME / SPEED));

    private ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, new BlockingLifoQueue());

    private static AnimojiHandler instance;

    public static AnimojiHandler getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new AnimojiHandler(context);
    }

    public AnimojiHandler(Context context)
    {
        mContext = context;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mAnimojiCache = new LruCache<>(activityManager.getMemoryClass() * 1024 * 1024 / 2);
        mPendingMap = new HashMap<>();

        AnimojiHandler.getInstance();
    }

    public Animoji getAnimoji(String name, int bounds, TextView textView, boolean is72, int start, int end)
    {
        Animoji animoji;

        if (mAnimojiCache.get(name + "" + bounds) != null && mAnimojiCache.get(name + "" + bounds).getDrawable() != null)
        {
            animoji = mAnimojiCache.get(name + "" + bounds);
            animoji.getDrawable().setBounds(0, 0, bounds, bounds);
            animoji.addTextView(textView, 0);

            if (mPendingMap.containsKey(name + "" + bounds))
            {
                ArrayList notReadyList = mPendingMap.get(name + "" + bounds);
                PendingAnimoji p = new PendingAnimoji(textView, start, end);
                notReadyList.add(p);
                mPendingMap.put(name + "" + bounds, notReadyList);
            }
        }
        else
        {
            LevelListDrawable levelListDrawable = new LevelListDrawable();
            WeakReference<Bitmap> weakReference = new WeakReference<>(getSampleFrame("48/" + name, 10));
            Drawable drawable = new BitmapDrawable(mContext.getResources(), weakReference.get());
            levelListDrawable.addLevel(0, 0, drawable);

            animoji = new Animoji(name, levelListDrawable, 1, 1);
            animoji.getDrawable().setBounds(0, 0, bounds, bounds);
            mAnimojiCache.put(name + "" + bounds, animoji);
            ArrayList notReadyList = new ArrayList<>();
            PendingAnimoji p = new PendingAnimoji(textView, start, end);
            notReadyList.add(p);
            mPendingMap.put(name + "" + bounds, new ArrayList<PendingAnimoji>(notReadyList));

            AnimojiLoader anim = new AnimojiLoader(name, bounds, is72);
            pool.submit(anim);

        }

        return animoji;
    }

    private Bitmap getBitmapFromAsset(String name)
    {
        AssetManager assetManager = mContext.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }

    private Bitmap getSampleFrame(String name, int frame)
    {

        Bitmap bitmap = getBitmapFromAsset(name);

        int width = bitmap.getWidth();
        int height = width;
        int x = 0;
        int y = height * frame + frame;

        WeakReference<Bitmap> weakReference = new WeakReference<>(Bitmap.createBitmap(bitmap, x, y, width, height));

        return weakReference.get();
    }

    public String getFileNameFromPattern(String name) {
        return name+".png";   // No I18N
    }

    public void handlePause()
    {
        try
        {
            Map<String, Animoji> snapshot = mAnimojiCache.snapshot();
            for (String id : snapshot.keySet())
            {
                Animoji animoji = mAnimojiCache.get(id);
                animoji.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void handleResume()
    {
        try
        {
            Map<String, Animoji> snapshot = mAnimojiCache.snapshot();
            for (String id : snapshot.keySet())
            {
                Animoji animoji = mAnimojiCache.get(id);
                animoji.start();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    class AnimojiLoader extends Thread
    {

        private String fileName;
        private int bounds;
        private boolean is72;

        public AnimojiLoader(String fileName, int bounds, boolean is72)
        {
            this.fileName = fileName;
            this.bounds = bounds;
            this.is72 = is72;
        }

        @Override
        public void run() {
            super.run();

            {
                try
                {
                    Bitmap bitmap = getBitmapFromAsset(is72 ? "72/" + fileName : "48/" + fileName);
                    final LevelListDrawable levelListDrawable = new LevelListDrawable();
                    int width = bitmap.getWidth();
                    int height = width;
                    final int frames = (bitmap.getHeight() - 1) / (width + 1);

                    for (int i = 0; i < frames; i++)
                    {
                        int x = 0;
                        int y = height * i + i;

                        WeakReference<Bitmap> weakReference = new WeakReference<>(Bitmap.createBitmap(bitmap, x, y, width, height));
                        Drawable drawable = new BitmapDrawable(mContext.getResources(), weakReference.get());
                        levelListDrawable.addLevel(i, i, drawable);
                    }

                    int rate = (int) Math.ceil((MIN_FRAME/SPEED*1000/SPEED) / (frames / SPEED));

                    if (rate >= MAX_SPEED) {
                        rate -= MAX_SPEED *65/100; // 65% of actual speed
                    }


                    final int finalRate = rate;

                    Handler mainHandler = new Handler(mContext.getMainLooper());
                    Runnable myRunnable = new Runnable()
                    {
                        @Override
                        public void run()
                        {

                            try
                            {
                                Animoji animoji = new Animoji(fileName, levelListDrawable, frames, finalRate);
                                animoji.start();
                                animoji.getDrawable().setBounds(0, 0, bounds, bounds);

                                mAnimojiCache.get(fileName + "" + bounds).stop();
                                mAnimojiCache.put(fileName + "" + bounds, (animoji));

                                SpannableString spannableString = null;
                                ZomojiImageSpan[] spans;
                                ArrayList<PendingAnimoji> pendingAnimojis = mPendingMap.get(fileName+""+bounds);
                                if(pendingAnimojis!=null)
                                {
                                    for(int i=0;i<pendingAnimojis.size();i++)
                                    {
                                        PendingAnimoji pendingAnimoji = pendingAnimojis.get(i);
                                        TextView textview = pendingAnimoji.getTextView();
                                        int start = pendingAnimoji.getStart();
                                        int end = pendingAnimoji.getEnd();
                                        spannableString = new SpannableString(textview.getText());
                                        spans = spannableString.getSpans(start, end, ZomojiImageSpan.class);
                                        for (ZomojiImageSpan span : spans)
                                        {
                                            spannableString.removeSpan(span);
                                        }
                                        animoji.addTextView(textview, 0);
                                        spannableString.setSpan(new ZomojiImageSpan(animoji.getDrawable()), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        textview.setText(spannableString);

                                        if(textview instanceof EditText)
                                        {
                                            ((EditText)textview).setSelection(end+1);
                                        }
                                    }

                                    mPendingMap.remove(fileName+""+bounds);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    mainHandler.post(myRunnable);
                    bitmap.recycle();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
    }
}