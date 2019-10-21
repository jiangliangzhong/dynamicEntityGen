package json;

import com.google.gson.Gson;

/**
 * 接口{@code IJsonParse}: json转换接口
 *
 * @author jiangliangzhong
 * @date 18:52 2019/10/18
 */
public interface IJsonParse {
    /**
     *  将类转换为json字符串
     * @date   18:53 2019/10/18
     * @author  jiangliangzhong
     * @return  none
     */
    String toJson();

    void fromJson(String jsonStr);
}
