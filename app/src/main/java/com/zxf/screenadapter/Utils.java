package com.zxf.screenadapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 自定义像素点适配
 * 使用：
 * 直接使用设计稿的宽高，比如给定了ios尺寸比列720*1250px，直接使用px写xml
 * 自定义常用布局，重写onMeasure方法，改变子view的LayoutParams参数
 * 缺点：
 *不能在编写XML时预览
 * 低版本API无法适配
 * 需要自定义所有的容器类View
 */
public class Utils {
    private static Utils utils;

    //设计稿参考宽高
    private static  final float STANDARD_WIDTH=1080;
    private static  final float STANDARD_HEIGHT=1920;

    //屏幕显示宽高
    private int mDisplayWidth;
    private int mDisplayHeight;

    public Utils(Context context) {
        //获取屏幕的宽和高
        if (mDisplayHeight==0||mDisplayWidth==0){
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager!=null){
                DisplayMetrics displayMetrics=new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                if (displayMetrics.widthPixels>displayMetrics.heightPixels){
                    //横屏
                    mDisplayWidth=displayMetrics.heightPixels;
                    mDisplayHeight=displayMetrics.widthPixels;
                }else{
                    mDisplayHeight=displayMetrics.heightPixels;
                    mDisplayWidth=displayMetrics.heightPixels-getStatusBarHeight(context);
                }
            }
        }

    }

    private int getStatusBarHeight(Context context) {
        int resId=context.getResources().getIdentifier("status_bar_height","dimen","android");
        if (resId>0){
            return context.getResources().getDimensionPixelSize(resId);
        }
        return 0;
    }

    //获取水平方向的缩放比例
    public float getHorizontalScale(){
        return mDisplayWidth/STANDARD_WIDTH;
    }

    //获取垂直方向的缩放比例
    public float getVerticalScale(){
        return mDisplayHeight/STANDARD_HEIGHT;
    }
    public static Utils getInstance(Context context){
        if (utils!=null){
            utils=new Utils(context.getApplicationContext());
        }
        return utils;
    }
}
