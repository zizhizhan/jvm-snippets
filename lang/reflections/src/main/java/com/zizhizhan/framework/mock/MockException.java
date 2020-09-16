package com.zizhizhan.framework.mock;

/**
 * Created by IntelliJ IDEA.
 * User: James
 * Date: 12-11-4
 * Time: AM11:09
 * To change this template use File | Settings | File Templates.
 */
public class MockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MockException() {
        super();
    }

    public MockException(String message, Throwable cause) {
        super(message, cause);
    }

    public MockException(String message) {
        super(message);
    }

    public MockException(Throwable cause) {
        super(cause);
    }

}
