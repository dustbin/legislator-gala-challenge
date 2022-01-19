import java.util.*;

public class Gala {
    public static void main(){

    }
    public void plan(int num_tables, List<String> guests, List<Preference> preferencesList){
        Map<String,List<String>> avoid =new HashMap<>();
        Map<String,List<String>> prefer =new HashMap<>();
        List<List<String>> tables = new ArrayList<>();
        for(Preference preference: preferencesList){
            if(preference.isAvoid()){
                if(!avoid.containsKey(preference.getFirstGuest())){
                    avoid.put(preference.getFirstGuest(),new ArrayList<>());
                }
                avoid.get(preference.getFirstGuest()).add(preference.getSecondGuest());
                if(!avoid.containsKey(preference.getSecondGuest())){
                    avoid.put(preference.getSecondGuest(),new ArrayList<>());
                }
                avoid.get(preference.getSecondGuest()).add(preference.getFirstGuest());
            }else{
                if(!prefer.containsKey(preference.getFirstGuest())){
                    prefer.put(preference.getFirstGuest(),new ArrayList<>());
                }
                prefer.get(preference.getFirstGuest()).add(preference.getSecondGuest());
                if(!prefer.containsKey(preference.getSecondGuest())){
                    prefer.put(preference.getSecondGuest(),new ArrayList<>());
                }
                prefer.get(preference.getSecondGuest()).add(preference.getFirstGuest());
            }
        }
        for(int i=0;i<num_tables;++i){
            tables.add(new ArrayList<>());
        }
        for(String guest: guests){
            int i;
            int[] avoidScore = new int[num_tables];
            int[] preferScore = new int[num_tables];
            List<String> guestAvoids = avoid.get(guest);
            List<String> guestPrefers = prefer.get(guest);
            List<Integer> lowestAvoidTables = new ArrayList<>();
            int lowestAvoidScore = guestAvoids.size();
            int largestTable = 0;
            for(i=0;i<num_tables;++i) {
                List<String> table = tables.get(i);
                for(String seated: table){
                    if(guestAvoids.contains(seated)){avoidScore[i]++;}
                    if(guestPrefers.contains(seated)){preferScore[i]++;}
                }
                if(avoidScore[i]<lowestAvoidScore){
                    lowestAvoidTables =new ArrayList<>(i);
                }else if(avoidScore[i]==lowestAvoidScore){
                    lowestAvoidTables.add(i);
                }
                if(table.size()>largestTable){
                    largestTable=table.size();
                }
            }
            if(lowestAvoidTables.size()==1){
                tables.get(lowestAvoidTables.get(0)).add(guest);
                continue;
            }
            List<Integer> highestPreferTables = new ArrayList<>();
            int highestPreferScore = 0;
            for(int table: lowestAvoidTables){
                if(preferScore[table]>highestPreferScore){
                    highestPreferTables = new ArrayList<>(table);
                }else if(preferScore[table]==highestPreferScore){
                    highestPreferTables.add(table);
                }
            }
            if(highestPreferTables.size()==1){
                tables.get(highestPreferTables.get(0)).add(guest);
                continue;
            }
            int smallestTable = largestTable;
            int smallestTableIndex = 0;
            for(int table: highestPreferTables){
                if(tables.get(table).size()<smallestTable){
                    smallestTableIndex = table;
                    smallestTable = tables.get(table).size();
                }
            }
            tables.get(smallestTableIndex).add(guest);
        }
    }
}
