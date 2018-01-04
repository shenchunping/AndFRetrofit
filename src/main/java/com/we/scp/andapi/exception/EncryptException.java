package com.we.scp.andapi.exception;

import java.io.IOException;

/**
 * 描述：加密异常
 * Created by 沈春平 on 2016/11/25.
 */

public class EncryptException extends IOException {

    public EncryptException() {
        super();
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptException(Throwable cause) {
        super(cause);
    }
}
