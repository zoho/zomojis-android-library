package com.zoho.zomojis.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class ZomojiContentProvider extends ContentProvider {
	
	private static UriMatcher urimatcher = null;
	public static ZomojiDatabase dbHelper;

	private static final int ZOMOJI_URI = 25;

	@Override
	public boolean onCreate() {
		final Context context = getContext();
        ZomojiContract.content_authority = context.getPackageName();
		ZomojiContract.BASE_CONTENT_URI = Uri.parse("content://" + ZomojiContract.content_authority);
		urimatcher = buildUriMatcher();
		dbHelper = new ZomojiDatabase(context);
		return true;
	}
	

	
	
	@Override
	public String getType(Uri uri)
	{
		
		final int match = urimatcher.match(uri);
		
		switch (match)
		{
			case ZOMOJI_URI:

				return ZomojiContract.ZomojiUsage.CONTENT_TYPE;
				
			default:
				break;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final int match = urimatcher.match(uri);
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		switch (match)
		{
			case ZOMOJI_URI:
			{
				db.insertOrThrow(ZomojiDatabase.Tables.ZOMOJIUSAGE, null, values);
				getContext().getContentResolver().notifyChange(uri, null);
				return ZomojiContract.ZomojiUsage.buildZomojiUriOnId(values.getAsString(BaseColumns._ID));
			}
			
		}
		return null;
	}
	
	
	@Override
	public Cursor query(Uri uri, String[] columnsToReturn, String whereClause, String[] whereClauseSubstitutions, String sortOrder)
	{
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		final int match = urimatcher.match(uri);
				
		
		switch (match)
		{
			case ZOMOJI_URI:
				return db.query(ZomojiDatabase.Tables.ZOMOJIUSAGE, null, whereClause, whereClauseSubstitutions, null, null, sortOrder);

			default:
				return null;
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = urimatcher.match(uri);
		int retVal = -1;
		switch (match)
		{
			case ZOMOJI_URI:
			{
				retVal = db.delete(ZomojiDatabase.Tables.ZOMOJIUSAGE, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
			}
			
			default:
				return retVal;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = urimatcher.match(uri);
		int retVal = -1;
		switch (match)
		{
			case ZOMOJI_URI:
				retVal = db.update(ZomojiDatabase.Tables.ZOMOJIUSAGE, values, selection, selectionArgs);
				getContext().getContentResolver().notifyChange(uri, null);
				return retVal;
				
			default:
				return -1;
		}
	}
	
	@Override
	public synchronized ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException
	{
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try
		{
			final int numOperations = operations.size();
			final ContentProviderResult[] results = new ContentProviderResult[numOperations];
			for (int i = 0; i < numOperations; i++)
			{
				results[i] = operations.get(i).apply(this, results, i);
			}
			db.setTransactionSuccessful();
			return results;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction();
			//db.close();
		}
		return null;
	}
	
	private static UriMatcher buildUriMatcher()
	{
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		final String authority = ZomojiContract.content_authority;

		matcher.addURI(authority, ZomojiContract.PATH_ZOMOJIUSAGE, ZOMOJI_URI);
		return matcher;
	}

}
