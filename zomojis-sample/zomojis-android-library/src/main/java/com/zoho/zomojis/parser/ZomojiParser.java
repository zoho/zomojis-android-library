package com.zoho.zomojis.parser;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoho.zomojis.model.Animoji;
import com.zoho.zomojis.handler.AnimojiHandler;
import com.zoho.zomojis.helper.ZomojiImageSpan;
import com.zoho.zomojis.utils.ZomojiInitUtils;

public class ZomojiParser {

    private static ZomojiParser sInstance;
    private Context mContext = null;

    private ZomojiParser(Context context)
    {
        mContext = context;
    }

    public static ZomojiParser getInstance()
    {
        return sInstance;
    }
    public static void init(Context context)
    {
        sInstance = new ZomojiParser(context);
    }

    public static final ArrayList<String> FREQUENT_ANIMOJI = ZomojiInitUtils.initFrequentAnimojis();

    public static final ArrayList<String> FREQUENT_EMOJI = ZomojiInitUtils.initFrequentEmojis();

    private final String[] mEmojiKeys = ZomojiInitUtils.initEmojiKeys();

    private final String[] mAnimojiKeys = ZomojiInitUtils.initAnimojiKeys();

    public static final int[] DEFAULT_EMOJI_RES_IDS = ZomojiInitUtils.initEmojiResources();

    public static final String[] EMOJI_CATEGORIES = ZomojiInitUtils.initEmojiCategories();

    public static final String[] ANIMOJI_CATEGORIES = ZomojiInitUtils.initAnimojiCategories();

    public static final String[][] EMOJI_LISTING_CATEGORY = ZomojiInitUtils.initEmojiListingCategory();

    public static final String[][] ANIMOJI_LISTING_CATEGORY = ZomojiInitUtils.initAnimojiListingCategory();

    public static final int[][] EMOJI_LISTING_CATEGORY_ID = ZomojiInitUtils.initEmojiListingCategoryID();

    private Pattern mPattern = buildPattern();

    private final HashMap<String, Integer> mSmileyToRes = buildSmileyToRes();

    private final Pattern mKeyboardPattern = buildKeyboardPattern();
    private final HashMap<String, Integer> mKeyboardSmileyToRes = buildKeyboardSmileyToRes();

    private Pattern buildPattern()
    {

        StringBuilder patternString = new StringBuilder(mEmojiKeys.length * 3);
        patternString.append("(^|[ \\n .])");					// No I18N
        patternString.append('(');
        for (String s : mEmojiKeys)
        {
            if(s.equalsIgnoreCase("-.-"))
            {
                patternString.append("-\\.-");  // No I18N
            }
            else
            {
                patternString.append(getEscapedString(s));
            }

            patternString.append('|');
        }

        for (String s : mAnimojiKeys)
        {
            patternString.append(getEscapedString(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        patternString.append("(?=$|[ \\n .])");					// No I18N
        return Pattern.compile(patternString.toString());
    }


    private Pattern buildKeyboardPattern()
    {
        StringBuilder patternString = new StringBuilder(getCategoryEmojiLength());
        patternString.append("(^|[ \\n .])");					// No I18N
        patternString.append('(');
        for(String[] categories : EMOJI_LISTING_CATEGORY)
        {
            for (String s : categories)
            {
                patternString.append(getEscapedString(s));
                patternString.append('|');
            }
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        patternString.append("(?=$|[ \\n .])");					// No I18N
        return Pattern.compile(patternString.toString());
    }

    public String getEscapedString(String str)
    {
        try
        {
            String str1 = str.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)").replaceAll("\\|", "\\\\|").replaceAll("\\+", "\\\\+").replaceAll("\\*", "\\\\*").replaceAll("\\?", "\\\\?").replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]").replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}");
            return str1;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return str;
        }
    }

    private HashMap<String, Integer> buildSmileyToRes()
    {
        if (DEFAULT_EMOJI_RES_IDS.length != mEmojiKeys.length)
        {
            throw new IllegalStateException("Smiley resource ID/text mismatch");								// No I18N
        }
        HashMap<String, Integer> smileyToRes = new HashMap(mEmojiKeys.length);
        for (int i = 0; i < mEmojiKeys.length; i++)
        {
            smileyToRes.put(mEmojiKeys[i], DEFAULT_EMOJI_RES_IDS[i]);
        }
        return smileyToRes;
    }

    private HashMap<String, Integer> buildKeyboardSmileyToRes()
    {
        int emojilength = getCategoryEmojiLength();
        if (emojilength != getCategoryEmojiIDLength())
        {
            throw new IllegalStateException("Smiley resource ID/text mismatch");								// No I18N
        }
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(emojilength);
        for (int i = 0 ; i < EMOJI_CATEGORIES.length ; i++)
        {
            String[] category = EMOJI_LISTING_CATEGORY[i];
            int[] categoryid = EMOJI_LISTING_CATEGORY_ID[i];
            for (int j =0; j < category.length ; j++)
            {
                smileyToRes.put(category[j],categoryid[j]);
            }
        }

        return smileyToRes;
    }

    public Spannable parseInputEmojis(CharSequence text, int size)
    {
        SpannableStringBuilder builder = null;
        try
        {
            builder = new SpannableStringBuilder(text);
            Matcher matcher = mKeyboardPattern.matcher(text.toString());
            while (matcher.find())
            {
                int resId = mKeyboardSmileyToRes.get(matcher.group().trim().replace("!",""));
                Drawable myIcon = mContext.getDrawable(resId);
                myIcon.setBounds(0, 0, size, size);

                myIcon.setBounds(0, 0, size, size);


                builder.setSpan(new ZomojiImageSpan(myIcon),matcher.start()+matcher.group(1).length(),matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return builder;
        }
        catch(Exception e)
        {
            return builder;
        }
    }

    public Spannable parseInputAnimojis(CharSequence text, EditText edit, int size)
    {
        SpannableStringBuilder builder = null;
        try {
            builder = new SpannableStringBuilder(text);
            Matcher matcher = mPattern.matcher(text.toString());

            while (matcher.find())
            {

                if (matcher.group().contains("!"))
                {
                    int bounds = size;

                    int start = edit.getSelectionStart();
                    int end = edit.getSelectionStart()+text.length()-1;

                    String name = AnimojiHandler.getInstance().getFileNameFromPattern(matcher.group().trim().replace("!", "").replace(":", ""));  // No I18N
                    Animoji animoji = AnimojiHandler.getInstance().getAnimoji(name, bounds, edit, false, start, end);

                    builder.setSpan(new ZomojiImageSpan(animoji.getDrawable()),
                            matcher.start() + matcher.group(1).length(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return builder;
    }

    public Spannable parseZomojis(TextView textView , CharSequence text, int bounds, boolean isSingle)
    {
        SpannableStringBuilder builder = null;
        try
        {
            builder = new SpannableStringBuilder(text);
            Matcher matcher = mPattern.matcher(text.toString());

            while (matcher.find())
            {
                if(matcher.group().contains("!"))
                {
                    try
                    {
                        if(textView!=null)
                        {
                            int start = matcher.start() + matcher.group(1).length();
                            int end = matcher.end();

                            String name = AnimojiHandler.getInstance().getFileNameFromPattern(matcher.group().trim().replace("!", "").replace(":", ""));   // No I18N
                            Animoji animoji = AnimojiHandler.getInstance().getAnimoji(name, bounds, textView, isSingle, start, end);

                            builder.setSpan(new ZomojiImageSpan(animoji.getDrawable()),
                                    start, end,
                                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Drawable myIcon = null;
                    int resId = mSmileyToRes.get(matcher.group().trim());

                    myIcon = mContext.getDrawable(resId);
                    myIcon.setBounds(0, 0, bounds, bounds);
                    if (textView != null)
                    {
                        builder.setSpan(new ZomojiImageSpan(myIcon),
                                matcher.start() + matcher.group(1).length(), matcher.end(),
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                    else
                    {
                        builder.setSpan(new ZomojiImageSpan(myIcon),
                                matcher.start() + matcher.group(1).length(), matcher.end(),
                                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                }
            }

            return builder;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return builder;
        }
    }

    public Boolean isSingleZomoji(String text)
    {
        Boolean isSingle = false;
        text = text.toString().trim();
        try
        {
            for(String animoji:mAnimojiKeys)
            {
                if(animoji.equals(text))
                {
                    isSingle = true;
                    break;
                }
            }

            if(!isSingle)
            {
                for(String emoji:mEmojiKeys)
                {
                    if(emoji.equals(text))
                    {
                        isSingle = true;
                        break;
                    }
                }
            }

            return isSingle;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return isSingle;
        }
    }

    private int getCategoryEmojiLength()
    {
        int total = 0;
        for (int category = 0; category < EMOJI_CATEGORIES.length; category++)
        {
            total = total + EMOJI_LISTING_CATEGORY[category].length;
        }
        return total;
    }

    private int getCategoryEmojiIDLength()
    {
        int total = 0;
        for (int category = 0; category < EMOJI_CATEGORIES.length; category++)
        {
            total = total + EMOJI_LISTING_CATEGORY_ID[category].length;
        }
        return total;
    }

    public int getEmojiResID(String emoji)
    {
        int resid = 0;
        try
        {
            ArrayList<String> codes = new ArrayList(Arrays.asList(mEmojiKeys));
            int pos = 0;
            for (int i = 0; i < codes.size() ; i++)
            {
                String code = codes.get(i);
                if (code.equals(emoji))
                {
                    pos = i;
                    break;
                }
            }
            resid = DEFAULT_EMOJI_RES_IDS[pos];
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return resid;

    }
}
