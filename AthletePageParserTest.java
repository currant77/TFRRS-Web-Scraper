package ParserTests;

import Model.Athlete;
import Model.Database;
import Model.Division;
import Model.Gender;
import Parsers.AthletePageParser;
import Parsers.Exceptions.ClassException;
import Parsers.Exceptions.IDException;
import Parsers.Exceptions.NameException;
import Parsers.Exceptions.SchoolException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by Taylor Curran on 21/3/17.
 */
public class AthletePageParserTest {

    private AthletePageParser app1;
    private AthletePageParser app2;
    private AthletePageParser app3;
    private static final double TOL = 1e-6;
    private PrintWriter pw;

    @Before
    public void setup() throws ClassException, IDException, IOException {
        Database.getInstance().clear();
        pw = new PrintWriter("tests.txt");
        app1 = new AthletePageParser("https://www.tfrrs.org/athletes/5031305/Boston_College/Molly_McCabe.html", Gender.F, Division.DIV_III, pw);
        app2 = new AthletePageParser("https://www.tfrrs.org/athletes/5614679/St_Augustines/Shaquille__Dill.html", Gender.M, Division.DIV_II, pw);
        app3 = new AthletePageParser("https://www.tfrrs.org/athletes/5005214/Georgetown_DC/Joseph_White.html", Gender.M, Division.DIV_I, pw);
    }

    @Test
    public void testMultipleAddResult() throws IOException, ClassException, NameException, SchoolException {
        app1.parse();
        app2.parse();
        app2.parse();
        app1.parse();

        Athlete a1 = Database.getInstance().getAthlete(5031305);
        Athlete a2 = Database.getInstance().getAthlete(5614679);

        assertEquals(6, a2.getNumResults());
        pw.close();
    }

    @Test
    public void testAddResult() throws IOException, ClassException, NameException, SchoolException {
        app1.parse();
        app2.parse();

        assertEquals(3, Database.getInstance().getNumAthletes());
        Athlete a1 = Database.getInstance().getInstance().getAthlete(5031305);
        Athlete a2 = Database.getInstance().getInstance().getAthlete(5614679);

        assertEquals("Molly McCabe", a1.getName());
        assertEquals("Shaquille Dill", a2.getName());
        assertEquals("Boston College", a1.getSchool());
        assertEquals("St. Augustine's", a2.getSchool());

        /**
        assertEquals(a1, a1.getResults().get(a1.getNumResults()-1).getAthlete());
        assertEquals(15, a1.getResults().get(a1.getNumResults()-1).getYear());
        assertEquals("DIII All-Ohio Outdoor Track & Field Championships", a1.getResults().get(a1.getNumResults()-1).getCompetition());
        assertEquals(120.0+12.75, a1.getResults().get(a1.getNumResults()-1).getTime(), TOL);
        assertEquals(Database.EVENT, a1.getResults().get(a1.getNumResults()-1).getEvent());
        */

        assertEquals(a2, a2.getResults().get(0).getAthlete());
        assertEquals(16, a2.getResults().get(0).getYear());
        assertEquals("NCAA Division II Outdoor Track & Field Championships", a2.getResults().get(0).getCompetition());
        assertEquals(1*60 + 49.30, a2.getResults().get(0).getTime(), TOL);
        assertEquals(Database.EVENT, a2.getResults().get(0).getEvent());
        pw.close();

    }

    @Test
    public void testAddAthlete() throws IOException, ClassException, NameException, SchoolException {
        Database db = Database.getInstance();

        app1.parse();
        app2.parse();
        app3.parse();
        assertEquals(3,db.getNumAthletes());

        assertTrue(db.getAthletes().contains(new Athlete(5031305)));
        assertTrue(db.getAthletes().contains(new Athlete(5614679)));
        assertTrue(db.getAthletes().contains(new Athlete(5005214)));
        assertEquals(db.getAthlete(5031305).getYear(), "JR-3");
        assertEquals(db.getAthlete(5614679).getYear(), "SO-2");
        assertEquals(db.getAthlete(5005214).getYear(), "JR-3");
        assertEquals(db.getAthlete(5031305).getDivision(), Division.DIV_III);
        assertEquals(db.getAthlete(5614679).getDivision(), Division.DIV_II);
        assertEquals(db.getAthlete(5005214).getDivision(), Division.DIV_I);

        app1.parse();
        app2.parse();
        app3.parse();
        assertEquals(db.getNumAthletes(),3);
        pw.close();
    }
}
