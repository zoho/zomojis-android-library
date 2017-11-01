package com.zoho.zomojis.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ZomojiDatabase extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "Zomojis";		// No I18N
	Context context;
	private static final int DATABASE_VERSION = 1;

	public interface Tables
	{
		String ZOMOJIUSAGE = "zomojiusage";		// No I18N
	}

	public ZomojiDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//Creating all Default tables
		db.execSQL(ZomojiContract.ZomojiUsageColumns.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

}
