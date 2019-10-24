package classgen.mapass;

import classgen.core.MyClassLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 类{@code EntityClassInfo}:
 *
 * @author jiangliangzhong
 * @date 15:18 2019/10/23
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EntityClassInfo {
    private MyClassLoader myClassLoader;
    private String className;
    /** 字段名：对应的信息 */
    private Map<String,FieldMemberInfo> fieldMemberInfoMap;
    private List<FieldMemberInfo> fieldMemberInfoList;

    public FieldMemberInfo getFieldMemberInfoByKey(String fieldName){
        return fieldMemberInfoMap.get(fieldName);
    }
}
