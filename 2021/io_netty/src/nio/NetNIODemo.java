import javax.management.remote.rmi._RMIConnection_Stub;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class NetNIODemo {
    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(9000));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select(1000); // 不设置参数就一直阻塞
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    if (selectionKey.isAcceptable()) {
                        // 获取新的连接请求
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel sc = ssc.accept();
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                    if (selectionKey.isReadable()) {
                        // 读数据
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int readNum = sc.read(byteBuffer);
                        byteBuffer.flip();  // 获取buffer数据前，先flip
                        System.out.println(byteBuffer.toString()); // 这里只是输出对象整体信息
                        // TODO 填写具体业务逻辑，解析数据，存入数据库，获取结果等
                        // 这里需要解耦
                        // 返回内容给客户端，具体内容由开发者决定
                        // 这里只返回success字符串
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put("success".getBytes());
                        writeBuffer.flip();
                        sc.write(writeBuffer);
                    }
                }
                // 其他定时任务，非客户端的请求内容
                // 这里也有耦合问题
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
