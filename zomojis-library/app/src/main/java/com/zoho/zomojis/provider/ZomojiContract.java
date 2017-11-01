package com.zoho.zomojis.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ZomojiContract {

	interface ZomojiUsageColumns
	{
		String PKID		=	BaseColumns._ID;
		String CODE     = 	"CODE";          										// No I18N
		String MTIME     = 	"MTIME";          										// No I18N
		String UC       = 	"UC";          					    					// No I18N


		String CREATE_TABLE = "CREATE TABLE " + ZomojiDatabase.Tables.ZOMOJIUSAGE + " ("				// No I18N
				+ PKID        + " INTEGER PRIMARY KEY AUTOINCREMENT not null, "			// No I18N
				+ CODE    	  + " TEXT, "          										// No I18N
				+ MTIME    	  + " LONG, "          										// No I18N
				+ UC          + " INTEGER ) ";  										// No I18N
	}

	public static class ZomojiUsage implements BaseColumns, ZomojiUsageColumns
	{
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ZOMOJIUSAGE).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zohochat.zomojiusage";	//No I18N

		public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.zohochat.zomojiusage";	//No I18N

		public static Uri buildZomojiUriOnId(String id)
		{
			return CONTENT_URI.buildUpon().appendPath(id).build();
		}
	}

	public static final String PATH_ZOMOJIUSAGE = "ZomojiUsage";			       	    		//No I18N

	public static String content_authority;
	public static Uri BASE_CONTENT_URI;

}
