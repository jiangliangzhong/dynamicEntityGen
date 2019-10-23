package json;



import java.util.ArrayList;
import java.util.List;

/**
 * 类{@code Person}:
 *
 * @author jiangliangzhong
 * @date 20:46 2019/10/18
 */
public class Person implements IJsonParse{
    private List<Integer> list = new ArrayList<Integer>();
    private int id;
    private boolean flag;
    private Boolean flag0;

    public Person() {


    }



    public Boolean getFlag0() {
        return flag0;
    }

    public void setFlag0(Boolean flag0) {

        this.flag0 = flag0;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toJson() {
        return null;
    }

    @Override
    public void fromJson(String jsonStr) {

    }
}
