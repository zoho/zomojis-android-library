package com.zoho.zomojis;

import android.app.Application;
import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zoho.zomojis.handler.AnimojiHandler;
import com.zoho.zomojis.handler.ZomojiHandler;
import com.zoho.zomojis.config.DeviceConfig;
import com.zoho.zomojis.parser.ZomojiParser;
import com.zoho.zomojis.utils.CommonUtils;

/**
 * Initialization point for the <a href="https://github.com/zoho/zomojis-android-library">zomojis-android-library</a>.
 * <p />
 * <pre>
 *   Zomojis.register(this)
 * </pre>
 * <p />
 * For more info, see <a href="https://github.com/zoho/zomojis-android-library/blob/master/README.md">API docs</a>. <br/>
 */
public class Zomojis {

    /**
     * Prepares the library with defaults.
     * @param appInstance Application instance from Application class.
     */
    public static void register(Application appInstance)
    {
        Context context = appInstance.getApplicationContext();
        ZomojiParser.init(context);
        AnimojiHandler.init(context);
        DeviceConfig.setContext(appInstance);
        CommonUtils.init(appInstance);
    }

    /**
     * Releases the resources held by the library.
     * <p>
     * Note: Invoke only from your <b>LauncherActivity</b>'s onDestroy. Failing to do will cause memory leaks.
     * </p>
     */
    public static void unregister()
    {
        ZomojiHandler.clearInstance();
    }

    /**
     * Initializes the resources required for the Zomoji keyboard.
     * @param context Context of the activity.
     * @param editText EditText to which the Zomojis to be filled.
     * @param zomojiSize size required for Zomoji.
     */
    public static void prepareKeyboard(Context context, EditText editText, int zomojiSize)
    {
        ZomojiHandler.getInstance().init(context, editText, zomojiSize);
    }

    /**
     * @return Returns the keyboard as a {@link View}.
     */
    public static View getKeyBoard()
    {
        return  ZomojiHandler.getInstance().getEmojiView();
    }

    /**
     * Parses the given characters into Zomojis.
     * @param textView {@link TextView} to parse the data.
     * @param text data that may contain Zomoji keys.
     * @param zomojiSize size of the Zomoji to be rendered.
     * @return Returns the {@link Spannable} after adding the Zomojis.
     *
     *  <p>
     *  Note: If you want high quality Zomoji, invoke {@link #getSpan(TextView, CharSequence, int, boolean)}.
     *  <br/> Also keep in mind that HighQuality Zomojis may consume more memory and may cause performance issues.
     * </p>
     */
    public static Spannable getSpan(TextView textView , CharSequence text, int zomojiSize)
    {
        return getSpan(textView, text, zomojiSize, false);
    }

    /**
     * Parses the given characters into Zomojis.
     * @param textView {@link TextView} to parse the Zomojis.
     * @param text text that may contain Zomoji keys.
     * @param zomojiSize size of the Zomoji to be rendered.
     * @param isHighResolution (true/false).
     * <br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|_______  Normal quality animoji<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    &nbsp;|____________ High quality animoji<br/>.
     * @return Returns the {@link Spannable} after adding the Zomojis.
     * <p>
     *  Note: Keep in mind that HighQuality Zomojis may consume more memory and may cause performance issues.
     * </p>
     */
    public static Spannable getSpan(TextView textView , CharSequence text, int zomojiSize, boolean isHighResolution)
    {
        return ZomojiParser.getInstance().parseZomojis(textView, text, zomojiSize, isHighResolution);
    }

    /**
     * Checks if the given text is a Zomoji.
     * @param text text to be checked.
     * @return Returns true if the message is only a Zomoji.
     */
    public static Boolean isSingleZomoji(String text)
    {
        return ZomojiParser.getInstance().isSingleZomoji(text);
    }
}
