package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @description:
 * @author: raven
 * @create: 2020-04-14 19:57
 **/
public class CmdUtil {
    public static String exeCmd(String commandStr) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {


        exeCmd("E:\\Program Files\\rabbitMQ\\rabbitmq_server-3.8.3\\sbin\\rabbitmqctl.bat add_user test2 test2");
        exeCmd(" E:\\Program Files\\rabbitMQ\\rabbitmq_server-3.8.3\\sbin\\rabbitmqctl.bat  set_permissions -p / test2 \".*\" \".*\" \".*\"");
    }
}
