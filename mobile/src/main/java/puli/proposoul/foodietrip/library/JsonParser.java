package puli.proposoul.foodietrip.library;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis383 on 15/7/19.
 */

public class JsonParser {
    Context context;
    JSONObject jsonObject;
    String url = "http://proposal.twbbs.org/api/index.php/store";

    public JsonParser(Context _context) {
        this.context = _context;
    }

    // 預設是 GET
    public JSONObject getJsonData(String action, String method) {
        try {
            URL url = new URL(this.url+"/"+action);
            //Log.d("URL: ", url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if ( method.equals("POST") ) {
                conn.setDoOutput(true);    // use POST
            }
            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ( (line = bufferedReader.readLine() ) != null)
                sb.append(line +"\n");
            inputStream.close();
            this.jsonObject = new JSONObject(sb.toString());
        } catch ( Exception e ) {
            Log.d("getJsonData_Error: ", e.toString());
        }
        return this.jsonObject;
    }

    // 送出資料
    public JSONObject getJsonData(String action, String method, HashMap<String, String> data) {
        try {
            String host = this.url+"/"+action;
            // GET 後墜處理
            if ( method.equals("GET") ) {
                if ( data.size() > 0 ) {
                    for ( Map.Entry<String,String> dataMap : data.entrySet() ) {
                        host += "?a=a&" +dataMap.getKey() +"=" +dataMap.getValue();
                    }
                }
            }
            URL url = new URL(host);
            Log.d("URL: ", url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            if ( method.equals("POST") ) {
                conn.setDoOutput(true);    // use POST
            }

            // 寫入資訊(POST 用)
            if ( method.equals("POST") ) {
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(setPostParam(data));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            }

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ( (line = bufferedReader.readLine() ) != null)
                sb.append(line +"\n");
            inputStream.close();
            this.jsonObject = new JSONObject(sb.toString());
        } catch ( Exception e ) {
            Log.d("getJsonData_Error: ", e.toString());
        }
        return this.jsonObject;
    }

    // 處理使用者 POST 至遠端的東西
    private String setPostParam(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for ( Map.Entry<String, String> entry : params.entrySet() ) {
            if ( first )
                first = false;
            else
                stringBuilder.append("&");
            stringBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return stringBuilder.toString();
    }

}
