package com.we.scp.andapi;

import android.content.Context;

import com.google.gson.Gson;
import com.we.scp.andapi.cookie.PersistentCookieJar;
import com.we.scp.andapi.cookie.cache.SetCookieCache;
import com.we.scp.andapi.cookie.persistence.SharedPrefsCookiePersistor;
import com.we.scp.andapi.encrypt.AesEncryptImpl;
import com.we.scp.andapi.encrypt.IEncrypt;
import com.we.scp.andapi.gsonconvert.GsonConverterFactory;
import com.we.scp.andapi.interceptor.CacheInterceptor;
import com.we.scp.andapi.interceptor.HeaderInterceptor;
import com.we.scp.andapi.interceptor.HttpLoggingInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * API 默认的基本配置
 * Created by shenchunping on 2016/4/27.
 */
public class ApiModule {


    /**
     * 默认网络请求封装
     *
     * @param baseUrl 服务器根地址
     * @param client  网络请求
     * @param gson
     * @param encrypt 加密接口
     * @return Retrofit
     */
    public Retrofit simpleJsonRetrofit(String baseUrl, OkHttpClient client, Gson gson, IEncrypt encrypt) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson, encrypt))
                .build();
        return retrofit;
    }


    /**
     * 数据AES加解密接口
     *
     * @return 加解密实现类
     */
    public IEncrypt getAesEncryptInterface(String key) {
        return new AesEncryptImpl(key);
    }


    /**
     * 默认OkHttpClient配置
     *
     * @return
     */
    public OkHttpClient simpleConfigOkHttpClient(Context context) {
        //设置缓存路径
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        Cache cache = new Cache(httpCacheDirectory, 100 * 1024 * 1024); //设置缓存 100M
        //创建OkHttpClient，并添加拦截器和缓存代码
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(getHeaderInterceptor(context))
                .addInterceptor(getLogInteceptor(HttpLoggingInterceptor.Level.NONE))
                .addNetworkInterceptor(getCacheInterceptor(context))
                .cookieJar(getCookieJar(context, false))
                .cache(cache);
        OkHttpClient client = builder.build();
        return client;
    }

    public CookieJar getCookieJar(Context context, boolean isSerial) {
        CookieJar cookieJar = null;
        if (isSerial) {
            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        } else {
            cookieJar = new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            };
        }
        return cookieJar;
    }


    /**
     * 缓存拦截器
     *
     * @param context
     * @param maxStale 有效时间（秒）
     * @return
     */
    public Interceptor getCacheInterceptor(Context context, long maxStale) {
        return new CacheInterceptor(context, maxStale);
    }

    /**
     * 缓存拦截器
     *
     * @param context
     * @return
     */
    public Interceptor getCacheInterceptor(Context context) {
        return new CacheInterceptor(context);
    }


    /**
     * 头部拦截器
     *
     * @param context
     * @return
     */
    public Interceptor getHeaderInterceptor(Context context) {
        return new HeaderInterceptor(context);
    }


    /**
     * @param level <ul>
     *              <li>{@link HttpLoggingInterceptor.Level#NONE}: 不打印</li>
     *              <li>{@link HttpLoggingInterceptor.Level#BASIC}: 打印基本信息</li>
     *              <li>{@link HttpLoggingInterceptor.Level#HEADERS}: 打印头信息</li>
     *              <li>{@link HttpLoggingInterceptor.Level#BODY}: 打印所有信息</li>
     *              </ul>
     * @return
     */
    public Interceptor getLogInteceptor(HttpLoggingInterceptor.Level level) {
        //日志拦截
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(level);
        return httpLoggingInterceptor;
    }


}
