import com.zzyl.common.constant.Constants;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.FileWriter;
import java.util.Properties;

public class VelocityDemoTest {
    public static void main(String[] args)throws Exception{
        Properties p = new Properties();
        // 加载velocity资源,告诉模板引擎模板文件存放的位置
        p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //定义字符集
        p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8);
        //初始化引擎
        Velocity.init(p);
        //创建上下文对象
        VelocityContext context = new VelocityContext();
        //设置模板变量
        context.put("message", "加油少年！！！！");
        //获取模板
        Template template = Velocity.getTemplate("vms/index.html.vm", "UTF-8");
        // 输出
        FileWriter fileWriter = new FileWriter("zzyl-generator\\src\\main\\resources\\index.html");
        // 合并模板和数据模型
        template.merge(context, fileWriter);
        // 关闭流
        fileWriter.close();
    }
}
