package com.huwei.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.baidu.navisdk.ui.routeguide.fsm.RouteGuideFSM;
import com.huwei.activity.R;

public class LoginDialog extends Dialog {
	private LayoutInflater inflater = null;
    private View view=null;
	public LoginDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public LoginDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoginDialog(Context context) {
		super(context);
	}

	public void init(int resid) {
		inflater = this.getLayoutInflater();
		this.view=inflater.inflate(resid, null);
		this.addContentView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// Window window= this.getWindow();
		this.setCanceledOnTouchOutside(true);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	public  void setText(int id,String text){
		if(view!=null){
			((TextView)(view.findViewById(id))).setText(text);
		}
	}
}
