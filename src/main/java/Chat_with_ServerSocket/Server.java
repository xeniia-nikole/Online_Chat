package Chat_with_ServerSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static final Date data = new Date();
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
    final private static int portNumber = 33333;
    final private static String nameSettings = "settings.txt";
    final private static String nameLog = "file.log";
    static ExecutorService executeIt =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(portNumber)) {
            createFiles();
            System.out.println("SYSTEM MESSAGE : Welcome to out chatroom!");

            while (!server.isClosed()) {
                Socket client = server.accept();
                executeIt.execute(new MonoThreadClient(client));
                System.out.println("SYSTEM MESSAGE : new Client connection accepted");
            }

            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean createFiles() {
        boolean result = false;
        String msgSettings = "File settings.txt was successfully created";
        String msgLog = "File file.log was successfully created";

        File settingsFile = new File(nameSettings);
        File logFile = new File(nameLog);
        try {
            if (settingsFile.createNewFile())
                System.out.println("SYSTEM MESSAGE : " + msgSettings);
            if (logFile.createNewFile())
                System.out.println("SYSTEM MESSAGE : " + msgLog);

            try (FileWriter writerPortNumber = new FileWriter(nameSettings, false)) {
                writerPortNumber.write(String.valueOf(portNumber));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (settingsFile.isFile() && logFile.isFile()) result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writerLogs = new FileWriter(nameLog, true)) {
            writerLogs.append(Server.dateFormat.format(Server.data))
                    .append(": ")
                    .append(msgSettings)
                    .append('\n')
                    .flush();
            writerLogs.append(Server.dateFormat.format(Server.data))
                    .append(": ")
                    .append(msgLog)
                    .append('\n')
                    .flush();
            writerLogs.append(Server.dateFormat.format(Server.data))
                    .append(": ")
                    .append("Online chatroom started")
                    .append('\n')
                    .flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
