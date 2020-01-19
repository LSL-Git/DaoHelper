package sll.plugin.helper.generator.unit.base;

/**
 * 注解基本单位
 * <p>
 * Created by LSL on 2020/1/10 17:05
 */
public class AnnotationUnit {

    /**
     * 注解名称
     */
    private String annotationName;

    /**
     * 注解对应import的包
     */
    private String importPackage;

    public String getAnnotationName() {
        return annotationName;
    }

    public void setAnnotationName(String annotationName) {
        this.annotationName = annotationName;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }
}
