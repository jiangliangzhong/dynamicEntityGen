package classgen.core;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 类{@code MyClassLoader}: 自定义类加载器，用于从外部加载类
 *  单例模式，文件只有一个
 * @author jiangliangzhong
 * @date 15:58 2019/10/19
 */
public class  MyClassLoader extends ClassLoader{
    private static class Hodler{
        private static MyClassLoader myClassLoader = new MyClassLoader();
    }
    /**class文件所在路径*/
    private List<String>  classpathList;
    {
        classpathList = new CopyOnWriteArrayList<>();
    }
    public static MyClassLoader getInstance(){
        return Hodler.myClassLoader;
    }
    public void addClassFile(String filePath){
        classpathList.add(filePath);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte [] classDate=getDate(name);

            //如果不为空，则直接生产类对象
            if(classDate!=null){
                //defineClass方法将字节码转化为类
                return defineClass(name,classDate,0,classDate.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    }
    private byte[] getDate(String className) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        //类文件路径
        for(String classPath:classpathList) {
            String path = classPath + File.separatorChar +
                    className.replace('.', File.separatorChar) + ".class";
            try {
                in = new FileInputStream(path);
                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                return out.toByteArray();
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
                System.out.println("没有找到该路径："+path+",尝试下一个路径");

            } finally {
                assert in != null;
                in.close();
                assert out != null;
                out.close();
            }
        }
        return null;
    }
}
