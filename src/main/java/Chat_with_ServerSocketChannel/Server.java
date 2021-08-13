package Chat_with_ServerSocketChannel;

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
    private static final Date data = new Date();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
    final private static int portNumber = 55555;
    final private static String nameSettings = "settings.txt";
    final private static String nameLog = "file.log";
    final private static String hostname = "127.0.0.1";

    public static void main(String[] args) {
        try {

            createFiles();

            System.out.println("SYSTEM MESSAGE : Online chat started");

            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(hostname, portNumber));

            while (true) {

                try (SocketChannel socketChannel = serverChannel.accept()) {
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2048);

                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break;
                        final String inputString = new String(inputBuffer.array(),
                                0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();

                        System.out.println(dateFormat.format(data) + ": " + inputString);
                        socketChannel.write(ByteBuffer.wrap(("\n" + dateFormat.format(data) + " New message: "
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

    public static boolean createFiles() {
        String msgSettings = "File settings.txt was successfully created";
        String msgLog = "File file.log was successfully created";

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
            writerLogs.write(dateFormat.format(data) + ": Online chat started\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean result = false;
        if (settingsFile.isFile() && logFile.isFile()) result = true;
        return result;
    }
}