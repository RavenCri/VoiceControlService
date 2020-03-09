package util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Auther: raven
 * @Date: 2019/11/12 23:01
 * @Description:
 */
public class FileUtil {

    public static String readClassPathFile(String name) {

        try {
            ClassPathResource classPathResource = new ClassPathResource("word.json");
            InputStream inputStream = classPathResource.getInputStream();

            byte []b = new byte[inputStream.available()];
            inputStream.read(b);
            return  new String(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("word.json");
        System.out.println(classPathResource.getInputStream());

    }
}