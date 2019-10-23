package classgen.ass;

import classgen.superinfo.EntitySuperInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * 类{@code GenerateTaskDetail}: 生成任务细节
 *
 * @author jiangliangzhong
 * @date 10:53 2019/10/22
 */
@Getter
@AllArgsConstructor
public class GenerateTaskDetail {
    private EntitySuperInfo entitySuperInfo;
    /** 任务中，每张表和类的对应关系 */
    private TableClassAssInfo tableClassAssInfo;
}
