package com.we.scp.andapi.interceptor;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.we.scp.andapi.util.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 描述：拦截器,设置公共请求头,如代理信息.
 * Created by 沈春平 on 2016/11/28.
 */

public class HeaderInterceptor implements Interceptor {
    private final Context mContext;

    public HeaderInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String agent = "V" + Utils.getAppVersionName(mContext);
        agent += "(";
        agent += Build.BRAND + ";";
        agent += Build.MODEL + ";";
        agent += Build.VERSION.RELEASE + ")";
        String cntentType = chain.request().headers().get("Content-Type");
        Request.Builder builder = chain.request().newBuilder();
        if (TextUtils.isEmpty(cntentType)) {
            builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        }
        Request request = builder
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("user-agent", agent)
                .build();
        return chain.proceed(request);
    }


}
