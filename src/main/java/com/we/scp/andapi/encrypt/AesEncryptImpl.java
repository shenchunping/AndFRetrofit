package com.we.scp.andapi.encrypt;


/**
 * AES加密接口实现
 */
public class AesEncryptImpl implements IEncrypt {

    private final static String TAG = "AesEncryptImpl";

    /**
     * 加解密key
     */
    private final String key;
    private boolean encrypt;
    private boolean decrypt;
    private boolean debug;

    public AesEncryptImpl(String key) {
        this.key = key;
    }

    @Override
    public String encrypt(String src) throws Exception {
        String result = src;
        if (encrypt) {
            result = AESEncryptor.encrypt(key, src);
        }
        return result;
    }

    @Override
    public String decrypt(String src) throws Exception {
        String result = src;
        if (decrypt) {
            result = AESEncryptor.decrypt(key, src);
        }
        return result;
    }

    @Override
    public boolean isEncrypt() {
        return encrypt;
    }

    @Override
    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    @Override
    public boolean isDecrypt() {
        return decrypt;
    }

    @Override
    public void setDecrypt(boolean decrypt) {
        this.decrypt = decrypt;
    }

    @Override
    public void debug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public boolean debug() {
        return debug;
    }
}
