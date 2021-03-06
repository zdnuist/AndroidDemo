package me.zdnuist.android.demo12;

import me.zdnuist.android.BaseActivity;
import me.zdnuist.android.R;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

public class AndroidUtils {

	public final static String TAG = "AndroidUtils";

	public final static String SHORT_CUT_ACTION1 = "com.android.launcher.action.INSTALL_SHORTCUT";
	public final static String SHORT_CUT_ACTION2 = Intent.ACTION_CREATE_SHORTCUT;

	public final static String SHORT_CUT_ACTION3 = "com.android.launcher.action.UNINSTALL_SHORTCUT";


	public static void makeShortcut(Context context, Class<? extends BaseActivity> cls, int id) {
		Intent intent = new Intent(SHORT_CUT_ACTION1);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(id));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(context.getResources(), R.drawable.ic_launcher));

		boolean b = shortcutExists(context, context.getString(id));
		if (b)
			return;
		Intent des  = new Intent(context, cls);
		Log.d(TAG, "des.action:" + des.getAction());
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, des);
		intent.putExtra("duplicate", false);
		context.sendBroadcast(intent);
	}
	
	public static void makeShortcut2(Context context, Class<? extends BaseActivity> cls, int id) {
		Intent intent = new Intent(SHORT_CUT_ACTION1);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(id));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
				.decodeResource(context.getResources(), R.drawable.ic_launcher));

		boolean b = shortcutExists(context, context.getString(id));
		if (b)
			return;
		ComponentName component = new ComponentName(context.getPackageName(), ".demo12.Demo12Activity");
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent().setComponent(component));
		intent.putExtra("duplicate", false);
		context.sendBroadcast(intent);
	}

	public static void deleteShotCut(Context context,
			Class<? extends BaseActivity> cls, int id) {
		Intent intent = new Intent(SHORT_CUT_ACTION3);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(id));

		boolean b = shortcutExists(context, context.getString(id));
		if (!b)
			return;

		Intent des  = new Intent(context, cls);
		des.setAction(Intent.ACTION_VIEW);
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
		Cursor c = cr.query(CONTENT_URI, new String[] { "title", "intent" },
				"title=?", new String[] { title }, null);
		boolean result = false;
		if (c == null) {
			return result;
		}
		try {
			if (c != null && c.moveToNext()) {
				String intent = c.getString(c.getColumnIndex("intent"));
				Log.d(TAG, "intent:" + intent);
			}
			result = c.moveToFirst();
		} finally {
			if (c != null)
				c.close();
		}
		return result;
	}

}
