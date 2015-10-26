package com.huwei.Application;

import java.io.File;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.carbons.Carbon.Private;
import org.wind.util.PreferenceUtils;

import cn.bmob.v3.Bmob;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.huwei.common.Common;
import com.huwei.db.MySQLiteOpenHelper;
import com.huwei.entity.UserInfo;

import android.app.Application;
import android.app.Service;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Vibrator;
/**
 * Created by wei.hu on 2015/10/23.
 */
public class IMApplication extends Application implements BDLocationListener {
    private PreferenceUtils sharepreference = null;
    // 标记是否创建过数据库
    public MySQLiteOpenHelper helper = null;
    // 登陆人的信息
    public UserInfo user = null;
    // -------------------百度地图初始化------------------------
    public LocationClient mLocationClient = null;
    public GeofenceClient mGeofenceClient = null;
    public Vibrator mVibrator = null;
    // 定位系统的消息内容
    private BDLocation bdLocation = null;

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SmackAndroid.init(this);
        //初始化bmob
        BmobConfiguration config = new BmobConfiguration.Builder(this).customExternalCacheDir("hwchat").build();
        BmobPro.getInstance(this).initConfig(config);
        Bmob.initialize(getApplicationContext(),"c982ca7349ee6fdf2fcfe57d50e71e0a");
        user = new UserInfo();
        sharepreference = PreferenceUtils.init(this, Common.IM_Config);
        helper = new MySQLiteOpenHelper(this);
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir() + File.separator + Common.DB_File_Name, null);
        helper.onCreate(database);
        sharepreference.putBoolen("database", true);
        // -----------------------------------------------
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(this);
        mGeofenceClient = new GeofenceClient(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(
                Service.VIBRATOR_SERVICE);
    }

    @Override
    public void onReceiveLocation(BDLocation arg0) {
        this.bdLocation = arg0;
    }

}