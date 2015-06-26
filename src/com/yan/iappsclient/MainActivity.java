package com.yan.iappsclient;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yan.iappsclient.DownloadImage.ImageCallback;
import com.yan.model.AppInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ListView listView;
	private ProgressDialog progressDialog;

	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.content_listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			/**
			 * AdapterView<?> parent对应的是ListView，
			 * View view对应的是ListView里面每一项的内容视图，这里是item_content_listview.xml
			 * position对应的是ListView的每一项的位置
			 */
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AppInfo appInfo = new AppInfo();
				appInfo = (AppInfo) parent.getAdapter().getItem(position);
				Log.v("OnClick",
						appInfo.getmTittle() + "<--->" + appInfo.getmProvider()
								+ "<--->" + appInfo.getmContent());
			}
		});
		myAdapter = new MyAdapter(this);// 构建适配器
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Tips");
		progressDialog.setMessage("正在下载");
		new MyTask().execute(CommonURL.IAPP_URL);

	}

	public class MyTask extends AsyncTask<String, Void, List<AppInfo>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(List<AppInfo> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			myAdapter.setData(result);
			listView.setAdapter(myAdapter);
			myAdapter.notifyDataSetChanged();
			progressDialog.dismiss();
		}

		@Override
		protected List<AppInfo> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<AppInfo> list = new ArrayList<AppInfo>();
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(params[0]);
				HttpResponse response = client.execute(post);
				// 判断是否连接成功
				if (response.getStatusLine().getStatusCode() == 200) {
					String resultString = EntityUtils.toString(
							response.getEntity(), "utf-8");
					list = getAppInfosFromHTML(resultString);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return list;
		}
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;

		private List<AppInfo> list = null;// 定义数据格式

		public MyAdapter(Context context) {
			// 获取LayoutInflater实例，用于动态加载布局
			layoutInflater = LayoutInflater.from(context);
		}

		public void setData(List<AppInfo> list) {
			// TODO Auto-generated method stub
			this.list = list;
		}

		/**
		 * 返回数据的数量，即list的大小
		 */
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		/**
		 * 返回指定位置上的具体数据
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		/**
		 * 返回数据的位置
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
			if (convertView == null) {
				// 注意这里的null，有时候用来null之后不能正常显示，要改为this，此处不可以用this，报错
				// 动态加载布局文件item_content_listview.xml
				view = layoutInflater.inflate(R.layout.item_content_listview,
						null);
			} else {
				view = convertView;
			}
			// 注意这里是view.findViewById(R.id.tittle_edit),不写view会出现空指针异常
			TextView mTittleTextView = (TextView) view
					.findViewById(R.id.tittle_edit);
			TextView mProviderTextView = (TextView) view
					.findViewById(R.id.provider_edit);
			TextView mContentTextView = (TextView) view
					.findViewById(R.id.content_edit);
			mTittleTextView.setText(list.get(position).getmTittle().toString());
			mProviderTextView.setText(list.get(position).getmProvider()
					.toString());
			mContentTextView.setText(list.get(position).getmContent()
					.toString());
			Log.v("NullPoint1", list.get(position).getmTittle().toString());
			Log.v("NullPoint2", list.get(position).getmProvider().toString());
			Log.v("NullPoint3", list.get(position).getmContent().toString());
			// 如何加载图片
			final ImageView mAppIconImageView = (ImageView) view
					.findViewById(R.id.appicon_imageView);
			DownloadImage downloadImage = new DownloadImage(list.get(position)
					.getImage_path().toString());
			downloadImage.loadImage(new ImageCallback() {

				@Override
				public void getDrawable(Drawable drawable) {
					// TODO Auto-generated method stub
					mAppIconImageView.setImageDrawable(drawable);
				}
			});
			// 两种加载图片的方式
			// mAppIconImageView.setImageDrawable(drawable);
			// mAppIconImageView.setImageBitmap(bm);
			return view;
		}
	}

	/**
	 * 从获取到的网页内容中提取App的信息
	 * 
	 * @param resultString
	 * @return
	 */
	public List<AppInfo> getAppInfosFromHTML(String resultString) {
		List<AppInfo> list = new ArrayList<AppInfo>();
		Document doc = Jsoup.parse(resultString);
		Elements namesOfApp = doc.select(".apps").select(".limit-free")
				.select("article:not(.editor-choice)")// 过滤掉class属性为editor-choice
				.select("article:not(.special-choice)");// 过滤掉class属性为special-choice
		Log.v("name", namesOfApp.size() + "");
		for (Element element : namesOfApp) {
			AppInfo appInfo = new AppInfo();
			// 获取app的标题名称
			String mTittleText = element.select("h2[class=entry-title]").get(0)
					.text();
			appInfo.setmTittle(mTittleText);
			// Log.v("name2", mTittleText + "");

			// 获取app的基本信息
			String mProviderText = element.select("div[class=pull-left]")
					.get(0).text();
			appInfo.setmProvider(mProviderText);
			// Log.v("name3", mProviderText + "");

			// 获取app的介绍信息
			String mContentText = element.select("div[class=entry-content]")
					.get(0).text();
			appInfo.setmContent(mContentText);
			// Log.v("name4", mContentText + "");

			// 获取app的详细介绍地址
			String app_path = element.select("a").get(0).attr("href");
			appInfo.setApp_path(app_path);
			// Log.v("name5", app_path + "");

			// 获取app的图片地址
			String image_path = element.select("img").get(0)
					.attr("data-original");
			appInfo.setImage_path(image_path);
			// Log.v("name6", image_path + "");

			list.add(appInfo);
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
