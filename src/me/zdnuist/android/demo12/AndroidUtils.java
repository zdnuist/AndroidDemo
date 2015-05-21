package me.zdnuist.android.demo12;

import me.zdnuist.android.R;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class AndroidUtils {
	
	public final static String TAG = "AndroidUtils";

	public final static String SHORT_CUT_ACTION1 = "com.android.launcher.action.INSTALL_SHORTCUT";
	public final static String SHORT_CUT_ACTION2 = Intent.ACTION_CREATE_SHORTCUT;
	
	public final static String SHORT_CUT_ACTION3 = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	public static Intent des = null;

	public static void makeShortcut(Context context) {
		Intent intent = new Intent(SHORT_CUT_ACTION1);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(context.getResources(), R.drawable.ic_launcher));
		if (des == null)
			des = new Intent(context, Demo12Activity.class);

		Log.d(TAG, "des.action:"+des.getAction());
		boolean b = shortcutExists(context,
				context.getString(R.string.app_name));
		if (b)
			return;
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, des);
		intent.putExtra("duplicate", false);
		context.sendBroadcast(intent);
	}
	
	
	public static void deleteShotCut(Context context){
		Intent intent = new Intent(SHORT_CUT_ACTION2);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		
		boolean b = shortcutExists(context,
				context.getString(R.string.app_name));
		if (!b)
			return;
		
		if (des == null)
			des = new Intent(context, Demo12Activity.class);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, des);
		intent.putExtra("duplicate", false);
		context.sendBroadcast(intent);
	}

	static final String AUTHORITY = "com.android.launcher2.settings";
	static final String TABLE_FAVORITES = "favorites";
	static final String PARAMETER_NOTIFY = "notify";
	static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"
			+ TABLE_FAVORITES + "?" + PARAMETER_NOTIFY + "=true");

	static boolean shortcutExists(Context context, String title) {
		final ContentResolver cr = context.getContentResolver();
		Cursor c = cr.query(CONTENT_URI, new String[] { "title" ,"intent"}, "title=?",
				new String[] { title }, null);
		boolean result = false;
		if(c==null){
			return result;
		}
		try {
			if(c!=null&&c.moveToNext()){
				String intent = c.getString(c.getColumnIndex("intent"));
				Log.d(TAG, "intent:"+intent);
			}
			result = c.moveToFirst();
		} finally {
			if(c!=null)
			c.close();
		}
		return result;
	}

}
