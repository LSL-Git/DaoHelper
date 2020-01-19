package sll.plugin.helper.generator.unit.base;

import sll.plugin.helper.generator.unit.enums.MethodTypeEnum;

import java.util.List;
import java.util.Set;

/**
 * Created by LSL on 2020/1/10 17:22
 */
public class MethodUnit {
    /**
     * 方法类型
     */
    private MethodTypeEnum methodType;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 输入参数类型
     */
    private List<String> methodParams;

    /**
     * 输入参数名称，应与类型一一对应
     */
    private List<String> methodParamsName;

    /**
     * 返回参数类型
     */
    private String resultParams;

    /**
     * 返回参数名称
     */
    private String resultParamName;

    /**
     * 字段对应需要导入的包名
     */
    private Set<String> importPackages;

    public MethodTypeEnum getMethodType() {
        return methodType;
    }

    public void setMethodType(MethodTypeEnum methodType) {
        this.methodType = methodType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(List<String> methodParams) {
        this.methodParams = methodParams;
    }

    public List<String> getMethodParamsName() {
        return methodParamsName;
    }

    public void setMethodParamsName(List<String> methodParamsName) {
        this.methodParamsName = methodParamsName;
    }

    public String getResultParams() {
        return resultParams;
    }

    public void setResultParams(String resultParams) {
        this.resultParams = resultParams;
    }

    public String getResultParamName() {
        return resultParamName;
    }

    public void setResultParamName(String resultParamName) {
        this.resultParamName = resultParamName;
    }

    public Set<String> getImportPackages() {
        return importPackages;
    }

    public void setImportPackages(Set<String> importPackages) {
        this.importPackages = importPackages;
    }
}
