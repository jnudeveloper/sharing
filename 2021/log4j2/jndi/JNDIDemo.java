import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

public class JNDIDemo {
    public static void main(String[] args) {
        try {
            System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
            System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");

            Hashtable<String, String> env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
            env.put(Context.PROVIDER_URL, "rmi://127.0.0.1:1234/");

            Context context = new InitialContext(env);
            Object object = context.lookupLink("rmi://127.0.0.1:1234/rce");
            System.out.println(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}