package sll.plugin.helper.exception;

/**
 * Created by LSL on 2020/1/7 17:24
 */
public class BaseException extends RuntimeException {

    private String msg;

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
