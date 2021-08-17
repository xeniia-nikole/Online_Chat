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
    static final Date data = new Date();
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
    final static int portNumber = 55555;
    final static String nameSettings = "settings.txt";
    final static String nameLog = "file.log";
    final static String hostname = "127.0.0.1";

    public static void main(String[] args) {
        try {

            createFiles();

            System.out.println("SYSTEM MESSAGE : Online chat started");
            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(hostname, portNumber));

            while (true) {
                ClientWorker w = new ClientWorker(serverChannel);
                Thread t = new Thread(w);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFiles() {
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
    }
}

class ClientWorker implements Runnable {
    ServerSocketChannel serverChannel;
    //List<SocketChannel> socketChannels = new ArrayList<>();

    public ClientWorker(ServerSocketChannel serverChannel){
        this.serverChannel = serverChannel;
    }


    @Override
    public void run() throws NullPointerException{
        while (true){
            try (SocketChannel socketChannel = serverChannel.accept()) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
                //socketChannels.add(socketChannel);

                while (socketChannel.isConnected()) {
                    int bytesCount = socketChannel.read(inputBuffer);
                    if (bytesCount == -1) break;
                    final String inputString = new String(inputBuffer.array(),
                            0, bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();

                    System.out.println(Server.dateFormat.format(Server.data) + ": " + inputString);
                    socketChannel.write(ByteBuffer.wrap(("\n" + Server.dateFormat.format(Server.data) + " New message: "
                            + inputString).getBytes(StandardCharsets.UTF_8)));

                    try (FileWriter writerMsg = new FileWriter(Server.nameLog, true)) {
                        writerMsg.write(Server.dateFormat.format(Server.data) + ": " + inputString + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
