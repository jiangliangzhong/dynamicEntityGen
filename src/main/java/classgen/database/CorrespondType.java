package classgen.database;

/**
 * 类{@code CorrespondType}:
 *
 * @author jiangliangzhong
 * @date 21:10 2019/10/22
 */
public interface CorrespondType {
    Class<?> toJavaType(String fieldTypeName);
}
