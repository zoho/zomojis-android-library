package com.zoho.zomojis.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zoho.zomojis.R;
import com.zoho.zomojis.config.DeviceConfig;
import com.zoho.zomojis.handler.ZomojiHandler;
import com.zoho.zomojis.parser.ZomojiParser;
import com.zoho.zomojis.provider.CursorUtility;
import com.zoho.zomojis.utils.CommonUtils;

import java.util.ArrayList;

public class AnimojiPagerAdapter extends PagerAdapter
{
    private Context mContext;
    private String tabTitles[] = new String[6];
    ArrayList<String> mRecentAnimojis;

    public AnimojiPagerAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ViewHolder holder;
        holder = onCreateHolder(container);
        onBindHolder(holder, position);
        holder.itemView.setTag(position);
        container.addView(holder.itemView);
        return holder.itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    public ViewHolder onCreateHolder(ViewGroup container)
    {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_smiley_category, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindHolder(ViewHolder holder, final int position)
    {
        AnimojiAdapter animojiAdapter;
        if (position == 0)
        {
            mRecentAnimojis = CommonUtils.getRecentAnimoji(30);
            if (mRecentAnimojis.size() == 0)
            {
                addFrequentAnimojis();
                mRecentAnimojis = CommonUtils.getRecentAnimoji(30);
            }

            animojiAdapter = new AnimojiAdapter(mContext,-1, mRecentAnimojis, ZomojiHandler.getInstance());
        }else
        {
            animojiAdapter = new AnimojiAdapter(mContext,position - 1, ZomojiParser.ANIMOJI_LISTING_CATEGORY[position - 1], ZomojiHandler.getInstance());
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, DeviceConfig.getDeviceWidth() / (CommonUtils.dpToPx(45)));
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(animojiAdapter);
    }

    @Override
    public int getCount()
    {
        return tabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view.equals(object);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles[position];
    }
    public class ViewHolder
    {
        public View itemView;
        public RecyclerView recyclerView;

        ViewHolder(View parent)
        {
            itemView = parent;
            recyclerView = (RecyclerView) itemView.findViewById(R.id.smileygrid);
        }
    }

    private void addFrequentAnimojis()
    {
        long currenttime = System.currentTimeMillis();
        for(String emoji : ZomojiParser.FREQUENT_ANIMOJI)
        {
            CursorUtility.INSTANCE.insertorUpdateZomojiUsage(emoji.trim(),currenttime);
            currenttime = currenttime - 1;
        }
    }
}
