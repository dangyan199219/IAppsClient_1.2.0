package com.yan.model;

public class AppInfo {
	private String mTittle;
	private String mContent;
	private String mProvider;
	private String app_path;
	private String image_path;

	public AppInfo() {
		super();
	}

	public AppInfo(String mTittle, String mContent, String mProvider,
			String app_path, String image_path) {
		super();
		this.mTittle = mTittle;
		this.mContent = mContent;
		this.mProvider = mProvider;
		this.app_path = app_path;
		this.image_path = image_path;
	}

	public String getmTittle() {
		return mTittle;
	}

	public void setmTittle(String mTittle) {
		this.mTittle = mTittle;
	}

	public String getmContent() {
		return mContent;
	}

	public void setmContent(String mContent) {
		this.mContent = mContent;
	}

	public String getmProvider() {
		return mProvider;
	}

	public void setmProvider(String mProvider) {
		this.mProvider = mProvider;
	}

	public String getApp_path() {
		return app_path;
	}

	public void setApp_path(String app_path) {
		this.app_path = app_path;
	}

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
}
