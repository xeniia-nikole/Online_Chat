import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void createFiles() {
        Assertions.assertTrue(Server.createFiles());
    }
}