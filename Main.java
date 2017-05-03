import Model.Database;
import Parsers.Exceptions.ClassException;
import Parsers.Exceptions.IDException;
import Parsers.ListPageParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Taylor Curran on 9/3/17.
 */
public class Main {

    private static Database db;
    private static PrintWriter pw;
    private static ListPageParser lpp;

    public static void main(String[] args) throws IOException, ClassException, IDException {

        setup();

        pw.println("ID,Name,Freshman Year,Best Freshman Time, Best Senior Time,Gender,Division");

        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2010", "2010");
        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2011", "2011");
        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2012", "2012");
        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2013", "2013");
        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2014", "2014");
        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2015", "2015");
        parseYear("https://www.tfrrs.org/archives.html?outdoor=1&year=2016", "2016");

        System.out.println("Number of Athletes: " + db.getNumAthletes());

        pw.close();
    }

    private static void parseYear(String url, String year) throws IOException, IDException, ClassException {
        lpp = new ListPageParser(url, pw);
        lpp.parseUrls();
        lpp.parseQualifyingLists();
        System.out.println("Finished " + year + " list");
    }

    private static void setup() throws FileNotFoundException {
        pw = new PrintWriter("output.txt");
        db = Database.getInstance();
        db.clear();
    }
}
