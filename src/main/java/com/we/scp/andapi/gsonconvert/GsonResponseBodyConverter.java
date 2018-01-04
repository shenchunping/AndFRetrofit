package com.we.scp.andapi.gsonconvert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;
import com.we.scp.andapi.encrypt.IEncrypt;
import com.we.scp.andapi.exception.DecryptException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by admin on 2016/11/17.
 */

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final String TAG = "ENCRYPT_LOG";

    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final IEncrypt mIEncrypt;

    public GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, IEncrypt IEncrypt) {
        this.gson = gson;
        this.adapter = adapter;
        this.mIEncrypt = IEncrypt;
    }

    /**
     * 响应数据解析
     *
     * @param value
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody value) throws IOException {


        try {
            JsonReader jsonReader;
            if (mIEncrypt.isDecrypt()) {
                try {
                    String string = value.string();

                    if (mIEncrypt.debug()){
                        Logger.d("response -> "+string);
                    }
                    string = mIEncrypt.decrypt(string);
                    if (mIEncrypt.debug()){
                        Logger.d("response decrypt-> "+string);
                    }
                    InputStream inputStream = new ByteArrayInputStream(string.getBytes());
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    jsonReader = gson.newJsonReader(reader);
                } catch (Exception e) {
                    throw new DecryptException(e);
                }
            } else {
                jsonReader = gson.newJsonReader(value.charStream());
            }
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }

    }


}
