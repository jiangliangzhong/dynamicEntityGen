package classgen.superinfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 类{@code EntitySuperInfo}: 为实体类配置继承接口和类
 *
 * @author jiangliangzhong
 * @date 20:03 2019/10/20
 */
@AllArgsConstructor
@Data
public class EntitySuperInfo {
    private SuperClassInfo superClassInfo;
    private List<InterfaceInfo> interfaceInfoList;
}
