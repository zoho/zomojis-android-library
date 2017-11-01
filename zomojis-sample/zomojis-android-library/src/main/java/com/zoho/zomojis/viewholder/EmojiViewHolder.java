package com.zoho.zomojis.viewholder;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zoho.zomojis.R;

public class EmojiViewHolder
{
    public LinearLayout zomojiTabHeader;
    public ImageButton emojiTabIcon, animojiTabIcon;

    public LinearLayout emojiParent;
    public LinearLayout animojiParent;

    public ViewPager emojiViewPager;
    public ViewPager animojiViewPager;
    public TabLayout emojiTabLayout;
    public TabLayout animojiTabLayout;

    public RelativeLayout emojiBackspace;
    public RelativeLayout animojiBackspace;
    public RelativeLayout emojiFrontLayout;
    public RelativeLayout animojiFrontLayout;
    public RelativeLayout animojiTabParent;
    public RelativeLayout emojiTabParent;

    public EmojiViewHolder(View emojiView)
    {
        zomojiTabHeader = (LinearLayout) emojiView.findViewById(R.id.zomojitabheader);
        emojiTabIcon = (ImageButton) emojiView.findViewById(R.id.emojitabicon);
        animojiTabIcon = (ImageButton) emojiView.findViewById(R.id.animojitabicon);

        emojiParent = (LinearLayout) emojiView.findViewById(R.id.emojiparent);
        animojiParent = (LinearLayout) emojiView.findViewById(R.id.animojiparent);

        emojiViewPager = (ViewPager) emojiView.findViewById(R.id.emojiviewpager);
        animojiViewPager = (ViewPager) emojiView.findViewById(R.id.animojiviewpager);
        emojiTabLayout = (TabLayout) emojiView.findViewById(R.id.emoji_sliding_tabs);
        animojiTabLayout = (TabLayout) emojiView.findViewById(R.id.animoji_sliding_tabs);

        emojiBackspace = (RelativeLayout) emojiView.findViewById(R.id.emojibacklayout);
        animojiBackspace = (RelativeLayout) emojiView.findViewById(R.id.animojibacklayout);
        emojiFrontLayout = (RelativeLayout) emojiView.findViewById(R.id.emojifrontlayout);
        animojiFrontLayout = (RelativeLayout) emojiView.findViewById(R.id.animojifrontlayout);

        animojiTabParent = (RelativeLayout) emojiView.findViewById(R.id.animojitabparent);
        emojiTabParent = (RelativeLayout) emojiView.findViewById(R.id.smileytabparent);
    }
}
