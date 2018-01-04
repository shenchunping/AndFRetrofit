package com.we.scp.andapi.gsonconvert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.we.scp.andapi.encrypt.IEncrypt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 自定义JSON转换器
 * Created by shenchunping on 2016/11/17.
 */

public class GsonConverterFactory extends Converter.Factory {


    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * <p>
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */

    public static GsonConverterFactory create(Gson gson, IEncrypt IEncrypt) {
        return new GsonConverterFactory(IEncrypt, gson);
    }


    private final IEncrypt mIEncrypt;
    private final Gson gson;

    GsonConverterFactory(IEncrypt IEncrypt, Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.mIEncrypt = IEncrypt;
        this.gson = gson;
    }

    /**
     * 响应数据转换
     *
     * @param type
     * @param annotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonResponseBodyConverter<>(gson, adapter, mIEncrypt);
    }


    /**
     * 请求数据转换
     *
     * @param type
     * @param parameterAnnotations
     * @param methodAnnotations
     * @param retrofit
     * @return
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter, mIEncrypt);

    }


}
