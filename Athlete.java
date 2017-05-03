package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Taylor Curran on 10/3/17.
 */

public class Athlete implements Iterable<Result> {

    private int id;
    private String name;
    private String school;
    private String year;
    private List<Result> results;
    private Gender gender;                        // M or F
    private Division division;
    private int HASHCODE_FACTOR = 31;

// Constructor ===========================================================

    // Effcts: creates new Athlete with empty year and gender;
    public Athlete(int id){
        this.id = id;
        this.name = "";
        this.school = "";
        this.year = "";
        this.results = new ArrayList<>();
        this.gender = null;
        this.division = null;
    }

// Getters and Setters  ===========================================================

    public int getID(){return id;}
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    /**
     * Adds result to athlete, and sets result's athlete to this
     * @param r
     */
    public void addResult(Result r){
        if (!results.contains(r)) {
            results.add(r);
            r.setAthlete(this);
        }
    }

    /**
     *
     * @return unmodifiable list of athlete's results
     */
    public List<Result> getResults(){return Collections.unmodifiableList(results);}

    /**
     *
     * @return number of results
     */
    public int getNumResults(){return results.size();}
    /**
     *
     * @return the school string
     */
    public String getSchool(){return this.school;}
    /**
     *
     * @param s
     * @return true if student attends given school s
     */
    public boolean atSchool(String s){return school.equals(s);}
    /**
     * Remove athlete from any previous school, set athlete's school to s, and add athlete to s
     * @param s
     */
    public void setSchool(String s){
        this.school = s;
    }
    /**
     *
     * @return true if athlete does not have empty string as year
     */
    public boolean hasYear(){return !year.isEmpty();}
    /**
     * Athlete's year
     * @return
     */
    public String getYear(){return this.year;}
    /**
     * Sets athlete's year (freshman, junior, ...)
     * @param year
     */
    public void setYear(String year){this.year = year;}
    /**
     *
     * @return athlete's gender
     */
    public Gender getGender(){
        return gender;
    }
    /**
     * Sets athlete's gender; throws Gender Exception if invalid gender
     * @param g
     */
    public void setGender(Gender g) {
        this.gender = g;
    }

    public Division getDivision(){return this.division;}
    public boolean hasDivision(){return !(this.division == null);}
    public void setDivision(Division d){this.division = d;}

// Iterator  ===========================================================

    public Iterator<Result> iterator(){return results.iterator();}

// Equals and Hashcode  ===========================================================

    /**
     * Two athletes considered equal if they have the same athlete ID;
     * @param o
     * @return whether two athletes are equal
     */
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Athlete a = (Athlete) o;
        return this.id == a.id;
    }

    @Override
    public int hashCode(){
        int code = id;
        return code;
    }

// Helpers  ===========================================================

    // Assumption: freshman and senior times, respectively, are best times in max and min year

    // Requires: athlete has at least one result in given year
    public double getBestResultInYear(int year){
        double bestResult = Double.MAX_VALUE;
        for (Result r : results){
            if (r.getYear() == year && r.getTime() < bestResult)
                bestResult = r.getTime();
        }
        return bestResult;
    }

    // Effects: returns true if athlete has a result in the given year
    public boolean hasResultinYear(int year){
        boolean hasResult = false;
        for (Result r : results){
            if (r.getYear() == year)
                hasResult = true;
        }
        return hasResult;
    }

    // Effects: returns true if athlete has results either 4 or 5 years apart
    public boolean hasSRandFRResults(){
        int maxYear = maxYear();
        int minYear = minYear();
        if (minYear == maxYear - 3 || minYear == maxYear - 4)
            return true;
        return false;
    }

    // Effects: returns largest year for which athlete has results
    public int maxYear(){
        int maxYear = 0;
        for (Result r : results){
            if (r.getYear() > maxYear)
                maxYear = r.getYear();
        }
        return maxYear;
    }

    // Effects: returns smallest year for which athlete has results
    public int minYear(){
        int minYear = Integer.MAX_VALUE;
        for (Result r : results){
            if (r.getYear() < minYear)
                minYear = r.getYear();
        }
        return minYear;
    }

    // Effects: returns if athlete is valid for analysis (all fields filled in)
    public boolean isValidAthlete(){
        return !name.isEmpty()
                && !school.isEmpty()
                && !year.isEmpty()
                && !results.isEmpty()
                && !(gender == null)
                && !(division == null);

    }
}
