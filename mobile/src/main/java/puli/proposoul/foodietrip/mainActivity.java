package puli.proposoul.foodietrip;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import puli.proposoul.foodietrip.library.JsonParser;

/**
 * Created by louis383 on 15/6/24.
 */

public class mainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);    //讀入介面
        // 連線測試
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> datas = new HashMap<String, String>();
                datas.put("page", "1");

                JsonParser jsonParser = new JsonParser(getApplicationContext());
                String result = jsonParser.getJsonData("list", "GET", datas).toString();
                Log.e("Test if okay: ", result);
            }
        }).start();
    }

}
