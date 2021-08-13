package com.zoho.zomojis.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.zoho.zomojis.R;
import com.zoho.zomojis.parser.ZomojiParser;

import static com.zoho.zomojis.utils.CommonUtils.dpToPx;

public class AnimojiAdapter extends RecyclerView.Adapter<AnimojiAdapter.MyViewHolder>
{
    Context mContext;
    int mCategoryId;
    String[] mAnimojis;
    private OnAnimojiClickListener mClickListener;
    ArrayList<String> mRecentAnimojis = new ArrayList<>();

    public AnimojiAdapter(Context context, int categoryId, String[] animojis, OnAnimojiClickListener clickListener)
    {
        this.mContext = context;
        this.mCategoryId = categoryId;
        this.mAnimojis = animojis;
        this.mClickListener = clickListener;
    }

    public AnimojiAdapter(Context context, int categoryId, ArrayList<String> recentAnimojis, OnAnimojiClickListener clickListener)
    {
        this.mContext = context;
        this.mCategoryId = categoryId;
        this.mRecentAnimojis = recentAnimojis;
        this.mClickListener = clickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView animojiTextView;
        public MyViewHolder(View v)
        {
            super(v);
            animojiTextView = (TextView) v.findViewById(R.id.animojitextview);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_animoji, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        if (mCategoryId == -1)
        {
            final String code = mRecentAnimojis.get(position);
            holder.animojiTextView.setGravity(Gravity.CENTER);
            holder.animojiTextView.setText(ZomojiParser.getInstance().parseZomojis(holder.animojiTextView, code, dpToPx(30), false));
            holder.animojiTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mClickListener != null)
                    {
                        mClickListener.onAnimojiClick(code);
                    }
                }
            });
        }else
        {
            holder.animojiTextView.setGravity(Gravity.CENTER);
            holder.animojiTextView.setText(ZomojiParser.getInstance().parseZomojis(holder.animojiTextView, mAnimojis[position], dpToPx(30), false));
            holder.animojiTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mClickListener != null)
                    {
                        mClickListener.onAnimojiClick(ZomojiParser.ANIMOJI_LISTING_CATEGORY[mCategoryId][position]);
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
            return mRecentAnimojis.size();
        }else
        {
            return mAnimojis.length;
        }
    }

    public interface OnAnimojiClickListener
    {
        void onAnimojiClick(String animoji);
    }

}

