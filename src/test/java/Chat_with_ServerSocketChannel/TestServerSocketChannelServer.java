package Chat_with_ServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestServerSocketChannelServer {
    static final int sleep = 10;

    public static void main(String[] args) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(3);
            int i = 0;

            while (i < 5){
                i++;
                executor.execute(new TestClientTester());
                Thread.sleep(sleep);
            }

            executor.shutdown();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

class TestClientTester implements Runnable{

    @Override
    public void run() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 55555);

            SocketChannel socketChannel = SocketChannel.open();

            try (socketChannel) {
                socketChannel.connect(socketAddress);
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
                int i = 0;
                String clientName = Thread.currentThread().getName();
                String newClient = clientName + " вошёл в чат\n";
                socketChannel.write(ByteBuffer.wrap(newClient.getBytes(StandardCharsets.UTF_8)));

                while (i < 10) {
                    i++;
                    String resultString = clientName + ": какое-то сообщение" + i;
                    socketChannel.write(ByteBuffer.wrap(resultString.getBytes(StandardCharsets.UTF_8)));
                    Thread.sleep(TestServerSocketChannelServer.sleep);

                    int bytesCount = socketChannel.read(inputBuffer);
                    System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));
                    Thread.sleep(TestServerSocketChannelServer.sleep);
                    inputBuffer.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
