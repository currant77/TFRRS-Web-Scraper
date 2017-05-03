package Model;

import java.util.*;

/**
 * Created by Taylor Curran on 11/3/17.
 */
public class Database implements Iterable<Athlete> {

    private static Database instance = null;
    private Set<Athlete> athletes;
    public static final String EVENT = "800";

// Constructor (using Singleton design pattern) =============================

    private Database(){
        athletes = new HashSet<>();
    }
    public static Database getInstance(){
        if (instance == null){
            instance = new Database();
        }
        return instance;
    }

// Methods ==================================================================

    // Effects: returns true if athlete with given name and school
    // is in database
    public boolean inDatabase(int id){
        boolean res = false;
        Athlete a = new Athlete(id);
        for (Athlete ath : athletes){
            if (ath.equals(a)){
                res = true;
            }
        }
        return res;
    }

    // Effects: returns athlete with given ID number; If no such athlete exists,
    // creates new one and adds it to database
    public Athlete getAthlete(int id){
        Athlete a = new Athlete(id);
        for (Athlete ath : athletes){
            if (ath.equals(a)){
                return ath;
            }
        }
        athletes.add(a);
        return a;
    }

    // Effects: returns athletes Hashset as unmodifiable set
    public Set<Athlete> getAthletes(){return Collections.unmodifiableSet(athletes);}

    // Effects: return number of athletes in database
    public int getNumAthletes(){return athletes.size();}

    // Effects: clears all athletes from database
    public void clear(){this.athletes.clear();}

// Iterator  =================================================================

    // Effects: returns iterator over all athletes
    public Iterator<Athlete> iterator(){return athletes.iterator();}

}
