package zhang.zhentao.refereeresource.util;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import zhang.zhentao.refereeresource.listener.HttpCallbackListener;

/**
 * Created by 张镇涛 on 2016/12/3.
 */

public class HttpURLConnectionUtil {

    public static void sendRequest(final String address, final Map<String,String> params, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL("http://10.0.2.2:8080/RefereeResourceServer/"+address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    //参数处理
                    String data = "";
                    if(params != null)data = mapToString(params);
                    connection.setRequestProperty("Connection","keep-alive");
                    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length",String.valueOf(data.getBytes().length));
                    connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.getBytes());
                    outputStream.flush();
                    if(connection.getResponseCode() == 200){
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine())!=null){
                            response.append(line);
                        }
                        reader.close();
                        inputStream.close();
                        if(listener != null){
                            listener.onFinish(response.toString());
                        }
                    }else {
                        if(listener != null){
                            listener.onError(new RuntimeException("连接失败"));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(listener != null){
                        listener.onError(new RuntimeException("连接错误"));
                    }
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private static String mapToString(Map<String,String> map){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String,String> entry:map.entrySet()){
            try {
                builder.append("&");
                builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        builder.deleteCharAt(0);
        return builder.toString();
    }
}
