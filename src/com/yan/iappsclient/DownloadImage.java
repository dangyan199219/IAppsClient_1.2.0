package com.yan.iappsclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class DownloadImage {
	private String img_path;

	public DownloadImage(String img_path) {
		super();
		this.img_path = img_path;
	}

	// 内部类用到外部变量要将外部变量定义为最终类型
	public void loadImage(final ImageCallback callback) {
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				callback.getDrawable((Drawable) msg.obj);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Drawable drawable = null;
				try {
					drawable = Drawable.createFromStream(
							new URL(img_path).openStream(), "");
					Message message = Message.obtain();
					message.obj = drawable;
					handler.sendMessage(message);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 接口的回调方式
	public interface ImageCallback {
		public void getDrawable(Drawable drawable);
	}
}
