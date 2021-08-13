import Chat_with_ServerSocketChannel.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ServerTest {

    @Test
    void createFiles() {
        Assertions.assertTrue(Server.createFiles());
    }
}