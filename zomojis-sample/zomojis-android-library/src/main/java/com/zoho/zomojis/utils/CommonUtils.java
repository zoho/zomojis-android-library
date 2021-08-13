package com.zoho.zomojis.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import android.util.TypedValue;

import java.util.ArrayList;

import com.zoho.zomojis.R;
import com.zoho.zomojis.handler.AnimojiHandler;
import com.zoho.zomojis.provider.CursorUtility;
import com.zoho.zomojis.provider.ZomojiContract;
import com.zoho.zomojis.provider.ZomojiDatabase;

public class CommonUtils
{

    private static Application mAppInstance;
    private static Context mContext;

    public static void init(Application appInstance)
    {
        mAppInstance = appInstance;
        mContext = appInstance.getApplicationContext();

        registerAppCallbacks();
    }

    public static Context getContext()
    {
        return mContext;
    }

    public static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Drawable changeDrawableColor(int id, int newColor, Context context)
    {
        Drawable mDrawable = null;
        try
        {
            mDrawable = ContextCompat.getDrawable(context, id);
            mDrawable.mutate();
            mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mDrawable;
    }

    public static Drawable changeDrawableColor(Drawable mDrawable, int newColor)
    {
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    public static int getAppColor(Context context)
    {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    public static ArrayList getRecentEmoji(int count)
    {
        ArrayList recent = new ArrayList();
        Cursor cursor=null;
        try
        {
            cursor=CursorUtility.INSTANCE.executeQuery(ZomojiDatabase.Tables.ZOMOJIUSAGE, null, ZomojiContract.ZomojiUsage.CODE+" NOT LIKE '%!:'", null, null, null, ZomojiContract.ZomojiUsage.UC+" DESC, "+ZomojiContract.ZomojiUsage.MTIME+" DESC", ""+count);
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
            {
                recent.add(cursor.getString(cursor.getColumnIndex(ZomojiContract.ZomojiUsage.CODE)));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(cursor != null)
            {
                cursor.close();
            }
        }
        return recent;
    }

    public static ArrayList getRecentAnimoji(int count)
    {
        ArrayList recent = new ArrayList();
        Cursor cursor=null;
        try
        {
            cursor= CursorUtility.INSTANCE.executeQuery(ZomojiDatabase.Tables.ZOMOJIUSAGE, null, ZomojiContract.ZomojiUsage.CODE+" LIKE '%!:'", null, null, null, ZomojiContract.ZomojiUsage.UC+" DESC, "+ZomojiContract.ZomojiUsage.MTIME+" DESC", ""+count);  // No I18N
            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
            {
                recent.add(cursor.getString(cursor.getColumnIndex(ZomojiContract.ZomojiUsage.CODE)));
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(cursor != null)
            {
                cursor.close();
            }
        }
        return recent;
    }

    private static void registerAppCallbacks()
    {
        try {
            mAppInstance.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks()
            {

                @Override
                public void onActivityStopped(Activity activity)
                {

                }

                @Override
                public void onActivityStarted(Activity activity)
                {

                }

                @Override
                public void onActivityResumed(Activity activity)
                {
                    AnimojiHandler.getInstance().handleResume();
                }

                @Override
                public void onActivityPaused(Activity activity)
                {
                    AnimojiHandler.getInstance().handlePause();
                }

                @Override
                public void onActivityDestroyed(Activity activity)
                {

                }


                @Override
                public void onActivityCreated(Activity activity,
                                              Bundle savedInstanceState)
                {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity,
                                                        Bundle outState)
                {

                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
