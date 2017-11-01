package sample.zoho.zomojis;

import android.app.Application;
import com.zoho.zomojis.Zomojis;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Zomojis.register(this);
    }
}
