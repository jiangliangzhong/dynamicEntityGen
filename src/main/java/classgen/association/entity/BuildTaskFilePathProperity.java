package classgen.association.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 类{@code GenerateTask}: 存放本次生成任务的配置文件路径
 *
 * @author jiangliangzhong
 * @date 10:15 2019/10/22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuildTaskFilePathProperity {
    private String superInfoFilePath;
    private String tableClassAssInfoFilePath;
}
