import java.util.List;

public class Preference {
    protected String preference;
    protected String firstGuest;
    protected String secondGuest;

    public Preference(String preference, String firstGuest, String secondGuest){
        this.preference = preference;
        this.firstGuest = firstGuest;
        this.secondGuest = secondGuest;
    }

    public boolean isAvoid(){
        return preference.equals("avoid");
    }

    public String getFirstGuest() {
        return firstGuest;
    }

    public String getSecondGuest() {
        return secondGuest;
    }
}
