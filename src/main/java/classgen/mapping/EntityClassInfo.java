package classgen.mapping;

import classgen.association.entity.FieldProperty;
import classgen.core.MyClassLoader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 类{@code EntityClassInfo}:
 *
 * @author jiangliangzhong
 * @date 15:18 2019/10/23
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntityClassInfo  implements Serializable {

    private static final long serialVersionUID = 4909813441139158498L;
    private String className;
    /** 字段名：对应的信息 */
    private Map<String, FieldProperty> fieldMemberInfoMap;
    private List<FieldProperty> fieldMemberInfoList;

    public FieldProperty getFieldMemberInfoByKey(String fieldName){
         FieldProperty fieldMemberInfo = fieldMemberInfoMap.get(fieldName);
         if(fieldMemberInfo == null){
             throw new RuntimeException("无法找到"+fieldName+"字段名对应的字段信息");
         }
         return fieldMemberInfo;
    }

    private MyClassLoader myClassLoader;

}
