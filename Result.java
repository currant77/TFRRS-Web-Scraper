package Model;

import Model.Exceptions.DateException;
import Model.Exceptions.TimeException;

/**
 * Created by Taylor Curran on 10/3/17.
 */
public class Result {

    private Athlete athlete;
    private int year;
    private String competition;                       // Indoor or outdoor
    private String event;                                // 800m
    private double time;                                // In seconds
    private static final Double SEC_PER_MIN = 60.0;
    private static final int HASHCODE_FACTOR = 17;

// Constructor =========================================================================

    // Effects: creates new result based on given information
    public Result(Athlete a, String date, String competition,
                  String event, String time) throws TimeException, DateException {
        this.athlete = a;
        this.year = getYear(date);
        this.competition = competition;
        this.event = event;
        this.time = getTime(time);
    }

// Helpers  =========================================================================

    // Effects: given a valid date string, returns the year
    public static int getYear(String date) throws DateException {
        try {
            int size = date.length();
            String s = date.substring(size - 2);
            Integer yr = Integer.parseInt(s);

            if (yr == 17) throw new DateException();

            return yr;

        } catch (Exception e) {
            throw new DateException();
        }
    }

    // Effects: given a valid time string, return a double
    public static Double getTime(String time) throws TimeException {
        try {
            int colon = time.indexOf(':');
            String m = time.substring(0,colon);
            Double min = Double.parseDouble(m);
            String s = time.substring(colon+1);
            Double sec = Double.parseDouble(s);
            return min*SEC_PER_MIN + sec;

        } catch (Exception e) {
            throw new TimeException();
        }
    }

// Getters =========================================================================

    public Athlete getAthlete(){return athlete;}
    public int getYear(){return year;}
    public String getCompetition(){return competition;}
    public String getEvent(){return event;}
    public Double getTime(){return time;}

// Setters =========================================================================

    // Effects: sets Athlete to a, checks that a contains this
    public void setAthlete(Athlete a){
        if (!this.athlete.equals(a)){
            this.athlete = a;
            a.addResult(this);
        }
    }

// Equals  =========================================================================

    /**
     * Two Results considered equal if they have the same athlete, name, time,and date
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (o == null || o.getClass() != this.getClass()) return false;
        Result r = (Result) o;
        return r.athlete.equals(this.athlete)
                && r.competition.equals(this.competition)
                && r.year == this.year
                && r.time == this.time;
    }

    @Override
    public int hashCode(){
        int code = athlete.hashCode();
        code = code*HASHCODE_FACTOR + competition.hashCode();
        code = code*HASHCODE_FACTOR + ((Double) time).hashCode();
        code = code*HASHCODE_FACTOR + year;
        return code;
    }
}


