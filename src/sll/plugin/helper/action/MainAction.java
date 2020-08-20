package sll.plugin.helper.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import sll.plugin.helper.enums.LocalCacheEnum;
import sll.plugin.helper.model.ConnectionSettings;
import sll.plugin.helper.ui.InfoUI;
import sll.plugin.helper.ui.SelectionUI;
import sll.plugin.helper.utils.DatabaseUtil;
import sll.plugin.helper.utils.MessageUtil;
import sll.plugin.helper.utils.StateLocalCacheUtil;

/**
 * Created by LSL on 2020/1/7 11:30
 */
public class MainAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        // 获取本地缓存信息
        ConnectionSettings connectionSettings = StateLocalCacheUtil.get(LocalCacheEnum.CONNECTION_SETTINGS_STATE);
        if (connectionSettings != null && testConnection(connectionSettings)) {
            try {
                new SelectionUI(anActionEvent, connectionSettings);
            } catch (Exception e) {
                MessageUtil.error(e.getMessage());
                new InfoUI(anActionEvent);
            }
        } else {
            new InfoUI(anActionEvent);
        }
    }

    /**
     * 测试连接
     */
    private boolean testConnection(ConnectionSettings connectionSettings) {
        DatabaseUtil databaseUtil = new DatabaseUtil(connectionSettings);
        try {
            databaseUtil.connection();
            return true;
        } catch (Exception ex) {
            MessageUtil.error(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
}
