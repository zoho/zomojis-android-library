package com.zoho.zomojis.helper;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import java.lang.ref.WeakReference;

public class ZomojiImageSpan extends ImageSpan
{

    private int initialDescent = 0;
    private int extraSpace = 0;

    public ZomojiImageSpan(final Drawable drawable)
    {
        this(drawable, DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    public ZomojiImageSpan(final Drawable drawable, final int verticalAlignment)
    {
        super(drawable, verticalAlignment);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm)
    {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null)
        {
            if (rect.bottom - (fm.descent - fm.ascent) >= 0)
            {
                initialDescent = fm.descent;
                extraSpace = rect.bottom - (fm.descent - fm.ascent);
            }

            fm.descent = extraSpace / 2 + initialDescent;
            fm.bottom = fm.descent;

            fm.ascent = -rect.bottom + fm.descent;
            fm.top = fm.ascent;
        }

        return rect.right;
    }

    private Drawable getCachedDrawable()
    {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null)
        {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }

    private WeakReference<Drawable> mDrawableRef;
}
