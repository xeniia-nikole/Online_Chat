package Chat_with_ServerSocket;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Client {
    final private static int portNumber = 33333;
    final private static String hostname = "127.0.0.1";

    public static void main(String[] args) {

        try (Socket socket = new Socket(hostname, portNumber);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("SYSTEM MESSAGE : connected");
            System.out.println();
            System.out.println("SYSTEM MESSAGE : writing channel & reading channel initialized");
            System.out.println("SYSTEM MESSAGE : Введите ваше имя...  ");
            String nickname = scanner.nextLine();
            System.out.println("SYSTEM MESSAGE : Добро пожаловать, " + nickname + "!\n");
            out.write("SYSTEM MESSAGE : " + nickname + " entered the chatroom\n");

            String clientCommand;
            while (true) {
                System.out.println("Введите сообщение или '/exit': ");
                clientCommand = scanner.nextLine();

                if (clientCommand.equalsIgnoreCase("/exit")) {
                    System.out.println("SYSTEM MESSAGE : connections were killed");
                    break;
                }
                out.write( new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ").
                        format(Calendar.getInstance().getTime()) + nickname + " : " + clientCommand + "\n");
                out.flush();

                String input;
                    try {
                        if ((input = in.readLine()) != null) {
                            System.out.println(input);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            System.out.println("Closing connections & channels on clintSide - DONE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

