import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private static Date data = new Date();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy.MM.dd");
    final private static String nameSettings = "settings.txt";
    final private static String nameLog = "file.log";
    final private static String hostname = "127.0.0.1";

    public static void main(String[] args) {
        try {
            int portNumber = readPortNumber();

            InetSocketAddress socketAddress = new InetSocketAddress(hostname, portNumber);

            SocketChannel socketChannel = SocketChannel.open();

            socketChannel.connect(socketAddress);

            try (Scanner scanner = new Scanner(System.in)) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
                String inputString;
                String clientName = getClientName();
                String newClient = clientName + " вошёл в чат";
                socketChannel.write(ByteBuffer.wrap(newClient.getBytes(StandardCharsets.UTF_8)));
                inputBuffer.clear();
                while (true) {
                    System.out.print(clientName + ", введите Ваше сообщение\n " +
                            "(для выхода из чата введите команду '/exit'): ");
                    inputString = scanner.nextLine();
                    if ("/exit".equals(inputString)) {
                        System.out.println(clientName + ", до новых встреч!");
                        String byClient = clientName + " покинул чат";
                        System.out.println(byClient);
                        socketChannel.write(ByteBuffer.wrap(byClient.getBytes(StandardCharsets.UTF_8)));
                        inputBuffer.clear();
                        break;
                    }
                    String resultString = clientName + ": " + inputString;
                    socketChannel.write(ByteBuffer.wrap(resultString.getBytes(StandardCharsets.UTF_8)));
                    Thread.sleep(3000);

                    int bytesCount = socketChannel.read(inputBuffer);
                    System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));
                    System.out.println();
                    inputBuffer.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                socketChannel.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readPortNumber() {
        String portNumber = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(nameSettings))) {
            String s;
            while ((s = reader.readLine()) != null) {
                portNumber = s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(portNumber);
    }

    public static String getClientName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Добро пожаловать! Введите ваше имя: ");
        String name = scanner.nextLine();
        return name;
    }
}