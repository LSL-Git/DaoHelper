package sll.plugin.helper.model;

/**
 * 数据库连接设置
 * Created by LSL on 2020/1/7 17:05
 */
public class ConnectionSettings {
    /**
     * 主机地址
     */
    private String host;
    /**
     * 端口号
     */
    private String port;
    /**
     * 数据库名称
     */
    private String database;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
