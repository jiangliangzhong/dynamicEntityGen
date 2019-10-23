package classgen.ass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类{@code GlobalConfigInif}:
 *
 * @author jiangliangzhong
 * @date 22:06 2019/10/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalConfigInfo {
    /** 存放任务信息*/
    private List<GenerateTaskInfo> generateTaskList;
}
