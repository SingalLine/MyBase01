
package com.rilin.lzy.mybase.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.rilin.lzy.mybase.R;

/**
 * 视图操作类 截图 、抖动视图
 * 
 * @author cate 2014-12-6 上午11:41:26
 */

public class ViewUtils
{
	/**
	 * 截取该view的显示的视图
	 * 
	 * @param v
	 *            需要进行截图的控件
	 * @return 该控件截图的Bitmap对象。
	 */
	public static Bitmap captureView(View v)
	{
		v.destroyDrawingCache();
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return v.getDrawingCache();
	}
	
	/**
	 * 创建快捷方式
	 * 
	 * @param cxt
	 *            Context
	 * @param icon
	 *            快捷方式图标
	 * @param title
	 *            快捷方式标题
	 * @param cls
	 *            要启动的类
	 */
	public void createDeskShortCut(Context cxt, int icon, String title, Class<?> cls)
	{
		// 创建快捷方式的Intent
		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		shortcutIntent.putExtra("duplicate", false);
		// 需要现实的名称
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 快捷图片
		Parcelable ico = Intent.ShortcutIconResource.fromContext(cxt.getApplicationContext(), icon);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
		Intent intent = new Intent(cxt, cls);
		// 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// 点击快捷图片，运行的程序主入口
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 发送广播。OK
		cxt.sendBroadcast(shortcutIntent);
	}
	
	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param aty
	 * @return
	 */
	public static Bitmap snapShotWithStatusBar(Activity aty)
	{
		View view = aty.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = DensityUtil.getScreenW(aty);
		int height = DensityUtil.getScreenH(aty);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;
	}
	
	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param aty
	 * @return
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity aty)
	{
		View view = aty.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		aty.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		int width = DensityUtil.getScreenW(aty);
		int height = DensityUtil.getScreenH(aty);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}
	
	/**
	 * 抖动窗口
	 * 
	 * @param context
	 * @param view
	 */
	public static void shakeView(Context context, View view)
	{
		if (view != null)
		{
			Animation shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
			view.startAnimation(shakeAnimation);
		}
	}
	
	/**
	 * 抖动控件并弹出指定msg
	 * 
	 * @param context
	 * @param view
	 * @param msg
	 */
	public static void shakeViewAndToast(Context context, View view, String msg)
	{
		shakeView(context, view);
		ToastUtils.showLongToast(msg);
		
	}
	
	/**
	 * 缩放和渐显动画 常用与点赞
	 * 
	 * @param view
	 * @param listener
	 */
	public static void alphaAndScale(View view, AnimatorListener listener)
	{
		alphaAndScale(view, 500, listener);
	}
	
	public static void alphaAndScale(View view, long duration)
	{
		alphaAndScale(view, duration, null);
	}
	
	/**
	 * 缩放和渐显动画 常用于点赞 默认duration500
	 * 
	 * @param view
	 */
	public static void alphaAndScale(View view)
	{
		alphaAndScale(view, 500);
	}
	
	/**
	 * 缩放和渐显动画 常用于点赞
	 * 
	 * @param view
	 * @param duration
	 * @param listener
	 */
	public static void alphaAndScale(View view, long duration, AnimatorListener listener)
	{
		PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.5f, 1f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.4f, 1f);
		PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.4f, 1f);
		ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, pvhY, pvhZ);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.setDuration(duration).start();
		if (listener != null)
		{
			animator.addListener(listener);
		}
	}
	
	/**
	 * 获取StateListDrawable
	 * 
	 * @param normal
	 * @param active
	 * @param disable
	 * @return
	 */
	public static StateListDrawable getStateDrawable(Context context, int normal, int active, int disable)
	{
		StateListDrawable states = new StateListDrawable();
		if (active != -1)
		{
			states.addState(new int[]
			{
				android.R.attr.state_pressed
			}, context.getResources().getDrawable(active));
			states.addState(new int[]
			{
				android.R.attr.state_focused
			}, context.getResources().getDrawable(active));
		}
		if (disable != -1)
		{
			states.addState(new int[]
			{
				-android.R.attr.state_enabled
			}, context.getResources().getDrawable(disable));
		}
		if (normal != -1)
		{
			states.addState(new int[] {}, context.getResources().getDrawable(normal));
		}
		
		return states;
	}
	
	/**
	 * 根据view获取view的bitmap
	 * 
	 * @param context
	 * @param paramView
	 * @return
	 */
	public static Bitmap fromView(Context context, View paramView)
	{
		paramView.destroyDrawingCache();
		paramView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
		paramView.layout(0, 0, paramView.getMeasuredWidth(), paramView.getMeasuredHeight());
		paramView.setDrawingCacheEnabled(true);
		Bitmap localBitmap = paramView.getDrawingCache(true);
		return localBitmap;
	}
}
