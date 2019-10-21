package json;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * 类{@code Test1}:
 *
 * @author jiangliangzhong
 * @date 19:04 2019/10/18
 */
public class Test1 {
    public static void main(String[] args) throws NoSuchFieldException {
        Student student= new Student();
        student.setAge(1);
        student.setName("sdf");
        student.setId(2);
        student.setBigDecimal(new BigDecimal(1232321321));
        Gson gson = new Gson();
//        System.out.println(gson.toJson(student));
        Student stu= gson.fromJson("{\"age\":1,\"name\":\"sdf\",\"bigDecimal\":1232321321,\"list\":[0,2]}",Student.class);
        Field[] fields = stu.getClass().getDeclaredFields();
        Field[] fields1 = stu.getClass().getFields();
        System.out.println(student.toJson());
        ;
    }
}
