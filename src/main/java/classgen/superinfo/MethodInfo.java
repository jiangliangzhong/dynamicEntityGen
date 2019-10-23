package classgen.superinfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类{@code MethodInfo}:
 *
 * @author jiangliangzhong
 * @date 11:17 2019/10/22
 */
@Data
@AllArgsConstructor
public class MethodInfo {
    private String methodName;
    private String code;
}
