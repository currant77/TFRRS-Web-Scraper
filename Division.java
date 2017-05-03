package Model;

/**
 * Created by Taylor Curran on 26/3/17.
 */
public enum Division {
    DIV_I, DIV_II, DIV_III;

    // Requires: division is not null
    // Effects: returns appropriate division as string
    public static String toString(Division d){
        if (d == DIV_I) return "Division I";
        else if (d == DIV_II) return "Division II";
        else return "Division III";
    }
}
