package classgen.adpaters;

import classgen.ass.ClassParam;
import classgen.ass.FieldParam;
import classgen.ass.GenerateTaskDetail;
import classgen.core.MyClassLoader;
import classgen.mapass.EntityClassInfo;
import classgen.mapass.FieldMemberInfo;
import classgen.mapass.TableInfo;
import classgen.mapass.TableWithEntityRelPool;
import sun.reflect.FieldInfo;

import java.lang.reflect.Field;
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

    public static void addIntoTableWithEntityRel(GenerateTaskDetail generateTaskDetail){
        //1.将类文件路径添加到classloader
        MyClassLoader classLoader = MyClassLoader.getInstance();
        classLoader.addClassFile(generateTaskDetail.getTableClassAssInfo().getClassFileDir());
        //将类信息添加
        for(ClassParam classParam:generateTaskDetail.getTableClassAssInfo().getClassParamList()){

            TableInfo tableInfo = new TableInfo(classParam.getTableName(),classParam.getSchema());


            Map<String, FieldMemberInfo> fieldMemberInfoMap = new HashMap<>(classParam.getFieldParamList().size());
            for(FieldParam fieldParam:classParam.getFieldParamList()){
                fieldMemberInfoMap.put(fieldParam.getFieldName(), new FieldMemberInfo(fieldParam.getFieldName(),fieldParam.getClassFieldName(),fieldParam.getFieldType(),
                        fieldParam.getSetterName(),fieldParam.getGetterName()));
            }


            EntityClassInfo entityClassInfo = new EntityClassInfo(classLoader,classParam.getClassName(),fieldMemberInfoMap,
                    new ArrayList<>(fieldMemberInfoMap.values()));

            //添加到关系池里
            TableWithEntityRelPool.getEntityClassInfoMap().put(tableInfo,entityClassInfo);
        }
    }
}