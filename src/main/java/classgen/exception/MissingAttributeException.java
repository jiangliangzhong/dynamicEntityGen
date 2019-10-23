package classgen.exception;

/**
 * 类{@code MissingAttributeException}:
 *
 * @author jiangliangzhong
 * @date 16:36 2019/10/22
 */
public class MissingAttributeException extends RuntimeException{
    public MissingAttributeException() {
        super();
    }

    public MissingAttributeException(String message) {
        super(message);
    }

    public MissingAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAttributeException(Throwable cause) {
        super(cause);
    }

    protected MissingAttributeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
