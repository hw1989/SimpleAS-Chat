package com.huwei.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.huwei.Application.IMApplication;
import com.huwei.common.Common;
import com.huwei.services.IMService;
import com.huwei.services.TaskManager;
import com.huwei.view.SystemBarTintManager;

import org.wind.util.FileUtils;
import org.wind.util.StringHelper;

/**
 * Created by wei.hu on 2015/10/23.
 */
public class BaseActivity extends FragmentActivity {
    SystemBarTintManager tintManager = null;
    private ServiceConnection connection = null;
    public IMService service = null;
    public TaskManager manager = null;
    // 标记是否绑定
    public IMApplication application = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        application = (IMApplication) getApplication();
        createPath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null) {
            unbindService(connection);
        }
    }

    public static void initSystemBar(Activity activity, int colorid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
// 使用颜色资源
        tintManager.setStatusBarTintResource(colorid);
//        if (Build.VERSION.SDK_INT >= 19)
//        {
//            ViewGroup group=(ViewGroup)activity.getWindow().getDecorView();
//            View view=new View();
//            // 透明状态栏
////            activity.getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void bindService() {
        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                IMService.SBinder sbinder = (IMService.SBinder) binder;
                service = sbinder.getService();
                manager = sbinder.getTaskManager();
                bindFinished(manager);
            }
        };
        Intent intent = new Intent(this, IMService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void createPath() {
        // 创建缓存文件路径
        FileUtils.createorexistsPath(Environment.getExternalStorageDirectory()
                + Common.Path_Cache, true);
        // 创建目录文件路径
        FileUtils.createorexistsPath(Environment.getExternalStorageDirectory()
                + Common.Path_Image, true);
        // 创建媒体的文件的路径
        FileUtils.createorexistsPath(Environment.getExternalStorageDirectory()
                + Common.Path_Media, true);
    }

    public void showToast(String text) {
        if (!StringHelper.isEmpty(text)) {
            Toast.makeText(getApplicationContext(), text,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void bindFinished(TaskManager manager) {

    }
}
