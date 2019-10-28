package classgen.database;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 类{@code DataBaseConfig}: 数据配置
 *
 * @author jiangliangzhong
 * @date 16:18 2019/10/22
 */
public class DataBaseConfig extends DruidDataSource{
    public DataBaseConfig(String driverClassName,String username, String password, String url) {
        super();
//        this.setDriverClassName(driverClassName);
        this.setUrl(url);
        this.setUsername(username);
        this.setPassword(password);
        this.setInitialSize(5);
        this.setMinIdle(1);
        this.setMaxActive(10);
    }
}
