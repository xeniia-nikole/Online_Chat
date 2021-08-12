import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void readPortNumber() {
        int port = Client.readPortNumber();

        int expected = 55555;

        Assertions.assertEquals(expected, port);
    }

}