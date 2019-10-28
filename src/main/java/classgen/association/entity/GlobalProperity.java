package classgen.association.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类{@code GlobalConfigInif}: 解析所有的任务
 *
 * @author jiangliangzhong
 * @date 22:06 2019/10/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalProperity {
    /** 存放任务信息*/
    private List<BuildTaskFilePathProperity> buildTaskFilePathProperityList;
}
