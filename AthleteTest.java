package ModelTests;

import Model.Athlete;
import Model.Division;
import Model.Exceptions.DateException;
import Model.Exceptions.TimeException;
import Model.Gender;
import Model.Result;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Taylor Curran on 20/3/17.
 */
public class AthleteTest {

    private String name1;       private String name2;
    private String sch1;        private String sch2;
    private Athlete a1;         private Athlete a2;
    private Result r1;          private Result r2;

    @Before
    public void setup() throws TimeException, DateException {
        name1 = "Bob";
        name2 = "Jim";
        sch1 = "UCONN";
        sch2 = "NYU";
        a1 = new Athlete(5993679);
        a2 = new Athlete(1234567);
        r1 = new Result(a1, "02/24-02/25/16", "BIGMEET","800m", "2:05.99");
        r2 = new Result(a2, "02/25/15", "BIGMEET", "800m", "2:05.99");
    }

    @Test
    public void testGender(){
        assertNull(a1.getGender());
        a1.setGender(Gender.M);
        assertEquals(a1.getGender(), Gender.M);
        a1.setGender(Gender.F);
        assertEquals(a1.getGender(),Gender.F);
    }

    @Test
    public void testAddResult(){
        a1.addResult(r1);
        assertEquals(a1.getResults().size(),1);
        assertTrue(a1.getResults().contains(r1));

        a1.addResult(r1);
        assertEquals(a1.getResults().size(),1);
        assertTrue(a1.getResults().contains(r1));

        a1.addResult(r2);
        assertEquals(a1.getResults().size(),2);
        assertTrue(a1.getResults().contains(r1));
        assertTrue(a1.getResults().contains(r2));
        assertEquals(r2.getAthlete(), a1);
    }

    @Test
    public void testEqualsAndHashCode(){
        assertEquals(a1, new Athlete(5993679));
        assertNotEquals(a1, new Athlete(1234567));
        assertEquals(a1.hashCode(), new Athlete(5993679).hashCode());
        assertNotEquals(a1.hashCode(), new Athlete(1234567).hashCode());
        assertNotEquals(a1.hashCode(), new Athlete(1112223).hashCode());
    }

    @Test
    public void testSetters(){
        assertTrue(a2.getResults().isEmpty());
        a2.setSchool(sch1);
        assertEquals(sch1, a2.getSchool());
        assertEquals("",a2.getYear());
        a2.setYear("FR-1");
        assertEquals("FR-1", a2.getYear());
        a2.setDivision(Division.DIV_I);
        assertEquals(Division.DIV_I, a2.getDivision());
        a2.setDivision(Division.DIV_III);
        assertEquals(Division.DIV_III, a2.getDivision());
    }

    @Test
    public void testConstructor(){
        assertEquals(a1.getName(), "");
        assertEquals(a1.getSchool(), "");

        a1.setSchool(sch1);
        assertTrue(a1.atSchool(sch1));
        assertFalse(a1.atSchool(sch2));

        assertTrue(a1.getResults().isEmpty());
        assertFalse(a1.hasYear());
        assertTrue(a1.getYear().isEmpty());

        assertNull(a1.getGender());
        assertNull(a2.getGender());

        a1.setGender(Gender.M);
        a2.setGender(Gender.F);
        assertEquals(Gender.M, a1.getGender());
        assertEquals(Gender.F, a2.getGender());

        assertNull(a1.getDivision());
        assertFalse(a2.hasDivision());
    }
}
