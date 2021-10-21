import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class NetBIODemo {
    public static void main(String [] args) {
        int port = 9000;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();  // 阻塞
                // 具体的应用操作，根据应用层协议和业务逻辑进行处理。

                // 假设应用层协议是以行来区分包。业务逻辑是读取客户端发来的一行信息并打印出来
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // SocketInputStream
                String line = bufferedReader.readLine();  // 数据未准备好，read会阻塞
                System.out.println(line);

                // 假设业务逻辑是每次打印完成返回字符串success
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // SocketOutputStream
                bufferedWriter.write("success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
        }
    }
}
