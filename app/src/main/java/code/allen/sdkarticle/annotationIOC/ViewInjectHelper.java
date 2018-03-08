package code.allen.sdkarticle.annotationIOC;

import android.app.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import code.allen.sdkarticle.annotationIOC.annotations.ContentView;
import code.allen.sdkarticle.annotationIOC.annotations.ViewInject;

/**
 * Created by allenni on 2018/3/8.
 */

public class ViewInjectHelper {
    public static void inject(Activity activity){
        injectContentView(activity);
        injectViews(activity);
    }

    /**
     * 注入布局文件
     * @param activity
     */
    private static void injectContentView(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null){
            int layoutId = contentView.value();
            try {
                // 找到并调用setContentView()方法
                Method method = clazz.getMethod("setContentView", int.class);
                method.setAccessible(true);
                method.invoke(activity,layoutId);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入view
     * @param activity
     */
    private static void injectViews(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null){
                int viewId = viewInject.value();
                try {
                    // 找到并调用findVeiwById()方法
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);
                    field.setAccessible(true);
                    // 设置成员变量
                    field.set(activity,view);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
}
