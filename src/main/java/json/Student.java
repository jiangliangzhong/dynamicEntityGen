package json;

import java.math.BigDecimal;

/**
 * 类{@code Student}:
 *
 * @author jiangliangzhong
 * @date 19:03 2019/10/18
 */
public class Student extends Person implements IJsonParse{
    private int age;
    private String name;
    private BigDecimal bigDecimal;
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toJson() {
        return BaseGen.toJsonObject(true,this).toString();
    }

    @Override
    public String toJson(boolean superFlag) {
        return null;
    }

    @Override
    public void fromJson(String jsonStr) {

    }

}
