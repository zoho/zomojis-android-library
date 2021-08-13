package com.zoho.zomojis.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import com.zoho.zomojis.parser.ZomojiParser;
import com.zoho.zomojis.R;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.MyViewHolder>
{

    Context mContext;
    int mCategoryId;
    int[] mEmojis;
    private OnEmojiClickListener mClickListener;
    ArrayList<String> mRecentemojis = new ArrayList<>();

    public EmojiAdapter(Context context, int categoryid, int[] emojicons, OnEmojiClickListener clickListener)
    {
        this.mContext = context;
        this.mCategoryId = categoryid;
        this.mEmojis = emojicons;
        this.mClickListener = clickListener;
    }

    public EmojiAdapter(Context context, int categoryid, ArrayList<String> recentemojis, OnEmojiClickListener clickListener)
    {
        this.mContext = context;
        this.mCategoryId = categoryid;
        this.mRecentemojis = recentemojis;
        this.mClickListener = clickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        public MyViewHolder(View v)
        {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.smileyimageview);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_emoji, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        if (mCategoryId == -1)
        {
            final String code = mRecentemojis.get(position);
            int id = ZomojiParser.getInstance().getEmojiResID(code);
            holder.imageView.setImageResource(id);
            holder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mClickListener != null)
                    {
                        mClickListener.onEmojiClick(code);
                    }
                }
            });
        }else
        {
            holder.imageView.setImageResource(mEmojis[position]);
            holder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mClickListener != null)
                    {
                        mClickListener.onEmojiClick(ZomojiParser.EMOJI_LISTING_CATEGORY[mCategoryId][position]);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        if (mCategoryId == -1)
        {
            return mRecentemojis.size();
        }else
        {
            return mEmojis.length;
        }
    }

    public interface OnEmojiClickListener
    {
        void onEmojiClick(String smiley);
    }

}
