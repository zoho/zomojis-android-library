package com.zoho.zomojis.model;

import android.widget.TextView;

public class PendingAnimoji
{
    private TextView textView;
    private int start;
    private int end;

    public PendingAnimoji(TextView textView, int start, int end)
    {
        this.textView = textView;
        this.start = start;
        this.end = end;
    }

    public TextView getTextView()
    {
        return textView;
    }

    public int getStart()
    {
        return start;
    }

    public int getEnd()
    {
        return end;
    }
}
