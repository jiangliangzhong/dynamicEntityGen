package classgen.association.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 类{@code TableClassAssInfo}:
 *
 * @author jiangliangzhong
 * @date 16:12 2019/10/22
 */
@AllArgsConstructor
@Getter
public class TableToClassMapping{

    /**
     * 类配置信息
     */
    private List<ClassPropertity> classPropertityList;
    /** 类文件存放路径 */
    private String classFileDir;
    /**
     * 数据库连接信息
     */
    private String url;
    private String userName;
    private String password;
    private String driverClassName;

}
