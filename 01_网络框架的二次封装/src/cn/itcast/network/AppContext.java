package cn.itcast.network;

import cn.itcast.network.api.ApiHttpClient;

import com.loopj.android.http.AsyncHttpClient;

import android.app.Application;

public class AppContext extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		init();
	}

	private void init() {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		ApiHttpClient.setHttpClient(httpClient);
		
	}
	
}
