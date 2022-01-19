package gala;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void addToIndex(Map<String,List<String>> index){
        if(!index.containsKey(firstGuest)){
            index.put(firstGuest,new ArrayList<>());
        }
        index.get(firstGuest).add(secondGuest);

        if(!index.containsKey(secondGuest)){
            index.put(secondGuest,new ArrayList<>());
        }
        index.get(secondGuest).add(firstGuest);
    }
}
