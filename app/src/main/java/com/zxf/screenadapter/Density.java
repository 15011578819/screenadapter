package com.zxf.screenadapter;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 修改density适配
 * density：表示每英寸所占有的像素点数
 *
 * 在baseActivity中onCreate()方法中setContentView()之前调用
 * 或者是在Application的
 * registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
 *             @Override
 *             public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
 *                 Density.setDensity(App.this, activity);
 * }中调用
 *
 *
 *
 * android中的dp在渲染前会将dp转为px，计算公式：
 *   density = dpi / 160;
 *   px = density * dp;
 *   px = dp * (dpi / 160);
 *   dp=px / (dpi / 160)
 * 而dpi是根据屏幕真实的分辨率和尺寸来计算的，每个设备都可能不一样的。
 */
public class Density {

    //参考设备宽  ，单位是dp
     private static final float WIDTH=320;
     //屏幕密度
    private static float appDensity;
    //字体缩放比例，默认appDensity
    private static float appScaleDensity;


    public static void setDensity(final Application application, Activity activity){
        //获取当前app的屏幕显示信息
        DisplayMetrics displayMetrics=application.getResources().getDisplayMetrics();
        if (appDensity==0){
            //初始化赋值操作
            appDensity=displayMetrics.density;
            appScaleDensity=displayMetrics.scaledDensity;


            //添加字体变化监听回调
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体发生变化，重新对scaleDensity进行赋值
                    if(newConfig!=null&&newConfig.fontScale>0){
                        appScaleDensity=application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        //计算目标值density, scaleDensity, densityDpi
        float targetDensity = displayMetrics.widthPixels / WIDTH; // 1080 / 360 = 3.0
        float targetScaleDensity = targetDensity * (appScaleDensity / appDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        //替换Activity的density, scaleDensity, densityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaleDensity;
        dm.densityDpi = targetDensityDpi;

    }
}
