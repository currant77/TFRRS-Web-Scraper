package Parsers;

import Model.*;
import Parsers.Exceptions.ClassException;
import Parsers.Exceptions.IDException;
import Parsers.Exceptions.NameException;
import Parsers.Exceptions.SchoolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taylor Curran on 21/3/17.
 * Class to parseUrls an athlete's bio page, extract all
 * required information, and add it to database
 */
public class AthletePageParser {

    private String url;
    private Athlete athlete;
    private Gender gender;
    private Division division;
    private Database db;
    private PrintWriter pw;

// Constructor ===========================================================

    // Effects: creates new AthletePageParser with their web address
    public AthletePageParser(String url, Gender gender, Division division, PrintWriter pw) throws IDException, IOException, ClassException {
        this.url = url;
        this.gender = gender;
        this.division = division;
        this.pw = pw;
        db = Database.getInstance();

        int id = getID(url);

        // Assume that if athlete is already in database,
        // all of their information has already been collected
        athlete =  db.getAthlete(id);

        if(!db.inDatabase(id)) {
            athlete.setGender(gender);
            athlete.setDivision(division);
        }
    }

    public AthletePageParser(int id, String url, Gender gender, Division division, PrintWriter pw) throws IDException, IOException, ClassException {
        this.url = url;
        this.gender = gender;
        this.division = division;
        this.pw = pw;
        db = Database.getInstance();
        athlete =  db.getAthlete(id);
        athlete.setGender(gender);
        athlete.setDivision(division);
    }

    /**
     * Gets runner's 7-digit ID from website URL
     * @param url
     * @return runner's seven digit URL as an Integer
     * @throws IDException
     */
    private int getID(String url) throws IDException {

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

    public Athlete getAthlete(){return this.athlete;}

// Parser ===========================================================

    // Modifies: database
    // Effects: adds athlete and all results to database
    public void parse() throws IOException, ClassException, NameException, SchoolException {

        // Get document from web
        Document doc = Jsoup.connect(url).maxBodySize(0).timeout(0).get();

        // Extract athlete's name and school
        Element ath = doc.getElementById("athlete");
        String name = ath.getElementsByTag("h2").first().text();
        String school = ath.getElementsByTag("a").first().text();

        athlete.setName(name);
        athlete.setSchool(school);
        parseResults(doc);
        parseClasses(doc);

        if (athlete.isValidAthlete() && athlete.hasSRandFRResults())
            printResults(athlete);

        // If need to get school URL
        // String url = school.attr("abs:href");
        // System.out.println(url);
    }


// Get Athlete's Class ===========================================================

    // Modifies: athlete
    // Effects: parses athlete's class (i.e. their year) from Top Performances
    private void parseClasses(Document doc) throws ClassException {
        Elements cla = doc.getElementsByClass("class");
        List<String> classes = new ArrayList<>();
        for (Element e : cla){
            if (validClass(e.text())){
                classes.add(e.text());
            }
        }

        if (classes.isEmpty())
            parseClassesBackup();

        athlete.setYear(getYear(classes));
    }

    // Requires: parseClasses() has failed (thrown exception)
    // Modifies: athlete
    // Effects: assumes athlete's class based on difference in years
    // between oldest and most recent performances
    private void parseClassesBackup() throws ClassException {
        int minYear = athlete.minYear();
        int maxYear = athlete.maxYear();
        int diff = maxYear - minYear;

        if (diff == 0) athlete.setYear("FR-1");
        if (diff == 1) athlete.setYear("SO-2");
        if (diff == 2) athlete.setYear("JR-3");
        if (diff == 3 || diff == 4) athlete.setYear("SR-4");
        else throw new ClassException();
    }


    // Effects: returns true if string is one of valid classes
    private boolean validClass(String s){
        return s.equals("FR-1") || s.equals("SO-2")|| s.equals("JR-3") || s.equals("SR-4");
    }

    // Effects: returns oldest class from list
    private String getYear(List<String> classes){
        String def = classes.get(0);
        for (String s : classes){
            if (largerClass(s,def)){
                def = s;
            }
        }
        return def;
    }

    // Effects: returns true if first class (c1) is larger than second class (c2)
    private boolean largerClass(String c1, String c2){
        if (c1.equals("FR-1"))
            return false;
        else if (c1.equals("SO-2"))
            return c2.equals("FR-1");
        else if (c1.equals("JR-3"))
            return c2.equals("FR-1") || c2.equals("SO-2");
        else
            return !c2.equals("SR-4");
    }

// Get Athlete's Results ===========================================================

    // Modifies: athlete
    // Effects: given athlete and document, get results and add them to athlete
    private void parseResults(Document doc){

        Elements rows = doc.getElementsByTag("tr");

        // Take only rows corresponding to to events (only those with "date" columns
        // and only take those in EVENT and Final results
        int i = 0;
        while (i < rows.size()){
            try {
                Element e = rows.get(i);
                if (e.getElementsByClass("date").size() == 1
                    && e.getElementsByClass("event").text().equals(Database.EVENT)
                    && e.getElementsByClass("io").first().text().equals("Outdoor"))
                parseResult(e);
                i++;
            } catch (Exception e) {
                i++;
                continue;
            }
        }
    }

    // Modifies: athlete
    // Effects: gets a single result and adds it to athlete
    private void parseResult(Element e) throws Exception {
        String date = e.getElementsByClass("date").text();
        String competition = e.getElementsByClass("meet").first().text();
        String time = e.getElementsByClass("mark").first().text();
        Result r = new Result(athlete, date, competition, Database.EVENT, time);
        athlete.addResult(r);
    }

    public void printResults(Athlete a){
        String output = "";
        output += Integer.toString(a.getID()) + ",";
        output += a.getName() + ",";
        output += a.minYear() + ",";
        output += roundToTwoDecimals(a.getBestResultInYear(a.minYear())) + ",";
        output += roundToTwoDecimals(a.getBestResultInYear(a.maxYear())) + ",";
        output += Gender.toString(a.getGender()) + ",";
        output += a.getSchool() + ",";
        output += Division.toString(a.getDivision());
        pw.println(output);
    }

    // Effects: rounds given double to two decimals
    private static double roundToTwoDecimals(double d){
        return (double) Math.round(d*1000) / 1000;
    }

}

