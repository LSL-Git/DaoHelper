package sll.plugin.helper.dto;

/**
 * Created by LSL on 2020/1/14 15:35
 */
public class TableColumnDTO {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列数据类型
     */
    private String dataType;
    /**
     * 列注释
     */
    private String columnComment;

    /**
     * 是否主键
     */
    private boolean isPrimaryKey;

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
