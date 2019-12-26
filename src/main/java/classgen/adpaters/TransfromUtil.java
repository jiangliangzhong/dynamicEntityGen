package classgen.adpaters;

import classgen.association.entity.BuildTaskProperity;

import classgen.association.entity.ClassPropertity;

import classgen.association.entity.FieldProperty;

import classgen.core.MyClassLoader;
import classgen.mapping.EntityClassInfo;
import classgen.mapping.FieldMemberInfo;
import classgen.mapping.TableInfo;
import classgen.mapping.TableWithEntityRelPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 类{@code TransfromUtil}:
 *
 * @author jiangliangzhong
 * @date 16:05 2019/10/23
 */
public class TransfromUtil {

    public static void addIntoTableWithEntityRel(BuildTaskProperity buildTaskProperity){
        //1.将类文件路径添加到classloader
        MyClassLoader classLoader = MyClassLoader.getInstance();
        classLoader.addClassFile(buildTaskProperity.getTableToClassMapping().getClassFileDir());
        //将类信息添加
        for(ClassPropertity classParam:buildTaskProperity.getTableToClassMapping().getClassPropertityList()){

            TableInfo tableInfo = new TableInfo(classParam.getTableName(),classParam.getSchema(),classParam.getDatabaseName());

            Map<String, FieldProperty> fieldMemberInfoMap = new HashMap<>(classParam.getFieldPropertyList().size());
            for(FieldProperty fieldParam:classParam.getFieldPropertyList()){
                fieldMemberInfoMap.put(fieldParam.getColumnName(),fieldParam);
            }


            EntityClassInfo entityClassInfo = new EntityClassInfo(classParam.getClassName(),fieldMemberInfoMap,
                    new ArrayList<>(fieldMemberInfoMap.values()),classLoader);

            //添加到关系池里
            TableWithEntityRelPool.getEntityClassInfoMap().put(tableInfo,entityClassInfo);
        }
    }
}
