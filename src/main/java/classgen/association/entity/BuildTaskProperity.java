package classgen.association.entity;

import classgen.superinfo.EntitySuperInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 类{@code GenerateTaskDetail}: 生成任务细节，对应<task>标签
 *
 * @author jiangliangzhong
 * @date 10:53 2019/10/22
 */
@Getter
@AllArgsConstructor
public class BuildTaskProperity {
    private EntitySuperInfo entitySuperInfo;
    /** 任务中，每张表和类的对应关系 */
    private TableToClassMapping tableToClassMapping;
}
