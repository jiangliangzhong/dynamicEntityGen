package classgen.ass;

import java.lang.reflect.Modifier;

/**
 * 类{@code FieldParam}: 用于包装类属性的字段
 *
 * @author jiangliangzhong
 * @date 10:32 2019/10/19
 */
public class FieldParam {
    /**修饰符数*/
    private int modifierNum;
    /**字段名*/
    private String fieldName;
    /**字段类型*/
    private Class<?> fieldType;
    /**类中属性名,动态生成类后，添加到对象中*/
    private String classFieldName;
    /**对应的set方法名*/
    private String setterName;
    /**对应的get方法名*/
    private String getterName;


    public FieldParam() {
    }

    public FieldParam(int modifierNum, String fieldName, Class<?> fieldType) {
        this.modifierNum = modifierNum;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getClassFieldName() {
        return classFieldName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getGetterName() {
        return getterName;
    }

    public void setGetterName(String getterName) {
        this.getterName = getterName;
    }

    public void setClassFieldName(String classFieldName) {
        this.classFieldName = classFieldName;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public int getModifierNum() {
        return modifierNum;
    }

    public void setModifierNum(int modifierNum) {
        //检查修饰符是否有效
        int fieldModifier = Modifier.methodModifiers();
        while(modifierNum > 0){
            if((fieldModifier&1)==0 && (modifierNum &1)==1){
                throw new RuntimeException("异常：修饰符无效");
            }
            fieldModifier >>= 1;
            modifierNum >>=1;
        }
        this.modifierNum = modifierNum;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
