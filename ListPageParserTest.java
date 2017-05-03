package ParserTests;

import Model.Division;
import Parsers.ListPageParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Taylor Curran on 27/3/17.
 */
public class ListPageParserTest {

    ListPageParser lpp;
    PrintWriter pw;

    @Before
    public void setup() throws IOException {
        pw = new PrintWriter("tests.txt");
        lpp = new ListPageParser("https://www.tfrrs.org/archives.html?outdoor=1&year=2016", pw);
        lpp.parseUrls();
    }

    @Test
    public void testLPP(){
        assertFalse(lpp.isEmpty());
        assertEquals(88, lpp.getsize());
        assertEquals(Division.DIV_III, lpp.getDiv("https://www.tfrrs.org/lists/1758.html"));
        assertEquals(Division.DIV_II, lpp.getDiv("https://www.tfrrs.org/lists/1734.html"));
        assertEquals(Division.DIV_I, lpp.getDiv("https://www.tfrrs.org/lists/1688.html"));
        pw.close();
    }
}
