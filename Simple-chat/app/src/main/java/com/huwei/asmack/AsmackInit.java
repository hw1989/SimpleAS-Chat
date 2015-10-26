package com.huwei.asmack;

import android.app.Application;
import android.content.Context;

import com.huwei.Application.IMApplication;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.wind.net.NetUtil;

/**
 * Created by wei.hu on 2015/10/23.
 */
public class AsmackInit {
    private IMApplication application = null;
    private Context context = null;
    //设置网络配置
    private ConnectionConfiguration configuration = null;
    private XMPPConnection connection = null;

    public AsmackInit(Application application) {
        this.application = (IMApplication) application;
        this.context = application.getApplicationContext();
    }
    public synchronized XMPPConnection getConnection() {
        return connection;
    }
    public XMPPConnection setConnect(String ip, int port, boolean security) {
        //判断网络状况
        if (NetUtil.getNetStatus(this.context) == NetUtil.NET_STATUS_NONE) {
            return null;
        }
        configuration = new ConnectionConfiguration(ip, port);
        //设置自动连接
        configuration.setReconnectionAllowed(true);
        //设置登录时通知服务器
        configuration.setSendPresence(true);
        configuration.setCompressionEnabled(false);
        configuration.setDebuggerEnabled(false);
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        connection = new XMPPConnection(configuration);
        //设置监听
        PacketTypeFilter typeFilter = new PacketTypeFilter(Message.class);
        connection.addPacketListener(new MyPacketListener(application,null), typeFilter);
        connection.addPacketInterceptor(new PacketInterceptor() {

            @Override
            public void interceptPacket(Packet packet) {
            }
        }, null);
        return connection;
    }
    /**
     * 开始与服务器建立连接
     */
    public boolean startConnect() {
        boolean flag = false;
        if (connection == null) {
            return flag;
        }
        try {
            connection.connect();
            if (connection.isConnected()) {
                flag = true;
                // 文件传输时的监听
                ServiceDiscoveryManager sdmanager = ServiceDiscoveryManager
                        .getInstanceFor(connection);
                if (sdmanager == null) {
                    sdmanager = new ServiceDiscoveryManager(connection);
                }
                sdmanager.addFeature("http://jabber.org/protocol/disco#info");
                sdmanager.addFeature("jabber:iq:privacy");
                FileTransferManager manager = new FileTransferManager(
                        connection);
                FileTransferNegotiator.setServiceEnabled(connection, true);
                // 设置文件传输的监听
                manager.addFileTransferListener(new ChatFileTransferListener(
                        this.application));
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 开始登陆账号
     */
    public boolean setLogin(String name, String psw) {
        boolean flag = false;
        try {
            if (connection != null && connection.isConnected()) {
                connection.login(name, psw);
                // 判断登陆后是否认证成功
                flag = connection.isAuthenticated();
                Presence presence = new Presence(Presence.Type.available);
                connection.sendPacket(presence);
                // if (flag) {
                // // 设置监听
                // Roster roster=connection.getRoster();
                // roster.addRosterListener(new
                // MyRosterListener(connection,this.context));
                // }
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
