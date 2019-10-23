package classgen.ass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 类{@code GenerateTask}:
 *
 * @author jiangliangzhong
 * @date 10:15 2019/10/22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateTaskInfo {
    private String superInfoFilePath;
    private String tableClassAssInfoFilePath;
}
