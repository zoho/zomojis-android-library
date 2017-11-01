package com.zoho.zomojis.config;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DeviceConfig {


	static Application mAppInstance;

	public static void setContext(Application app) {
		DeviceConfig.mAppInstance = app;
	}

	public static int getDeviceWidth()
	{
		if(mAppInstance ==null)
		{
			return 0;
		}
		Display display = ((WindowManager) mAppInstance.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		return screenWidth;
	}
}
