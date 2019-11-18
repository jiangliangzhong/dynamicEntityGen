package classgen.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 类{@code FieldMemberInfo}:
 *
 * @author jiangliangzhong
 * @date 15:41 2019/10/23
 */
@Data
@AllArgsConstructor
public class FieldMemberInfo implements Serializable {

    private static final long serialVersionUID = 7929125045086792833L;
    /**字段名*/
    private String dbFieldName;
    /**类中属性名,动态生成类后，添加到对象中*/
    private String classFieldName;
    /**对应类型*/
    private Class<?> type;
    /**对应的set方法名*/
    private String setterName;
    /**对应的get方法名*/
    private String getterName;
}
