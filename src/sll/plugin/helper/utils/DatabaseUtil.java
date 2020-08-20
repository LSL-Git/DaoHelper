package sll.plugin.helper.utils;

import sll.plugin.helper.dto.TableColumnDTO;
import sll.plugin.helper.exception.BaseException;
import sll.plugin.helper.model.ConnectionSettings;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LSL on 2020/1/7 16:44
 */
public class DatabaseUtil {

    private ConnectionSettings connectionSettings;
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    /**
     * 获取数据库连接
     *
     * @return Connection
     * @throws Exception
     */
    public Connection connection() throws Exception {
        Class.forName(DRIVER_NAME);
        checkConnectSettings();
        String url = "jdbc:mysql://"
                + connectionSettings.getHost()
                + ":" + connectionSettings.getPort()
                + "/" + connectionSettings.getDatabase()
                + "?useSSL=true";
        return DriverManager.getConnection(url, connectionSettings.getUsername(), connectionSettings.getPassword());
    }

    /**
     * 获取数据库所有表名称
     *
     * @return String[] 表名
     * @throws Exception
     */
    public String[] getTables() throws Exception {
        Connection connection = connection();
        Statement statement = connection.createStatement();
        ResultSet tables = statement.executeQuery("show tables");
        List<String> tableList = new ArrayList<>();
        while (tables.next()) {
            tableList.add(tables.getString(1));
        }
        connection.close();
        return tableList.toArray(new String[0]);
    }


    /**
     * 获取表对于的列信息
     *
     * @param tableNames 表
     * @return 表-列信息
     * @throws Exception 异常
     */
    public Map<String, List<TableColumnDTO>> getTableColumns(List<String> tableNames) throws Exception {
        Connection connection = connection();
        Map<String, List<TableColumnDTO>> map = new HashMap<>();
        Statement statement = connection.createStatement();
        // 执行查询
        ResultSet tables = statement.executeQuery(getSql(tableNames));
        while (tables.next()) {
            String tableName = tables.getString("table_name");
            List<TableColumnDTO> tableColumnDTOS = map.get(tableName);
            TableColumnDTO tableColumnDTO = getTableColumnDTO(tables);
            if (tableColumnDTOS == null) {
                List<TableColumnDTO> columnDTOS = new ArrayList<>();
                columnDTOS.add(tableColumnDTO);
                map.put(tableName, columnDTOS);
            } else {
                tableColumnDTOS.add(tableColumnDTO);
            }
        }
        connection.close();
        return map;
    }

    private TableColumnDTO getTableColumnDTO(ResultSet tables) throws SQLException {
        TableColumnDTO tableColumnDTO = new TableColumnDTO();
        tableColumnDTO.setColumnName(tables.getString("column_name"));
        tableColumnDTO.setDataType(tables.getString("data_type"));
        tableColumnDTO.setColumnComment(tables.getString("column_comment"));
        tableColumnDTO.setPrimaryKey(tables.getString("column_key").equals("PRI"));
        return tableColumnDTO;
    }

    /**
     * 拼接sql，查询表列信息
     *
     * @param tableNames 表名
     * @return sql
     */
    private static String getSql(List<String> tableNames) {
        StringBuilder sql = new StringBuilder("select table_name, column_name, data_type, column_comment, column_key from information_schema.columns where table_name in (");
        int i = 0;
        for (String tableName : tableNames) {
            if (i > 0) {
                sql.append(", ");
            }
            i++;
            sql
                    .append("\'")
                    .append(tableName)
                    .append("\'");
        }
        sql.append(");");
        return sql.toString();
    }

    /**
     * 检查数据库连接信息
     */
    private void checkConnectSettings() {
        if (connectionSettings == null) {
            throw new BaseException("连接失败，连接信息为空");
        }
        if (connectionSettings.getHost().isEmpty()
                || connectionSettings.getPort().isEmpty()
                || connectionSettings.getDatabase().isEmpty()
                || connectionSettings.getUsername().isEmpty()) {
            throw new BaseException("连接失败，连接信息错误");
        }
    }

    public DatabaseUtil(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    private DatabaseUtil() {
    }
}
