package me.zdnuist.android.demo6;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.zdnuist.android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;

/**
 * 获取指定Activity的截屏，保存到jpg文件
 */
public class ScreenShot {

	private static final String TAG = "ScreenShot";
	// 获取屏幕长和高
	private static int screen_width;
	private static int screen_height;

	// 获取指定Activity的截屏，保存到png文件
	private static Bitmap takeScreenShot(Activity activity) {
		// View是你需要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 去掉标题栏
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);

		int height = screen_height - statusBarHeight;

		// // 定义预转换成的图片的宽度和高度
		// int dstHeight = (int) (dstWidth * height * 1.0 / screen_width);
		// // 计算缩放率，新尺寸除原始尺寸
		// float sx = ((float) dstWidth) / screen_width;
		// float sy = ((float) dstHeight) / height;
		// // 创建操作图片用的matrix对象
		// Matrix matrix = new Matrix();
		// // 缩放图片动作
		// matrix.postScale(sx, sy);
		// // 创建新的图片
		// Bitmap newbm = Bitmap.createBitmap(b1, 0, statusBarHeight,
		// screen_width, height, matrix, true);

		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, screen_width,
				height);
		view.destroyDrawingCache();
		return b;
	}

	/**
	 * 讲文字绘制到bitmap内
	 * 
	 * @param text
	 * @param srcmap
	 * @return
	 */
	@SuppressLint("ResourceAsColor")
	public static Bitmap drawTextInBitmap(Context mContext, String text1,
			String text2, int imgSrc) {
		Bitmap bitmap = null;
		try {
			Bitmap srcmap = BitmapFactory.decodeResource(
					mContext.getResources(), imgSrc);
			bitmap = Bitmap.createBitmap(srcmap.getWidth(), srcmap.getHeight(),
					srcmap.getConfig());
			float scale = srcmap.getWidth() / 450f;
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(srcmap, 0, 0, null);
			Paint paint = new Paint();
			paint.setFakeBoldText(true);
			// 画出百分比
			paint.setColor(Color.rgb(82, 246, 109));
			paint.setTextSize(18 * scale);
			canvas.drawText(text1, (125 - 8) * scale, (145 + 16 + 10) * scale,
					paint);

			// 画出分数
			// 因为分数的长度是不规则的，所以根据长度的不同 动态改变字体的大小，以达到适应屏幕的效果
			float length = text2.length();
			float textScale = 66 * 2.0f / length;
			paint.setFakeBoldText(false);
			paint.setTextSize((int) textScale * scale);
			canvas.drawText(text2, (340 - 6) * scale, (107 + 52 + 10) * scale,
					paint);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			makeKSDir();
			savePic(bitmap,
					IMAGE_SAVE_PATH + File.separator + sdf.format(new Date())
							+ ".jpg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 把Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 将两个Bitmap合并成一个图片
	 * 
	 * @param firstBitmap
	 * @param secondBitmap
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
		Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(),
				firstBitmap.getHeight() + secondBitmap.getHeight(),
				secondBitmap.getConfig());
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(firstBitmap, 0, 0, null);
		canvas.drawBitmap(secondBitmap, 0, firstBitmap.getHeight(), null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		// // 计算缩放率，新尺寸除原始尺寸
		// float sx = 1.0f;
		// float sy = ((float) secondBitmap.getHeight()) / bitmap.getHeight();
		// // 创建操作图片用的matrix对象
		// Matrix matrix = new Matrix();
		// // 缩放图片动作
		// float scale;
		// if(screen_height <= 900){
		// scale = 0.75f;
		// }else{
		// scale = 0.82f;
		// }
		// matrix.postScale(scale, scale);//0.82为满足要求呕心沥血配出来的比例～～～～～～
		// //创建新的图片
		// return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		// bitmap.getHeight(), matrix, true);

		return bitmap;
	}


	// 保存到sdcard
	private static void savePic(final Bitmap b, final String strFileName) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 分享图片的最底层图片生成
	 * 
	 * @param activity
	 * @return
	 */
//	private static Bitmap getTop(Activity activity) {
//		Bitmap share_logo = BitmapFactory.decodeResource(
//				activity.getResources(), R.drawable.share_logo);
//		Bitmap share_word = BitmapFactory.decodeResource(
//				activity.getResources(), R.drawable.share_word);
//		Bitmap share_qcode = BitmapFactory.decodeResource(
//				activity.getResources(), R.drawable.share_qcode);
//
//		int bitmap_height = 110;
//		int cell_width = (screen_width - share_logo.getWidth()
//				- share_qcode.getWidth() - share_word.getWidth()) / 6;
//		Bitmap top = Bitmap.createBitmap(screen_width, bitmap_height,
//				Bitmap.Config.RGB_565);
//		Canvas canvas = new Canvas(top);
//		canvas.drawColor(Color.WHITE);
//		canvas.drawBitmap(share_logo, cell_width,
//				(bitmap_height - share_logo.getHeight()) / 2, null);
//		canvas.drawBitmap(share_word, cell_width * 3 + share_logo.getWidth(),
//				(bitmap_height - share_word.getHeight()) / 2, null);
//		canvas.drawBitmap(share_qcode, cell_width * 5 + share_logo.getWidth()
//				+ share_word.getWidth(),
//				(bitmap_height - share_qcode.getHeight()) / 2, null);
//		canvas.save(Canvas.ALL_SAVE_FLAG);
//		canvas.restore();
//		return top;
//	}

	/**
	 * 程序入口
	 * 
	 * @param activity
	 * @return
	 */
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public static Bitmap shootScreen(Activity activity) {
		try {
			screen_width = activity.getWindowManager().getDefaultDisplay()
					.getWidth();
			screen_height = activity.getWindowManager().getDefaultDisplay()
					.getHeight();
//			Bitmap firstBitmap = getTop(activity);
			Bitmap secondBitmap = takeScreenShot(activity);
//			Bitmap bitmap = mergeBitmap(firstBitmap, secondBitmap);

			makeKSDir();

			savePic(secondBitmap,
					IMAGE_SAVE_PATH + File.separator + sdf.format(new Date())
							+ ".jpg");
			return secondBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转换bitmap 为byte
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] getBytesFromBitmap(Bitmap bm) {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		try {
			int height = bm.getHeight();
			int width = bm.getWidth();
			int[] rgbs = new int[width * height];
			bm.getPixels(rgbs, 0, width, 0, 0, width, height);

			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			for (int i = 0; i < rgbs.length; i++) {
				if (rgbs[i] != -1) {
					dos.writeInt(rgbs[i]);
				}
			}
			baos.flush();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static String IMAGE_SAVE_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "zdnuist"
			+ File.separator + "cache" ;

	public static void makeKSDir() {
		File dir = new File(IMAGE_SAVE_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

}