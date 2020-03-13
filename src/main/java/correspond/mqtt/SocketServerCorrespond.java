package correspond.mqtt;

public class SocketServerCorrespond {
    private void sendInstruct(String instruct) {
        String classPath = getClass().getClassLoader().getResource("").getPath().substring(1);
        try {
            Runtime runtime = Runtime.getRuntime();
            Process exec = runtime.exec(String.format("python %s/client.py --instruct=%s", classPath, instruct));
            exec.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
