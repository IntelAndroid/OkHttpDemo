package com.feicuiedu.demookhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<Contributor> adapter;

    //compile 'com.squareup.okhttp3:okhttp:3.4.0-RC1'
    private OkHttpClient client;
    //compile 'com.squareup.okhttp3:logging-interceptor:3.4.0-RC1'
    private HttpLoggingInterceptor loggingInterceptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 列表视图
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ArrayAdapter<Contributor>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // 初始OKHTTP相关
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // 添加一个日志拦截器
                .build();

        // https://api.github.com/repos/square/okhttp/contributors
        // https://api.github.com/repos/square/retrofit/contributors
        // OKHTTP网络连接-----------------------------------------------
        // 构建好 请求
        String owner = "square"; // 公司
        String repo = "retrofit";// 产品
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/" + owner + "/" + repo + "/contributors")
                .build();

        // 请求(异步)
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        Toast.makeText(MainActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();

                TypeToken<List<Contributor>> typeToken =
                        new TypeToken<List<Contributor>>(){};

                final List<Contributor> contributors = gson.fromJson(body, typeToken.getType());

                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        adapter.addAll(contributors);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
