package Chat_with_ServerSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        try (ServerSocket servSocket = new ServerSocket(portNumber)) {
            if (createFiles())
            System.out.println("SYSTEM MESSAGE : Welcome to out chatroom!");

            while (!servSocket.isClosed()) {
                Socket client = servSocket.accept();
                executeIt.execute(new MonoThreadClient(client));
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
            writerLogs.append(dateFormat.format(data))
                    .append(": ")
                    .append(msgSettings)
                    .append('\n')
                    .flush();
            writerLogs.append(dateFormat.format(data))
                    .append(": ")
                    .append(msgLog)
                    .append('\n')
                    .flush();
            writerLogs.append(dateFormat.format(data))
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

class MonoThreadClient implements Runnable {

    private static Socket clientDialog;

    public MonoThreadClient(Socket client) {
        MonoThreadClient.clientDialog = client;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
             PrintWriter out = new PrintWriter(clientDialog.getOutputStream(), true)) {

            while (!clientDialog.isClosed()) {
                String entry;
                while ((entry = in.readLine()) != null) {

                    if (entry.equalsIgnoreCase("/exit")) {
                        systemMSG("SYSTEM MESSAGE : Client initialize connections suicide ...");
                        systemMSG("SYSTEM MESSAGE : Server reply - " + entry + " - OK");
                        break;
                    }
                }
                String reply = "Новое сообщение в чате\n" +
                        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").
                                format(Calendar.getInstance().getTime()) + entry;
                if (systemMSG(reply)) out.write(reply);
                out.flush();
            }

            systemMSG("Client disconnected");
            systemMSG("Closing connections & channels");

            clientDialog.close();

            systemMSG("Closing connections & channels - DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean systemMSG(String msg) {
        try (FileWriter writer = new FileWriter("file.log", true)) {
            writer.append(msg)
                    .append('\n')
                    .flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

}



