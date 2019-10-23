package classgen.exception;

/**
 * 类{@code CannotInitGenTool}: 无法初始化异常
 *
 * @author jiangliangzhong
 * @date 22:09 2019/10/21
 */
public class CannotInitGenTool extends  Exception{

    public CannotInitGenTool() {
        super();
    }

    public CannotInitGenTool(String message) {
        super(message);
    }

    public CannotInitGenTool(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotInitGenTool(Throwable cause) {
        super(cause);
    }

    protected CannotInitGenTool(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
