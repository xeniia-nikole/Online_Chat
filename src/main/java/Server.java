import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    private static Date data = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
    final private static int portNumber = 33333;
    final private static String nameSettings = "settings.txt";
    final private static String nameLog = "file.log";
    final private static String hostname = "127.0.0.1";

    public static void main(String[] args) {
        try {

            createFiles();

            System.out.println("SYSTEM MESSAGE : Вы вошли в сетевой чат.");

            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(hostname, portNumber));

            while (true) {

                try (SocketChannel socketChannel = serverChannel.accept()) {
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 20);

                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break;
                        final String inputString = new String(inputBuffer.array(),
                                0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();

                        System.out.println(dateFormat.format(data) + ": " + inputString);
                        socketChannel.write(ByteBuffer.wrap((dateFormat.format(data) + " Новое сообщение в чате: "
                                + inputString).getBytes(StandardCharsets.UTF_8)));

                        try (FileWriter writerMsg = new FileWriter(nameLog, true)) {
                            writerMsg.write(dateFormat.format(data) + ": " + inputString + "\n");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFiles() {
        String msgSettings = "Файл settings.txt успешно создан";
        String msgLog = "Файл file.log успешно создан";

        File settingsFile = new File(nameSettings);
        try {
            if (settingsFile.createNewFile())
                System.out.println("SYSTEM MESSAGE : " + msgSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writerPortNumber = new FileWriter(nameSettings, false)) {
            writerPortNumber.write(String.valueOf(portNumber));
        } catch (Exception e) {
            e.printStackTrace();
        }

        File logFile = new File(nameLog);
        try {
            if (logFile.createNewFile())
                System.out.println("SYSTEM MESSAGE : " + msgLog);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writerLogs = new FileWriter(nameLog, true)) {
            writerLogs.write(dateFormat.format(data) + ": " + msgSettings + "\n");
            writerLogs.write(dateFormat.format(data) + ": " + msgLog + "\n");
            writerLogs.write(dateFormat.format(data) + ": чат начал свою работу!\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
