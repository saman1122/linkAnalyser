package fr.insta.saman;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private PrintStream sysOut;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        sysOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void revertStreams() {
        System.setOut(sysOut);
        System.out.print(outContent.toString());
    }

    @Test
    public void testLip6() throws URISyntaxException {
        String url ="https://www-apr.lip6.fr/~buixuan/mrinsta2018";
        App.main(new String[] {url});

        Assert.assertTrue(!outContent.toString().isEmpty());
    }
}
