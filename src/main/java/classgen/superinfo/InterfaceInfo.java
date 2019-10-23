package classgen.superinfo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 类{@code InterfaceInfo}: 接口信息
 *
 * @author jiangliangzhong
 * @date 11:13 2019/10/22
 */
@Data
@AllArgsConstructor
public class InterfaceInfo {
    private List<MethodInfo> methodInfoList;
    private String interfaceName;
}
