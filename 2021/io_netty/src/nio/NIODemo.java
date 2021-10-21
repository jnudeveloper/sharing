import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NIODemo {
    public static void main(String[] args) {
        try {
            // 创建文件test.txt
//            Files.deleteIfExists(Paths.get("test.txt"));
            if (!Files.exists(Paths.get("test.txt"))) {
                Files.createFile(Paths.get("test.txt"));
                Files.write(Paths.get("test.txt"), "init init".getBytes());
            }

            // 写入数据“test nio"到文件test.txt中
            FileChannel fileChannel = (FileChannel) Files.newByteChannel(
                    Paths.get("test.txt"), StandardOpenOption.READ, StandardOpenOption.WRITE);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            System.out.println(byteBuffer.toString());
            byteBuffer.put("test".getBytes(StandardCharsets.UTF_8));
            System.out.println(byteBuffer.toString());
            byteBuffer.flip();  // 写到文件之前要flip
            System.out.println(byteBuffer.toString());
            fileChannel.write(byteBuffer);
            System.out.println(byteBuffer.toString());

            // 读取文件test.txt的数据到buffer中
            byteBuffer.clear(); // 从文件读之前要clear
            System.out.println(byteBuffer.toString());
            fileChannel.read(byteBuffer);
            System.out.println(byteBuffer.toString());
            byteBuffer.flip(); // 从buffer中获取之前要flip
            System.out.println(byteBuffer.toString());
            while (byteBuffer.hasRemaining()) {
                System.out.println(Character.toChars(byteBuffer.get()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
