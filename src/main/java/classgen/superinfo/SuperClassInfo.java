package classgen.superinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类{@code SuperClassInfo}: 继承的超类信息
 *
 * @author jiangliangzhong
 * @date 11:09 2019/10/22
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuperClassInfo {
    /** 超类完整名字 */
    private String className;
}
