package classgen.mapping;

import classgen.association.entity.FieldProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author ZhouPan
 * @date 2019-11-18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityClassProperty implements Serializable {
	private static final long serialVersionUID = -6946918743663262280L;
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
}
