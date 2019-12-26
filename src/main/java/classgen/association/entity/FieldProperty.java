package classgen.association.entity;

import classgen.entity.JdbcTypeE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.lang.reflect.Modifier;

/**
 * 类{@code FieldParam}: 用于包装类属性的字段
 *
 * @author jiangliangzhong
 * @date 10:32 2019/10/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldProperty implements Serializable {
    /**修饰符数*/
    private int modifierNum;
    /**字段名*/
    private String columnName;
    /** 字段jdbctype */
    private JdbcTypeE jdbcType;

    /**字段类型*/
    private Class<?> javaType;
    /**类中属性名,动态生成类后，添加到对象中*/
    private String fieldName;

    /**对应的set方法名*/
    private String setterName;
    /**对应的get方法名*/
    private String getterName;
    {
        this.modifierNum = Modifier.PRIVATE;
    }

    public FieldProperty(String columnName, JdbcTypeE jdbcType, Class<?> javaType, String fieldName, String setterName, String getterName) {
        this.columnName = columnName;
        this.jdbcType = jdbcType;
        this.javaType = javaType;
        this.fieldName = fieldName;
        this.setterName = setterName;
        this.getterName = getterName;
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


}
