package classgen.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类{@code TableWithEntityRel}: 用于存放表与实际的关系
 *
 * @author jiangliangzhong
 * @date 15:06 2019/10/23
 */
public class TableWithEntityRelPool {
    private static Map<TableInfo, EntityClassInfo> entityClassInfoMap;

    static{
        entityClassInfoMap = new ConcurrentHashMap<>();
    }
    public static Map<TableInfo, EntityClassInfo> getEntityClassInfoMap() {
        return entityClassInfoMap;
    }
    public static EntityClassInfo getEntityClassInfoByKey(TableInfo tableInfoKey){
        return entityClassInfoMap.get(tableInfoKey);
    }

}
