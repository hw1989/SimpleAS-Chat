package org.wind.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	public static final int NET_STATUS_NONE = 0;
	public static final int NET_STATUS_WIFI = 1;
	public static final int NET_STATUS_MOBILE = 2;
    //获取链接状态
	public static int getNetStatus(Context context) {
		ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info= manager.getActiveNetworkInfo();
		if(info!=null){
			//判断是否网络是否可用，并且状态为 已连接或正在连接
			if(info.isAvailable()&&info.isConnectedOrConnecting()){
				if(info.getType()==ConnectivityManager.TYPE_WIFI){
					return NET_STATUS_WIFI;
				}else if(info.getType()==ConnectivityManager.TYPE_MOBILE){
					return NET_STATUS_MOBILE;
				} 
			}
		}
		return NET_STATUS_NONE;
	}
}
