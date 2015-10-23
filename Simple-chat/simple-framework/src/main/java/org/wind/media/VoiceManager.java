package org.wind.media;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class VoiceManager {
	private MediaRecorder recorder = null;
	private VoiceThread thread = null;
	// 主线程的handler
	private Handler mainHandler = null;
	// 子线程
	private Handler handler = null;
	// 录音默认时常为30秒
	private int timesize = 30;

	private Object obj = new Object();
	// 继续录制
	boolean isstop = false;

	public VoiceManager(String filepath, Handler handler) {
		this.mainHandler = handler;
		recorder = new MediaRecorder();
		// 设置录音源
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		// 3gp格式
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
		// recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		// 设置录音的文件路径
		recorder.setOutputFile(filepath);
		thread = new VoiceThread();
		// Looper looper=Looper.myLooper();
		// looper.prepare();
		// handler=new Handler(looper);
	}

	public void startRecord() {
		thread.start();
		// return true;
	}

	/**
	 * 发送给子线程的体制录制的消息
	 */
	public void stopRecord() {
		// Handler childHandler = getHandler();
		// Message msg = childHandler.obtainMessage();
		// msg.what = 1;
		// msg.sendToTarget();
		synchronized (obj) {
			isstop = true;
			obj.notify();
		}
	}

	class VoiceThread extends Thread {
		int i = 0;

		@Override
		public void run() {
			// Looper.prepare();
			// synchronized (obj) {
			// handler = new Handler(Looper.myLooper()) {
			// @Override
			// public void handleMessage(Message msg) {
			// if (msg.what == 1) {
			// // 停止录音
			// isstop = true;
			// }
			// }
			// };
			// obj.notifyAll();
			// }
			// Looper.loop();
			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 开始录制
			recorder.start();
			for (i = 0; i < timesize && !isstop; i++) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// 结束录制
			recorder.stop();
			// recorder.reset();
			recorder.release();
			Message msg = mainHandler.obtainMessage();
			// 发消息主线程，提示录音结果
			if (isstop) {
				// 正常录制完毕
				msg.what = MediaComm.What_Voice_Normal_Finish;
			} else {
				// 非正常录制完毕
				msg.what = MediaComm.What_Voice_Timeout_Finish;
			}
			msg.what = MediaComm.What_Voice_Finish;
			msg.sendToTarget();
		}
	}

	@Deprecated
	public Handler getHandler() {
		// synchronized (obj) {
		// if (handler == null) {
		// try {
		// obj.wait();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		return handler;
	}
}
