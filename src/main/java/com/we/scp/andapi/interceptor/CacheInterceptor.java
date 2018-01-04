package com.we.scp.andapi.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.we.scp.andapi.util.Utils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：缓存拦截器,在无网络情况下,默认加载本地缓存(仅支持GET请求)
 * Created by 沈春平 on 2016/11/28.
 */

public class CacheInterceptor implements Interceptor {
    private final Context mContext;
    private long maxStale = 2419200;

    public CacheInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @param context
     * @param maxStale 缓存有效期(秒)
     */
    public CacheInterceptor(Context context, long maxStale) {
        mContext = context;
        this.maxStale = maxStale;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!Utils.isConnected(mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (Utils.isConnected(mContext)) {
            //读接口上的@Headers里的配置
            String cacheControl = request.cacheControl().toString();
            response = response.newBuilder()
                    .header("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
        } else {
            //候读接口上的@Headers里的配置
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                //使用配置的缓存策略
                response = response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                //使用默认的缓存策略
                response = response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }

        }
        return response;
    }


}
