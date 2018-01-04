package com.we.scp.andapi.gsonconvert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.orhanobut.logger.Logger;
import com.we.scp.andapi.encrypt.IEncrypt;
import com.we.scp.andapi.exception.EncryptException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;


/**
 * 请求参数转换
 * Created by admin on 2016/11/17.
 */
class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {


    /**
     * HTTP Body 类型 contentType application/json; charset=UTF-8
     */
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    /**
     * 字符集类型
     */
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * JSON解析框架
     */
    private final Gson gson;
    /**
     * 解析适配器
     */
    private final TypeAdapter<T> adapter;
    /**
     * 加密工具
     */
    private final IEncrypt mIEncrypt;

    /**
     * 请求参数转换
     *
     * @param gson
     * @param adapter
     * @param IEncrypt 加密工具
     */
    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter, IEncrypt IEncrypt) {
        this.gson = gson;
        this.adapter = adapter;
        this.mIEncrypt = IEncrypt;
    }

    /**
     * 请求数据转换,讲请求的参数对象转为JSON,再加密
     *
     * @param value 参数对象
     * @return Http Body
     * @throws IOException
     */
    @Override
    public RequestBody convert(T value) throws IOException {
        Buffer buffer = new Buffer();
        Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        JsonWriter jsonWriter = gson.newJsonWriter(writer);
        adapter.write(jsonWriter, value);
        jsonWriter.close();
        if (mIEncrypt.isEncrypt()) {
            String string = new String(buffer.readByteArray());
            try {
                if (mIEncrypt.debug()) {
                    Logger.d("request -> " + string);
                }
                string = mIEncrypt.encrypt(string);
                if (mIEncrypt.debug()) {
                    Logger.d("request encrypt-> " + string);
                }
            } catch (Exception e) {
                throw new EncryptException(e);
            }
            return RequestBody.create(MEDIA_TYPE, string);
        } else {
            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }

    }

}
