import Chat_with_ServerSocketChannel.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void readPortNumber() {
        int port = Client.readPortNumber();

        int expected = 55555;

        Assertions.assertEquals(expected, port);
    }

}