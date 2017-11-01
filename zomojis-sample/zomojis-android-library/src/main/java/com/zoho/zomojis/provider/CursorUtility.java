package com.zoho.zomojis.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.zoho.zomojis.Zomojis;
import com.zoho.zomojis.utils.CommonUtils;

public enum CursorUtility
{
	INSTANCE;

	public Cursor executeQuery(String query, String[] projection,String selection,String[] selectionargs,String groupby,String having,String orderby,String limit) {
		SQLiteDatabase db = ZomojiContentProvider.dbHelper.getWritableDatabase();
		return db.query(query, projection, selection, selectionargs, groupby, having, orderby, limit);
	}

	public void insertorUpdateZomojiUsage(String code,long time)
	{
		Uri uri = ZomojiContract.ZomojiUsage.CONTENT_URI;
		ContentResolver resolver = CommonUtils.getContext().getContentResolver();
		ContentValues values = new ContentValues();
		values.put(ZomojiContract.ZomojiUsageColumns.CODE,code.trim());
		values.put(ZomojiContract.ZomojiUsageColumns.MTIME,time);
		Cursor cursor=null;
		try
		{
			cursor = CursorUtility.INSTANCE.executeQuery(ZomojiDatabase.Tables.ZOMOJIUSAGE, null, ZomojiContract.ZomojiUsage.CODE + "=? ", new String[]{code.trim()}, null, null, null, null);
			if (cursor.moveToNext())
			{
				int pkid = cursor.getInt(cursor.getColumnIndex(ZomojiContract.ZomojiUsageColumns.PKID));
				int uc = cursor.getInt(cursor.getColumnIndex(ZomojiContract.ZomojiUsageColumns.UC));
				values.put(ZomojiContract.ZomojiUsageColumns.UC,++uc);
				resolver.update(uri, values, ZomojiContract.ZomojiUsageColumns.PKID + "=?", new String[]{""+pkid});
			}
			else
			{
				values.put(ZomojiContract.ZomojiUsageColumns.UC,1);
				resolver.insert(uri, values);
				resolver.notifyChange(uri, null);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
		}
	}
}
