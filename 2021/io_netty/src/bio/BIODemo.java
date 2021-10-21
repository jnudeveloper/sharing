import java.io.*;
import java.util.Arrays;

public class BIODemo {
    public static void main(String[] args) {
        try {
            File file = new File("test.txt");
            // 获取文件输出流
            OutputStream outStream = new FileOutputStream(file);

            // 写信息到文件
            byte[] outputContent = "test output".getBytes();
            outStream.write(outputContent);

            // 获取文件输入流
            InputStream is = new FileInputStream(file);
            byte[] inputContent = new byte[(int)file.length()];

            // 读取文件字节信息
//            if (is.read(inputContent) > 0){
//                System.out.println(Arrays.toString(inputContent));
//            }

            // 读取文件字符信息
            InputStreamReader isr = new InputStreamReader(is);
            char[] inputChars = new char[(int)file.length()];
            if (isr.read(inputChars) > 0) {
                System.out.println(Arrays.toString(inputChars));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}