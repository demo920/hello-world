package cn.itcast.network;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.itcast.network.api.NetWorkApi;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity {

	private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView = (TextView) findViewById(R.id.tv);
    }
    
    public void getData(View v){
    	int catalog = 1;
    	int page = 0;
    	
		NetWorkApi.getNewsList(catalog, page, asyncHttpResponseHandler);
    	
    }
    AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler() {
    	
    	@Override
    	public void onSuccess(int code, Header[] headers, byte[] data) {
    		textView.setText(new String(data));
    	}
    	
    	@Override
    	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
    		// TODO Auto-generated method stub
    		
    	}
    };

}
