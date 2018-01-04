package com.we.scp.andapi.exception;

import java.io.IOException;

/**
 * 描述：解密异常
 * Created by 沈春平 on 2016/11/25.
 */

public class DecryptException extends IOException {

    public DecryptException() {
        super();
    }

    public DecryptException(String message) {
        super(message);
    }

    public DecryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecryptException(Throwable cause) {
        super(cause);
    }
}
