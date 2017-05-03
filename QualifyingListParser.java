package Parsers;

import Model.Database;
import Model.Division;
import Model.Gender;
import Parsers.Exceptions.ClassException;
import Parsers.Exceptions.IDException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Taylor Curran on 21/3/17.
 * Takes a given event page URL and corresponding gender and division.
 * extract all the athlete url's, and calls AthletePageParser
 * on them (passing the appropriate gender)
 */

public class QualifyingListParser {

    private String url;
    private Division division;
    public static final String ATHLETE_STRING = "athletes";
    private AthletePageParser app;
    private PrintWriter pw;
    private Database db;

// Constructor ===========================================================

    public QualifyingListParser(String url, Division division, PrintWriter pw){
        this.url = url;
        this.division = division;
        this.pw = pw;
        this.db = Database.getInstance();
    }

// Parser ===========================================================

    // Modifies: database
    // Effects: adds information about all athletes that compete in
    // given race to the database
    public void parse() throws IOException, ClassException, IDException {

        Document doc = Jsoup.connect(url).maxBodySize(0).timeout(0).get();
        Elements tables = doc.getElementsByClass("tablelist");
        for (Element e : tables) {

            if (!e.getElementsContainingOwnText("800m (Men)").isEmpty()) {
                parseAthletes(e.getElementsByAttributeValueContaining("href", ATHLETE_STRING), Gender.M);
            } else if (!e.getElementsContainingOwnText("800m (Women)").isEmpty()) {
                parseAthletes(e.getElementsByAttributeValueContaining("href", ATHLETE_STRING), Gender.F);
            }
        }
    }

    public void parseAthletes(Elements athletes, Gender g) throws IOException, ClassException, IDException {
        for (Element e : athletes){
            String url = e.attr("href");
            url = adjustUrl(url);
            try {
                int athleteID = getAthleteID(url);
                if (!db.inDatabase(athleteID)){
                    app = new AthletePageParser(athleteID, url, g, division, pw);
                    app.parse();
                }
            } catch (Exception ex){
                System.out.println("Error parsing athlete at " + url + " / " + Gender.toString(g) + " / " + Division.toString(division) + " from " + this.url);
                ex.printStackTrace();
                return;
            }
        }
    }

    public static String adjustUrl(String url){
        String r = url.replace("//","https://");
        r = r.replaceAll("\\s+", "");
        return r;
    }

    /**
     * Gets runner's 7-digit ID from website URL
     * @param url
     * @return runner's seven digit URL as an Integer
     * @throws IDException
     */
    private int getAthleteID(String url) throws IDException {

        try{
            // Sample URL format: https://www.tfrrs.org/athletes/5047860/St_Martins/Josh_Hunt.html
            int startIndex = 0;
            int endIndex = 0;
            int len = url.length();

            for (int i = 0; i < len; i++){
                Character c = url.charAt(i);
                if (Character.isDigit(c)){
                    startIndex = i;
                    break;
                }
            }

            for (int i = startIndex; i < len; i++){
                Character c = url.charAt(i);
                if (!Character.isDigit(c)){
                    endIndex = i;
                    break;
                }
            }

            String IDString = url.substring(startIndex, endIndex);
            Integer id = Integer.parseInt(IDString);
            return id;

        } catch (Exception e){
            throw new IDException();
        }
    }
}


