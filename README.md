# Zomojis 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; This repository has a custom input view with Zomojis for Android apps.

###  Integration

```java
public class MyApplication extends Application {
  public void onCreate() {
    super.onCreate();
    Zomojis.register(this);
  }
}
```

###  Usage
* [register](#user-content-registerapplication-appinstance)
* [prepareKeyboard](#user-content-preparekeyboardcontext-context-edittext-edittext-int-zomojisize)
* [getKeyBoard](#user-content-getkeyboard)
* [getSpan](#user-content-getspantextview-textview-charsequence-text-int-zomojisize)
* [isSingleZomoji](#user-content-issinglezomoji) 
* [unregister](#user-content-unregister) 


##### register(Application appInstance) 
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Prepares the library with defaults. <br/>
```java
Zomojis.register(this);
```
##### prepareKeyboard(Context context, EditText editText, int zomojiSize)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Initializes the resources required for the Zomoji keyboard and associates Zomoji inputs to the given [`EditText`](https://developer.android.com/reference/android/widget/EditText.html). Invoke from activity's [`onCreate`](https://developer.android.com/reference/android/app/Activity.html#onCreate(android.os.Bundle)) method to avoid lag while opening keyboard.
```java
Zomojis.prepareKeyboard(myContext, myEditText, myZomojiSize); 
```

##### getKeyBoard()
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Returns the keyboard as a [`View`](https://developer.android.com/reference/android/view/View.html). <br/>
```java
View view = Zomojis.getKeyboard(this);
myKeyboardLayout.addView(view); 
```
##### getSpan(TextView textView , CharSequence text, int zomojiSize)
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Parses the given characters into Zomojis and returns a [`Spannable`](https://developer.android.com/reference/android/text/Spannable.html) applied with Zomojis.  <br/>
```java
Spannable span = Zomojis.getSpan(myTextView, myText, myZomojiSize); 
// do your your formatting to the span if you have any ...
myTextView.setText(span); 
```
***Note:*** If you want high quality Zomojis, invoke `getSpan(TextView, CharSequence, int, boolean)`[boolean = true for High quality Zomoji]. Also keep in mind that high quality Zomojis may consume more memory and may cause performance issues. <br/>

##### isSingleZomoji()
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Checks if the given text is a Zomoji. Returns ***true*** if the message is only a Zomoji.  <br/>
```java
if(Zomojis.isSingleZomoji(myText)) {
   // Single zomoji, I may go with high quality, display without background, etc.
}
else {
   // Multiple Zomojis, I may go with normal quality.
}
``` 
##### unregister()
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Releases the resources held by the library.  <br/>
```java
Zomojis.unregister();
```  
***Note:*** Invoke only from your ***LauncherActivity***'s onDestroy. Failing to invoke may cause memory leaks.