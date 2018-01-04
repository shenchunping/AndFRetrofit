package com.we.scp.andapi.encrypt;

/**
 * 加解密接口
 * Created by shenchunping on 2016/11/17.
 */

public interface IEncrypt {

    /**
     * 加密请求数据
     *
     * @param src 待加密字符
     * @return
     * @throws Exception
     */
    String encrypt(String src) throws Exception;

    /**
     * 解密响应数据
     *
     * @param src 待解密字符
     * @return
     * @throws Exception
     */
    String decrypt(String src) throws Exception;

    /**
     * 是否加密请求数据
     *
     * @return
     */
    boolean isEncrypt();

    /**
     * 是否解密响应数据
     *
     * @return
     */
    boolean isDecrypt();


    /**
     * 设置是否加密
     *
     * @param encrypt
     */
    void setEncrypt(boolean encrypt);


    /**
     * 设置是否解密
     *
     * @param decrypt
     */
    void setDecrypt(boolean decrypt);


    /**
     * 是否调试打印日志
     *
     * @param debug
     */
    void debug(boolean debug);

    /**
     * 是否打印日志
     *
     * @return
     */
    boolean debug();

}
