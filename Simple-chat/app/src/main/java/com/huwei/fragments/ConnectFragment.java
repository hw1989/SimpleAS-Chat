package com.huwei.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;
import org.wind.util.StringHelper;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.huwei.Application.IMApplication;
import com.huwei.activity.ChatActivity;
import com.huwei.activity.IndexActivity;
import com.huwei.activity.R;
import com.huwei.adapter.ConnectAdapter;
import com.huwei.common.Common;
import com.huwei.entity.ConnectInfo;
import com.huwei.services.IDoWork;
import com.huwei.services.TaskManager;

public class ConnectFragment extends Fragment implements
		OnGroupCollapseListener, OnGroupExpandListener, OnChildClickListener,
		OnItemClickListener, OnScrollListener {
	private IMApplication application = null;
	private TaskManager manager = null;
	private IndexActivity activity = null;
	private ExpandableListView elv_friend = null;

	// 内容接受者
	private ContentResolver resolver = null;
	private ConnectAdapter adapter = null;

	// private Handler handler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (IndexActivity) getActivity();
		application = (IMApplication) (getActivity().getApplication());
		manager = activity.manager;

		resolver = getActivity().getContentResolver();
		// handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// if (msg.what == IndexContentObserver.Code_Connect) {
		// // 联系人发生变化
		//
		// }
		// }
		// };
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.connects_layout, container, false);
		elv_friend = (ExpandableListView) view.findViewById(R.id.elv_connect);
		// elv_friend.setOnGroupCollapseListener(this);
		// elv_friend.setOnGroupExpandListener(this);
		elv_friend.setOnChildClickListener(this);
		// 对fling状态的处理
		elv_friend.setOnScrollListener(this);
		// elv_friend.setOnItemClickListener(this);
		// getGroup();
		// adapter = new ConnectAdapter(getActivity(), list, this);
		adapter = new ConnectAdapter(getActivity(), null, this);
		elv_friend.setAdapter(adapter);
		getGroup();
		return view;
	}

	public void getGroup() {
		ArrayList<ConnectInfo> list = null;
		list = new ArrayList<ConnectInfo>();
		Uri uri = Uri.parse("content://com.hwant.im.friend/friend");
		Cursor cursor = resolver.query(uri, null, " user=? ",
				new String[] { application.user.getJid() }, null);
		cursor.moveToFirst();
		ConnectInfo connect = null;
		while (!cursor.isAfterLast()) {
			connect = new ConnectInfo();
			connect.setJid(cursor.getString(cursor.getColumnIndex("jid")));
			connect.setName(cursor.getString(cursor.getColumnIndex("name")));
			connect.setGroup(cursor.getString(cursor.getColumnIndex("fgroup")));
			connect.setNickname(cursor.getString(cursor
					.getColumnIndex("nickname")));
			connect.setUserimg(cursor.getString(cursor
					.getColumnIndex("userimg")));
			list.add(connect);
			cursor.moveToNext();
		}
		cursor.close();
		adapter.setAdapterData(list);
	}

	@Override
	public void onGroupCollapse(int groupPosition) {

	}

	@Override
	public void onGroupExpand(int groupPosition) {

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
								int groupPosition, int childPosition, long id) {
		ConnectInfo info = adapter.getChild(groupPosition, childPosition);
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("info", info);
		startActivity(intent);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		Toast.makeText(getActivity(), "123", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_FLING) {
			adapter.setIsfling(true);
		} else {
			adapter.setIsfling(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {

	}

	public class LoadImage implements IDoWork {
		private VCard vcard = null;
		// private Bitmap bitmap=null;
		private WeakReference<Fragment> weakReference = null;
		private String user = "";
		private FileOutputStream fos = null;
		private SimpleDateFormat format = null;
		// 文件名
		private String filename = "";

		public LoadImage(String user) {
			this.user = user;
			vcard = new VCard();
			weakReference = new WeakReference<Fragment>(ConnectFragment.this);
			format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			filename = format.format(new Date()) + ".png";
		}

		@Override
		public Object doWhat() {
			Bitmap bitmap = null;
			if (StringHelper.isEmpty(user)) {
				return null;
			}
			if (!activity.service.getConnection().isConnected()
					|| !activity.service.getConnection().isAuthenticated()) {
				return null;
			}
			try {
				vcard.load(activity.service.getConnection(), user);
				if (vcard != null && vcard.getAvatar() != null) {
					bitmap = BitmapFactory.decodeByteArray(vcard.getAvatar(),
							0, vcard.getAvatar().length);
				}
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			if (bitmap != null) {
				// String name = format.format(new Date()) + ".png";
				try {
					fos = new FileOutputStream(new File(
							Environment.getExternalStorageDirectory()
									+ Common.Path_Image, filename));
					bitmap.compress(CompressFormat.PNG, 1, fos);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return bitmap;
		}

		@Override
		public void Finish2Do(Object obj) {
			if (obj != null || weakReference.get() != null) {
				if (resolver != null) {
					// 同时需要写入数据库
					Uri uri = Uri.parse("content://com.hwant.im.friend/friend");
					ContentValues values = new ContentValues();
					values.put("userimg", filename);
					resolver.update(uri, values, " jid=? and userimg!=? ",
							new String[] { user,filename });
				}
				ImageView iv_img = (ImageView) elv_friend.findViewWithTag(user);
				if (iv_img != null) {
					adapter.putCache(user, (Bitmap) obj);
					iv_img.setImageBitmap((Bitmap) obj);
				}
			}
		}
	}

	public void refreshAdapter(TaskManager manager) {
		adapter.setManager(manager);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
