package com.huwei.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huwei.Application.IMApplication;
import com.huwei.asmack.AsmackInit;
import com.huwei.common.Common;
import com.huwei.dialog.LoginDialog;
import com.huwei.services.IDoWork;
import com.huwei.services.IMService;
import com.huwei.services.TaskManager;
import com.huwei.view.SystemBarTintManager;

import org.wind.annotation.ActivityInject;
import org.wind.annotation.ViewInject;
import org.wind.util.PreferenceUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(id =R.id.btn_login)
    private Button btn_login;
    private Intent intent=null;
    @ViewInject(id =R.id.et_input_name )
    private EditText et_name;
    @ViewInject(id = R.id.et_input_psw)
    private  EditText et_psw;
    private ServiceConnection connection = null;
    private IMService service = null;
    // 登陆时的对话框
    private LoginDialog dialog = null;
    // private PreferenceUtils sharepreference = null;
    private TaskManager manager = null;
    private IMApplication application = null;
    private ContentResolver resolver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                IMService.SBinder sbinder = (IMService.SBinder) binder;
                service = sbinder.getService();
                AsmackInit init = service.getAsmack();
                if (init != null) {
                    if (init.getConnection().isConnected()
                            && init.getConnection().isAuthenticated()) {
                        // 已经连接并登陆成功
                        Intent intent = new Intent(LoginActivity.this,
                                IndexActivity.class);
                        startActivity(intent);
                    }
                }
                manager = sbinder.getTaskManager();
                manager.addTask(new ConnectServer());
            }
        };
        Intent intent = new Intent(this, IMService.class);
        startService(new Intent(this, IMService.class));
        application = (IMApplication) getApplication();
        bindService(intent, connection, Service.BIND_AUTO_CREATE);
        setContentView(R.layout.activity_login);
        ActivityInject.getInstance().setInject(this);
        init();
        resolver = getContentResolver();
    }
    private  void init(){
        btn_login.setOnClickListener(this);
        dialog = new LoginDialog(this, R.style.dialog);
        ActivityInject.getInstance().setInject(this);
        initSystemBar(this,R.color.translate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(et_name.getText().toString().trim().isEmpty()){
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.init(R.layout.dialog_login_tip_layout);
                        dialog.show();
                    }
                    return;
                }
                if(et_psw.getText().toString().trim().isEmpty()){
                    if (dialog != null && !dialog.isShowing()) {
                        dialog.init(R.layout.dialog_login_tip_layout);
                        dialog.show();
                    }
                    return;
                }
                manager.addTask(new LoginServer(et_name.getText().toString().trim(), et_psw.getText().toString().trim()));
                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (manager != null) {
            manager.removeAll();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection != null) {
            unbindService(connection);
        }
    }
    class ConnectServer implements IDoWork {
        @Override
        public Object doWhat() {
            service.getAsmack().startConnect();
            return null;
        }

        @Override
        public void Finish2Do(Object obj) {
            Toast.makeText(LoginActivity.this, "可以获取连接服务器的状态",
                    Toast.LENGTH_LONG).show();
        }
    }
    class LoginServer implements IDoWork {
        private String username;
        private String userpsw;

        public LoginServer(String username, String userpsw) {
            this.username = username;
            this.userpsw = userpsw;
        }

        @Override
        public Object doWhat() {
            boolean flag = service.getAsmack().setLogin(
                    this.username + Common.DomainName, this.userpsw);
            return flag;
        }

        @Override
        public void Finish2Do(Object obj) {
            Boolean flag = (Boolean) obj;
            if (obj != null && flag) {
                application.user.setJid(this.username + Common.DomainName);
                // 插入登陆用户的信息，已经做了重复的处理
                Uri uri = Uri.parse("content://com.hwant.im.login/user");
                ContentValues values = new ContentValues();
                values.put("jid", this.username + Common.DomainName);
                values.put("password", this.userpsw);
                resolver.insert(uri, values);
                // 获取登陆用户的信息
                Cursor cursor = resolver.query(uri, null, " jid=? ",
                        new String[] { this.username + Common.DomainName },
                        null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    application.user.setUserimg(cursor.getString(cursor
                            .getColumnIndex("userimg")));
                    cursor.moveToNext();
                }
                cursor.close();
                service.addConnectListener();
                // 存储当前用户的信息
                PreferenceUtils.putString(Common.SP_UserName, this.username);
                PreferenceUtils.putString(Common.SP_UserPsw, this.userpsw);
                Intent intent = new Intent(LoginActivity.this,
                        IndexActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
                Toast.makeText(LoginActivity.this, "可以获取连接服务器的状态",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "登陆失败!", Toast.LENGTH_LONG)
                        .show();
            }

        }
    }
}
