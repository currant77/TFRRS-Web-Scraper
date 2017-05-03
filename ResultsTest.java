package ModelTests;

import Model.Athlete;
import Model.Exceptions.DateException;
import Model.Exceptions.TimeException;
import Model.Result;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Taylor Curran on 15/3/17.
 */
public class ResultsTest {

    private static final double TOL = 1e-6;
    private String d1;
    private String d2;
    private String d3;
    private String t1;
    private String t2;
    private Result r1;
    private String c1;
    private String c2;
    private Athlete a1;

    @Before
    public void setup() throws TimeException, DateException {
        d1 = "04-09-16";
        d2 = "03/11-03/12/16";
        d3 = "02/24-02/25/15";
        t1 = "2:05.99";
        t2 = "2:02.75";
        c1 ="Port Hardy Open";
        c2 = "NCAA World Cup";
        a1 = new Athlete(1234567);
        r1 = new Result(a1, d1, c1, "800m", t1);
    }

    @Test
    public void testConstructor(){
        assertEquals(a1, r1.getAthlete());
        assertEquals(16, r1.getYear());
        assertEquals(c1, r1.getCompetition());
        assertEquals("800m", r1.getEvent());
        assertEquals(r1.getTime(), 2*60 + 05.99, TOL);
    }

    @Test
    public void testGetYear() throws DateException {
        assertEquals(Result.getYear(d1), 16);
        assertEquals(Result.getYear(d2), 16);
    }

    @Test
    public void testGetTime() throws TimeException {
        assertEquals(Result.getTime(t1), 2*60 + 5.99, TOL);
        assertEquals(Result.getTime(t2), 2*60 + 2.75, TOL);
    }
}
