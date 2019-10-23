package classgen.ass;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class TableClassAssInfo {

    private List<ClassParam> classParamList;
    /** 类文件存放路径 */
    private String classFileDir;
    private String url;
    private String userName;
    private String password;
    private String driverClassName;

}
