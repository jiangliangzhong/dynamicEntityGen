package classgen.database;

import lombok.Getter;

/**
 * 数据库类型枚举类
 * @date   20:29 2019/10/22
 * @author  jiangliangzhong
 */
public enum DBType {
    /** oracle数据库*/
    ORACLE("oracle",null,null),
    /** mysql */
    MYSQL("mysql","select COLUMN_NAME,DATA_TYPE from information_schema.COLUMNS " +
            "where table_schema = ? and table_name = ?  ",new MysqlCorrespondType()),
    /** postgre */
    POSTGRE("postgre","",new MysqlCorrespondType()),
    /** 未知数据库 */
    UNKNOWNDB("unknow",null,null);
    @Getter
    private final String databaseName;
    @Getter
    private final String selectSql;
    @Getter
    private CorrespondType correspondType;
    DBType(String databaseName,String selectSql,CorrespondType correspondType ) {
        this.databaseName = databaseName;
        this.selectSql = selectSql;
        this.correspondType = correspondType;
    }
    public DBType valueof(String driverClassName){
        if(driverClassName.contains("oracle")){
            return ORACLE;
        }else if(driverClassName.contains("mysql")){
            return MYSQL;
        }else if(driverClassName.contains("postgre")){
            return POSTGRE;
        }else{
            return UNKNOWNDB;
        }
    }
}