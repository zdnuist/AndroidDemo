package me.zdnuist.android.demo9.util;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 本地媒体文件工具了
 * 
 * @author zdnuist
 * 
 */
public class MediaUtils {

	public static final String TAG = "MediaUtils";

	/**
	 * 搜索媒体文件
	 * 
	 * @param mContext
	 */
	public static void scanMediaFiles(Context mContext) {
		// 4.4以前可以用MEDIA_MOUNTED 4.4以后把它变为系统权限了 不能用
		if (android.os.Build.VERSION.SDK_INT < 19) {
			mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
					.parse("file://"
							+ Environment.getExternalStorageDirectory()
									.getAbsolutePath())));
		} else {
			MediaScannerConnection.scanFile(mContext,
					new String[] { Environment.getExternalStorageDirectory()
							.getAbsolutePath() + File.separator }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
						}
					});
		}
	}

	/**
	 * 获取SdCard多媒体信息结合
	 */
	public static Map<String, String> mediaList = new LinkedHashMap<String, String>();
	static{
		mediaList.put("系统默认铃声", null);
	}
	public static Map<String, String> getAllMediaList(Context context,
			String selection) {
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.TITLE,
							MediaStore.Audio.Media.DATA, }, selection, null,
					MediaStore.Audio.Media.DATE_ADDED + " DESC");
			if (cursor == null) {
				Log.d(TAG, "The getMediaList cursor is null.");
				return mediaList;
			}
			int count = cursor.getCount();
			if (count <= 0) {
				Log.d(TAG, "The getMediaList cursor count is 0.");
				return mediaList;
			}
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID));
				String title = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String path = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA));
				mediaList.put(id + "#" + title, path);
				
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return mediaList;
	}

}
