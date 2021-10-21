import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class NetAIODemo {
    AsynchronousServerSocketChannel asynServerSocketChannel;

    public static void main(String[] args) {
        NetAIODemo netAIODemo = new NetAIODemo();
        netAIODemo.run();
    }

    public void run() {
        try {
            int port = 9000;
            // 创建服务器通道
            asynServerSocketChannel = AsynchronousServerSocketChannel.open();
            // 绑定端口
            asynServerSocketChannel.bind(new InetSocketAddress(port));
            // 等待客户端请求
            asynServerSocketChannel.accept(this, new AcceptCompleteHandler());
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
}

class AcceptCompleteHandler implements CompletionHandler<AsynchronousSocketChannel, NetAIODemo> {
    // 读数据已经准备好时的操作
    @Override
    public void completed(AsynchronousSocketChannel result, NetAIODemo attachment) {
        attachment.asynServerSocketChannel.accept();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer, byteBuffer, new ReadCompleteHandler(result));
    }

    @Override
    public void failed(Throwable exc, NetAIODemo attachment) {
        exc.printStackTrace();
        System.out.println(attachment.toString());
    }

}

class ReadCompleteHandler implements CompletionHandler<Integer, ByteBuffer> {
    AsynchronousSocketChannel channel;
    public ReadCompleteHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    // 读数据完成之后的操作
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();  // 获取buffer数据前，先flip
        System.out.println(attachment.toString()); // 这里只是输出对象整体信息
        // TODO 填写具体业务逻辑，解析数据，存入数据库，获取结果等
        // 这里需要解耦
        // 返回内容给客户端，具体内容由开发者决定
        // 这里只返回success字符串
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        writeBuffer.put("success".getBytes());
        writeBuffer.flip();
        channel.write(writeBuffer);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        exc.printStackTrace();
        System.out.println(attachment.toString());
    }
}