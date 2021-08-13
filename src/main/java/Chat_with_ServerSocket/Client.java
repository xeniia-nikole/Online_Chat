package Chat_with_ServerSocket;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client {
    final private static int portNumber = 33333;
    final private static String hostname = "127.0.0.1";

    public static void main(String[] args) {

        try (Socket socket = new Socket(hostname, portNumber);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            System.out.println("SYSTEM MESSAGE : connected");
            System.out.println();
            System.out.println("SYSTEM MESSAGE : writing channel & reading channel initialized");
            System.out.println("SYSTEM MESSAGE : Введите ваше имя -  ");
            String nickname = br.readLine();
            System.out.println("SYSTEM MESSAGE : Привет, " + nickname + "!\n");
            out.write(nickname + " entered the chatroom");

            while (true) {
                    System.out.println("Введите сообщение в чат или '/exit': ");
                    String clientCommand = br.readLine();
                    out.write(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").
                            format(Calendar.getInstance().getTime()) + nickname + ": " + clientCommand + "\n");
                    out.flush();

                    if (clientCommand.equalsIgnoreCase("/exit")) {
                        System.out.println("SYSTEM MESSAGE : connections were killed");

                        if (!in.readLine().isEmpty()) {
                            System.out.println("SYSTEM MESSAGE : reading server messages...");
                            String incoming = in.readLine();
                            System.out.println(incoming);
                        }
                        break;
                    } else {
                        if (!in.readLine().isEmpty()) {
                            System.out.println("SYSTEM MESSAGE : reading server messages...");
                            String incoming = in.readLine();
                            System.out.println(incoming);
                        }
                    }
            }

            System.out.println("Closing connections & channels on clintSide - DONE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

