package sll.plugin.helper.model;

import java.util.List;

/**
 * Created by LSL on 2020/1/8 15:52
 */
public class MainUIData {

    /**
     * 选择的表
     */
    private List<String> selectedTables;

    /**
     * 数据库连接信息
     */
    private ConnectionSettings connectionSettings;

    public List<String> getSelectedTables() {
        return selectedTables;
    }

    public void setSelectedTables(List<String> selectedTables) {
        this.selectedTables = selectedTables;
    }

    public ConnectionSettings getConnectionSettings() {
        return connectionSettings;
    }

    public void setConnectionSettings(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }
}
