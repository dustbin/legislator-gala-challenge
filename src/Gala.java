import java.util.*;

public class Gala {
    public static void main(String[] args){
        List<String> guests = new ArrayList<>();
        for(int i=0;i<10;++i) {
            guests.add("guest_" + i);
        }
        List<Preference> preferences = new ArrayList<>();
        preferences.add(new Preference("avoid", guests.get(0), guests.get(1)));
        preferences.add(new Preference("avoid", guests.get(1), guests.get(2)));
        preferences.add(new Preference("avoid", guests.get(2), guests.get(3)));
        preferences.add(new Preference("avoid", guests.get(3), guests.get(4)));
        preferences.add(new Preference("avoid", guests.get(4), guests.get(5)));

        preferences.add(new Preference("prefer", guests.get(0), guests.get(4)));
        preferences.add(new Preference("prefer", guests.get(1), guests.get(5)));
        preferences.add(new Preference("prefer", guests.get(2), guests.get(6)));
        preferences.add(new Preference("prefer", guests.get(3), guests.get(7)));
        preferences.add(new Preference("prefer", guests.get(4), guests.get(3)));
        preferences.add(new Preference("prefer", guests.get(5), guests.get(2)));

        Gala gala = new Gala(5, guests,preferences);
        gala.plan();
        System.out.println(gala.toJSON());
    }
    protected int numTables;
    protected List<String> guestList;
    protected List<Preference> preferenceList;
    protected Map<String, List<String>> avoidIndex;
    protected Map<String, List<String>> preferIndex;
    protected List<List<String>> tables;
    public Gala(int numTables, List<String> guests, List<Preference> preferences){
        this.numTables=numTables;
        this.guestList=guests;
        this.preferenceList=preferences;
    }
    public String toJSON(){
        List<String> jsonTables = new ArrayList<>();
        for(int i=0;i<tables.size();++i) {
            jsonTables.add("{table:"+i+",guests:["+String.join(",",tables.get(i))+"]}");
        }
        return "{"+String.join(",", jsonTables)+"}";
    }
    public void plan(){
        generateIndexes();
        generateTables();
        for(String guest: guestList){
            seatGuest(guest);
        }
    }
    protected void generateIndexes(){
        avoidIndex = new HashMap<>();
        preferIndex = new HashMap<>();
        for(Preference preference: preferenceList){
            if(preference.isAvoid()){
                preference.addToIndex(avoidIndex);
            }else{
                preference.addToIndex(preferIndex);
            }
        }
    }
    protected void generateTables(){
        tables = new ArrayList<>();
        for(int i=0;i<numTables;++i){
            tables.add(new ArrayList<>());
        }
    }
    protected void seatGuest(String guest){
        List<Integer> lowestAvoidTables = lowestTablesFor(avoidIndex.get(guest), allTables());
        if(lowestAvoidTables.size()==1){
            tables.get(lowestAvoidTables.get(0)).add(guest);
            return;
        }
        List<Integer> highestPreferTables = highestTablesFor(preferIndex.get(guest), lowestAvoidTables);
        if(highestPreferTables.size()==1){
            tables.get(highestPreferTables.get(0)).add(guest);
            return;
        }
        tables.get(smallestTable(highestPreferTables)).add(guest);
    }
    protected List<Integer> lowestTablesFor(List<String> others, List<Integer> startingTables){
        if(others == null){
            return startingTables;
        }
        int lowestScore = others.size();
        List<Integer> lowestAvoidTables = new ArrayList<>();
        for(int table: startingTables) {
            int score = 0;
            for(String seated: tables.get(table)){
                if(others.contains(seated)){++score;}
            }
            if(score<lowestScore){
                lowestAvoidTables = new ArrayList<>();
                lowestScore = score;
            }
            if(score==lowestScore){
                lowestAvoidTables.add(table);
            }
        }
        return lowestAvoidTables;
    }
    protected List<Integer> highestTablesFor(List<String> others, List<Integer> startingTables){
        if(others == null){
            return startingTables;
        }
        int highestScore = 0;
        List<Integer> highestTables = new ArrayList<>();
        for(int table: startingTables){
            int score = 0;
            for(String seated: tables.get(table)){
                if(others.contains(seated)){++score;}
            }
            if(score>highestScore){
                highestTables = new ArrayList<>();
                highestScore = score;
            }
            if(score==highestScore){
                highestTables.add(table);
            }
        }
        return highestTables;
    }
    protected List<Integer> allTables(){
        List<Integer> tables = new ArrayList<>();
        for(int i=0;i<numTables;++i){
            tables.add(i);
        }
        return tables;
    }
    protected int smallestTable(List<Integer> tableList){
        int index = tableList.get(0);
        int smallest = Integer.MAX_VALUE;
        for(int table: tableList){
            int size = tables.get(table).size();
            if(size<smallest){
                index = table;
                smallest = size;
            }
        }
        return index;
    }
}
