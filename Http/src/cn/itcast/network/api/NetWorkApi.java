package cn.itcast.network.api;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 获取具体业务相关的数据
 * @author poplar
 *
 */
public class NetWorkApi {

	/**
	 * 获取资讯列表数据
	 * @param catalog
	 * @param page
	 * @param mHander
	 */
	public static void getNewsList(int catalog, int page,
			AsyncHttpResponseHandler mHander) {
		String path = "";
		switch (catalog) {
		case 1:
			path = "oschina/list/news/page"+page+".xml";
			break;
		case 4:
			path = "oschina/list/hotspot/page"+page+".xml";
			break;
		default:
			break;
		}
		
		ApiHttpClient.getLocal(path, mHander);
		
	}

}
