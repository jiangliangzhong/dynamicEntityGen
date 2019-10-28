package classgen.core.configuration;


import classgen.association.entity.*;
import classgen.core.FieldAnaylzeUtil;
import classgen.database.DataBaseConfig;
import classgen.entity.JdbcTypeE;
import classgen.exception.MissingAttributeException;
import classgen.superinfo.EntitySuperInfo;
import classgen.superinfo.InterfaceInfo;
import classgen.superinfo.MethodInfo;
import classgen.superinfo.SuperClassInfo;
import classgen.type.transform.AliasManager;
import classgen.type.transform.TfTypeHanderRegistry;
import classgen.xmldeal.XmlUtil;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类{@code TaskDetailConfiguration}: 生成任务详情xml文件加载
 * 每次加载一个任务
 *
 * @author jiangliangzhong
 * @date 10:57 2019/10/22
 */

public class BuildTaskConfiguration {
    private static Logger logger;
    private BuildTaskFilePathProperity filePathProperity;
    /**
     * 存放该任务下的数据库连接
     */
    @Getter
    private DataBaseConfig dataBaseConfig;
    static{
        Logger.getLogger(BuildTaskConfiguration.class.getName());
    }
    public BuildTaskConfiguration(BuildTaskFilePathProperity buildTaskFilePathProperity) {
        if( buildTaskFilePathProperity.getTableClassAssInfoFilePath() == null||buildTaskFilePathProperity.getTableClassAssInfoFilePath().isEmpty()){
            throw new RuntimeException("异常：任务加载文件路径不能为null或者爲空");
        }
        this.filePathProperity = buildTaskFilePathProperity;
    }
    /**
     * 从文件中解析任务细节
     * @date   11:03 2019/10/22
     * @author  jiangliangzhong
     * @return  任务细节类
     */
    public BuildTaskProperity analyzeGenerateTaskDetail() throws SQLException {
        EntitySuperInfo entitySuperInfo = this.anylyzeEntitySuperInfo();
        TableToClassMapping tableToClassMapping = this.anylyzeTableClassAssInfo();
        return new BuildTaskProperity(entitySuperInfo,tableToClassMapping);
    }
    /**
     * 解析继承信息
     * @date   11:04 2019/10/22
     * @author  jiangliangzhong
     * @return  继承信息
     */
    private EntitySuperInfo anylyzeEntitySuperInfo()  {
        if(this.filePathProperity.getSuperInfoFilePath() == null || this.filePathProperity.getSuperInfoFilePath().isEmpty()){
            return  null;
        }
        XmlUtil xmlUtil;
        try {
            xmlUtil = new XmlUtil(this.filePathProperity.getSuperInfoFilePath());
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：SuperInfoFile文件路径错误，请重新设置");
        }
        String superClassName = null;
        List<InterfaceInfo> interfaceInfos = new ArrayList<>();
        for(Element e1: xmlUtil.getRoot().elements()){
            //拿到超类信息
            if("superClass".equals(e1.getName())){
                superClassName = e1.attributeValue("className");
            }else if("interfaces".equals(e1.getName())){

                //遍历接口信息
                for(Element e2:e1.elements()){
                    //拿到接口信息
                    if("interface".equals(e2.getName())){
                        List<MethodInfo> methodInfoList = new ArrayList<>();
                        //遍历方法集合
                        for(Element e3: e2.elements()){
                            //拿到方法信息
                            if("method".equals(e3.getName())){
                                //默认为空,如果为空，则不实现该接口
                                String code="";
                                for(Element e4:e3.elements()){
                                    if("code".equals(e4.getName())){
                                        code = e4.getText();
                                    }
                                }
                                if(code.isEmpty()){
                                    logger.warn("警告：" +e3.attributeValue("MName")+"方法实现为空字符串,如果没有返回值，需要使用return;");
                                }
                                methodInfoList.add(new MethodInfo(e3.attributeValue("MName"), code));
                            }
                        }
                        interfaceInfos.add(new InterfaceInfo(methodInfoList, e2.attributeValue("IName")));
                    }
                }
            }
        }
        if(superClassName == null){
            return new EntitySuperInfo(null,interfaceInfos);
        }
        return new EntitySuperInfo(new SuperClassInfo(superClassName),interfaceInfos);
    }
    /**
     * 解析表和类关联信息
     * @date   11:04 2019/10/22
     * @author  jiangliangzhong
     * @return  表和类关联信息集合
     */
    private TableToClassMapping anylyzeTableClassAssInfo() throws SQLException {
        XmlUtil xmlUtil;
        try {
            xmlUtil = new XmlUtil(this.filePathProperity.getTableClassAssInfoFilePath());
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("异常：TableClassAssInfoFile文件路径错误，请重新设置");
        }
        //设置数据库表信息, 一定要有
        String url = xmlUtil.getRoot().attributeValue("url");
        String userName = xmlUtil.getRoot().attributeValue("userName");
        String password = xmlUtil.getRoot().attributeValue("password");
        String driverClassName = xmlUtil.getRoot().attributeValue("driverClassName");
        //默认的类文件存放路径
        String classFileDir = "System.getProperty(\"user.dir\")+\"\\\\class-file\"";
        //构建数据库连接
        if(dataBaseConfig ==null) {
            dataBaseConfig = new DataBaseConfig(driverClassName, userName, password,url );
        }
        //存放每张表和类对应关系
        List<ClassPropertity> classPropertities = new ArrayList<>();
        DatabaseMetaData databaseMetaData = dataBaseConfig.getConnection().getMetaData();
        //遍历<tables>和<classFileDir>
        for(Element e1: xmlUtil.getRoot().elements()){
            //得到类文件所在路径,默认路径
            if("classFileDir".equals(e1.getName())){
                classFileDir = e1.getText();
            }else if("tables".equals(e1.getName())){
                //遍历<table>
                for(Element e2 :e1.elements()){
                    String tableName = e2.attributeValue("tableName");
                    if(tableName == null){
                        throw new MissingAttributeException("异常：缺失tableName属性");
                    }
                    String[] tableInfos = tableName.split(databaseMetaData.getCatalogSeparator());
                    //防止用户误将全称写入表名
                    tableName = tableInfos.length ==0 ? tableName :tableInfos[tableInfos.length -1];

                    String className = e2.attributeValue("className");
                    if(className == null){
                        throw new MissingAttributeException("异常：缺失className属性");
                    }
                    String schema = e2.attributeValue("schema");
                    String databaseName = e2.attributeValue("databaseName");

                    //parseBoolean会将除“true”之外的内容全部赋值为false
                    boolean autoMatch = Boolean.parseBoolean(e2.attributeValue("autoMatch"));
                    //保存字段和属性对应关系
                    List<FieldProperty> fieldProperties = new ArrayList<>();
                    //自动生成对应关系
                    if(autoMatch){
                        Connection connection = dataBaseConfig.getConnection();
                        //自动得到字段对应信息
                        FieldAnaylzeUtil.fieldAutoAnalyze(connection,fieldProperties,databaseName,schema,tableName);
                    }
                    else{
                        //手动生成
                        //遍历field
                        for(Element e3:e2.elements()){
                            if("field".equals(e3.getName())){
                                String columnName = e3.attributeValue("columnName");
                                if(columnName == null|| columnName.isEmpty()){
                                    throw new MissingAttributeException("异常：fieldName属性不能为空");
                                }
                                String property = e3.attributeValue("property");
                                if(property == null || property.isEmpty()){
                                    property = FieldAnaylzeUtil.formatFieldName(columnName);
                                }
                                //jdbcType
                                JdbcTypeE realJdbcType = JdbcTypeE.forCode(databaseMetaData.getColumns(databaseName,
                                        schema,tableName,columnName).getInt("DATA_TYPE "));
                                String jdbcTypeName = e3.attributeValue("jdbcType");
                                if(jdbcTypeName !=null &&!jdbcTypeName.isEmpty()){
                                    JdbcTypeE jdbcType = JdbcTypeE.valueOf(jdbcTypeName);
                                    if(jdbcType != realJdbcType){
                                        throw new RuntimeException("配置文件的jdbc类型和数据库中的jdbc类型不一致");
                                    }
                                }
                                //javaType
                                String javaType = e3.attributeValue("javaType");
                                Class<?> javaTypeClass;
                                if(javaType !=null &&!javaType.isEmpty()){
                                    //判断是否有别名
                                    javaTypeClass = AliasManager.resolveAlias(javaType);
                                    if(javaTypeClass == null){
                                        try {
                                            javaTypeClass = Class.forName(javaType);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                            throw new RuntimeException("无法加载"+javaType+"这个类");
                                        }
                                    }
                                }else{
                                    //javaType的属性无效，则自动匹配
                                    javaTypeClass = (Class<?>) ((ParameterizedType) TfTypeHanderRegistry.getTypeHanderMapByJdbcType(realJdbcType)
                                            .getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                                }




                                String methodName = property.substring(0,1).toUpperCase() + property.substring(1);
                                String getMethodName;
                                if(javaTypeClass == boolean.class){
                                    getMethodName = "is"+ methodName;
                                }else{
                                    getMethodName = "get"+methodName;
                                }
                                String setMethodName = "set"+methodName;
                                fieldProperties.add(new FieldProperty(columnName,realJdbcType,javaTypeClass,property,setMethodName,getMethodName));
                            }
                        }
                    }
                    classPropertities.add(new ClassPropertity(tableName,schema,databaseName,className, autoMatch, fieldProperties));
                }
            }
        }
        return new TableToClassMapping(classPropertities,classFileDir,url,userName,password,driverClassName);
    }


}
