package Model;

/**
 * Created by Taylor Curran on 21/3/17.
 */
public enum Gender {
    M,F;

    // Requires: gender is not null
    // Effects: returns appropriate gender as string
    public static String toString(Gender g){
        if (g == Gender.M) return "M";
        else return "F";
    }
}
