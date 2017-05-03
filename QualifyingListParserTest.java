package ParserTests;

import Model.Athlete;
import Model.Database;
import Model.Division;
import Model.Gender;
import Parsers.Exceptions.ClassException;
import Parsers.Exceptions.IDException;
import Parsers.QualifyingListParser;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Taylor Curran on 21/3/17.
 */

public class QualifyingListParserTest {

    private QualifyingListParser qlp;
    private Database db;
    private PrintWriter pw;

    @Before
    public void setup() throws FileNotFoundException {
        db = Database.getInstance();
        db.clear();
        pw = new PrintWriter("tests.txt");
        qlp = new QualifyingListParser("https://www.tfrrs.org/lists/1682.html", Division.DIV_I, pw);
    }

    @Test
    public void testAthletes() throws IOException, ClassException, IDException {
        qlp.parse();

        assertTrue(db.getAthletes().contains(new Athlete(4537007)));
        assertEquals(Gender.M,db.getAthlete(4537007).getGender());
        assertEquals("Ethan Homan",db.getAthlete(4537007).getName());
        assertEquals("Boston U.",db.getAthlete(4537007).getSchool());

        assertTrue(db.getAthletes().contains(new Athlete(5031305)));
        assertEquals(Gender.F,db.getAthlete(5031305).getGender());
        assertEquals("Molly McCabe",db.getAthlete(5031305).getName());
        assertEquals("Boston College",db.getAthlete(5031305).getSchool());

        assertEquals(282, db.getNumAthletes());;

        pw.close();
    }

}
