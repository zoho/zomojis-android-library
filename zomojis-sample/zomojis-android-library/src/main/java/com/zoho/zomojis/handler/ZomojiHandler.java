package com.zoho.zomojis.handler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.zoho.zomojis.R;
import com.zoho.zomojis.adapter.AnimojiAdapter;
import com.zoho.zomojis.adapter.AnimojiPagerAdapter;
import com.zoho.zomojis.adapter.EmojiPagerAdapter;
import com.zoho.zomojis.helper.ZomojiImageSpan;
import com.zoho.zomojis.parser.ZomojiParser;
import com.zoho.zomojis.provider.CursorUtility;
import com.zoho.zomojis.utils.CommonUtils;
import com.zoho.zomojis.adapter.EmojiAdapter;
import com.zoho.zomojis.viewholder.EmojiViewHolder;

public class ZomojiHandler implements EmojiAdapter.OnEmojiClickListener,
        AnimojiAdapter.OnAnimojiClickListener
{
    private static ZomojiHandler zomojiHandler;

    Context mContext;
    View mZomojiView;
    EditText mEditText;
    int mZomojiSize;
    EmojiViewHolder mViewHolder;

    public static ZomojiHandler getInstance()
    {
        if (zomojiHandler == null)
        {
            zomojiHandler = new ZomojiHandler();
        }
        return zomojiHandler;
    }

    public static void clearInstance() {
        zomojiHandler = null;
    }

    int[] emojiCategoryIcons = new int[]
            {
                    R.drawable.vector_recent_emoji,
                    R.drawable.vector_emoji,
                    R.drawable.vector_party,
                    R.drawable.vector_sport,
                    R.drawable.vector_travel,
                    R.drawable.vector_flag
            };

    int[] animojiCategoryIcons = new int[]
            {
                    R.drawable.ic_animoji_recent,
                    R.drawable.ic_animoji_expressions,
                    R.drawable.ic_animoji_signs,
                    R.drawable.ic_animoji_celebrate,
                    R.drawable.ic_animoji_food,
                    R.drawable.ic_animoji_sports
            };

    public void init(Context context, EditText editText, int size)
    {
        mContext = context;
        mEditText = editText;
        mZomojiSize = size;

        mEditText.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mZomojiView = inflater.inflate(R.layout.emojiview, null);
        mViewHolder = new EmojiViewHolder(mZomojiView);

        mViewHolder.emojiBackspace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleDelete();
            }
        });

        mViewHolder.animojiBackspace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleDelete();
            }
        });

        // Animoji Tab
        mViewHolder.animojiViewPager.setAdapter(new AnimojiPagerAdapter(mContext));
        mViewHolder.animojiTabLayout.setupWithViewPager(mViewHolder.animojiViewPager);
        mViewHolder.animojiTabLayout.setSelectedTabIndicatorHeight(0);

        for (int i = 0; i < ZomojiParser.ANIMOJI_CATEGORIES.length + 1; i++) {
            mViewHolder.animojiTabLayout.getTabAt(i).setCustomView(R.layout.item_customtab_smiley);
        }
        for (int i = 0; i < mViewHolder.animojiTabLayout.getTabCount(); i++) {
            ImageView view = (ImageView) mViewHolder.animojiTabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_icon);
            view.setImageDrawable(CommonUtils.changeDrawableColor(animojiCategoryIcons[i], Color.parseColor("#a2a2a2"), mContext));    //No I18N
        }
        mViewHolder.animojiTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    ImageView view = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
                    Drawable drawable = view.getDrawable();
                    view.setImageDrawable(CommonUtils.changeDrawableColor(drawable, CommonUtils.getAppColor(mContext)));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ImageView view = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
                Drawable drawable = view.getDrawable();
                view.setImageDrawable(CommonUtils.changeDrawableColor(drawable, Color.parseColor("#a2a2a2"))); //No I18N
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                    ImageView view = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
                    Drawable drawable = view.getDrawable();
                    view.setImageDrawable(CommonUtils.changeDrawableColor(drawable, CommonUtils.getAppColor(mContext)));
            }
        });

        mViewHolder.animojiTabLayout.getTabAt(0).select();
        mViewHolder.animojiViewPager.setCurrentItem(0, false);


        // Smiley Tab
        mViewHolder.emojiViewPager.setAdapter(new EmojiPagerAdapter(mContext));
        mViewHolder.emojiTabLayout.setupWithViewPager(mViewHolder.emojiViewPager);
        mViewHolder.emojiTabLayout.setSelectedTabIndicatorHeight(0);
        for (int i = 0; i< ZomojiParser.EMOJI_CATEGORIES.length + 1 ; i++)
        {
            mViewHolder.emojiTabLayout.getTabAt(i).setCustomView(R.layout.item_customtab_smiley);
        }
        for (int i = 0; i < mViewHolder.emojiTabLayout.getTabCount(); i++)
        {
                ImageView view = (ImageView) mViewHolder.emojiTabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_icon);
                view.setImageDrawable(CommonUtils.changeDrawableColor(emojiCategoryIcons[i], Color.parseColor("#a2a2a2"), mContext));    //No I18N
        }
        mViewHolder.emojiTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                ImageView view = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
                Drawable drawable = view.getDrawable();
                view.setImageDrawable(CommonUtils.changeDrawableColor(drawable, CommonUtils.getAppColor(mContext)));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                ImageView view = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
                Drawable drawable = view.getDrawable();
                view.setImageDrawable(CommonUtils.changeDrawableColor(drawable,Color.parseColor("#a2a2a2"))); //No I18N
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
                ImageView view = (ImageView) tab.getCustomView().findViewById(R.id.tab_icon);
                Drawable drawable = view.getDrawable();
                view.setImageDrawable(CommonUtils.changeDrawableColor(drawable, CommonUtils.getAppColor(mContext)));
            }
        });

        mViewHolder.emojiTabLayout.getTabAt(0).select();
        mViewHolder.emojiViewPager.setCurrentItem(0,false);

        mViewHolder.emojiTabParent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mViewHolder.animojiParent.getVisibility() == View.VISIBLE)
                {
                    mViewHolder.emojiParent.setVisibility(View.VISIBLE);
                    mViewHolder.animojiParent.setVisibility(View.GONE);
                    mViewHolder.emojiTabIcon.setImageDrawable(CommonUtils.changeDrawableColor(R.drawable.vector_emoji, (CommonUtils.getAppColor(mContext)), mContext));
                    mViewHolder.animojiTabIcon.setImageDrawable(CommonUtils.changeDrawableColor(R.drawable.ic_animoji_title, Color.parseColor("#757575"), mContext)); //No I18N
                }
            }
        });

        mViewHolder.animojiTabParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewHolder.emojiParent.getVisibility() == View.VISIBLE)
                {
                    mViewHolder.animojiParent.setVisibility(View.VISIBLE);
                    mViewHolder.emojiParent.setVisibility(View.GONE);
                    mViewHolder.animojiTabIcon.setImageDrawable(CommonUtils.changeDrawableColor(R.drawable.ic_animoji_title, (CommonUtils.getAppColor(mContext)), mContext));
                    mViewHolder.emojiTabIcon.setImageDrawable(CommonUtils.changeDrawableColor(R.drawable.vector_emoji, Color.parseColor("#757575"), mContext)); //No I18N
                }
            }
        });

        mViewHolder.zomojiTabHeader.setVisibility(View.VISIBLE);
        mViewHolder.animojiParent.setVisibility(View.VISIBLE);
        mViewHolder.emojiParent.setVisibility(View.GONE);
        mViewHolder.animojiTabIcon.setImageDrawable(CommonUtils.changeDrawableColor(R.drawable.ic_animoji_title, (CommonUtils.getAppColor(mContext)), mContext));
        mViewHolder.emojiTabIcon.setImageDrawable(CommonUtils.changeDrawableColor(R.drawable.vector_emoji, Color.parseColor("#757575"), mContext)); //No I18N
    }

    public View getEmojiView() {

        return mZomojiView;
    }

    private void handleDelete() {
        int length = mEditText.getSelectionStart();
        if (length > 0)
        {
            Object spantoremove = null;
            int start = length-1, end =length;
            Editable message = mEditText.getEditableText();
            ZomojiImageSpan[] list = message.getSpans(start, end, ZomojiImageSpan.class);

            for (ZomojiImageSpan span : list) {
                int spanStart = message.getSpanStart(span);
                int spanEnd = message.getSpanEnd(span);
                if ((spanStart < end) && (spanEnd > start)) {
                    if (span instanceof ZomojiImageSpan) {
                        spantoremove = span;
                    }
                }
            }

            message = mEditText.getEditableText();
            if (spantoremove != null) {
                start = message.getSpanStart(spantoremove);
                end = message.getSpanEnd(spantoremove);
                message.removeSpan(spantoremove);
                if (start != end) {
                    message.delete(start, end);
                }
            }
            else {
                mEditText.getText().delete(length - 1, length);
            }
        }
    }

    @Override
    public void onEmojiClick(String smiley)
    {
        if(mEditText !=null) {
            if ((mEditText.getText().toString().length() == 0)) {
                mEditText.getText().append(ZomojiParser.getInstance().parseInputEmojis(smiley, mZomojiSize));
                mEditText.append(" ");     // No I18N
            } else {
                smiley = smiley + " ";   // No I18N
                if (mEditText.getSelectionStart() > 0) {
                    if (mEditText.getText().charAt(mEditText.getSelectionStart() - 1) != ' ') {
                        smiley = " " + smiley;   // No I18N
                    }
                }
                mEditText.getText().insert(mEditText.getSelectionStart(), ZomojiParser.getInstance().parseInputEmojis(smiley, mZomojiSize));
            }
            CursorUtility.INSTANCE.insertorUpdateZomojiUsage(smiley.trim(), System.currentTimeMillis());
        }

    }

    @Override
    public void onAnimojiClick(String animoji)
    {
        if(mEditText !=null)
        {
            if ((mEditText.getText().toString().length() == 0))
            {
                animoji = animoji + " ";   // No I18N
                mEditText.getText().append(ZomojiParser.getInstance().parseInputAnimojis(animoji, mEditText, mZomojiSize));
            }
            else
            {
                animoji = animoji + " ";   // No I18N
                if (mEditText.getSelectionStart() > 0) {
                    if (mEditText.getText().charAt(mEditText.getSelectionStart() - 1) != ' ') {
                        animoji = " " + animoji;   // No I18N
                    }
                }
                mEditText.getText().insert(mEditText.getSelectionStart(), ZomojiParser.getInstance().parseInputAnimojis(animoji, mEditText, mZomojiSize));
            }

            CursorUtility.INSTANCE.insertorUpdateZomojiUsage(animoji.trim(), System.currentTimeMillis());
        }
    }

}