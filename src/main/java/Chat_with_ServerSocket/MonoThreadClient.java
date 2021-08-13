package Chat_with_ServerSocket;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MonoThreadClient implements Runnable {

    private static Socket clientDialog;

    public MonoThreadClient(Socket client) {
        MonoThreadClient.clientDialog = client;
    }

    @Override
    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()))) {

            while (clientDialog.isConnected()) {

                String entry = "\n" + in.readLine();

                writeFileServer(entry);

                if (entry.equalsIgnoreCase("/exit")) {
                    systemMSG("SYSTEM MESSAGE : Client initialize connections suicide ...");
                    systemMSG("SYSTEM MESSAGE : Server reply - " + entry + " - OK");
                    Thread.sleep(3000);
                    break;
                }

                out.flush();

            }

            writeFileServer("Client disconnected");
            writeFileServer("Closing connections & channels");

            clientDialog.close();

            writeFileServer("Closing connections & channels - DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void systemMSG(String msg) {
        writeFileServer(msg);
        System.out.println(msg);
    }

    protected static void writeFileServer (String msg){
        try (FileWriter writer = new FileWriter("file.log", true)) {
            writer.append(Server.dateFormat.format(Server.data))
                    .append(": ")
                    .append(msg)
                    .append('\n')
                    .flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}


