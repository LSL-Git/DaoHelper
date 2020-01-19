package sll.plugin.helper.utils;

import com.intellij.openapi.ui.Messages;

/**
 * Created by LSL on 2020/1/7 17:30
 */
public class MessageUtil {

    public static void error(String msg) {
        Messages.showMessageDialog(msg, "错误", Messages.getErrorIcon());
    }

    public static void success(String msg) {
        Messages.showMessageDialog(msg, "成功", Messages.getInformationIcon());
    }

    public static void warn(String msg) {
        Messages.showMessageDialog(msg, "警告", Messages.getWarningIcon());
    }
}
