package gala;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GalaTest {

    @Test
    void plan() {
        List<String> guests = genGuests(6);
        Gala gala = new Gala(3,guests,prefStandard(guests));
        gala.plan();
        assert gala.tables.get(0).contains(guests.get(0));
        assert gala.tables.get(0).contains(guests.get(1));
        assert gala.tables.get(1).contains(guests.get(2));
        assert gala.tables.get(2).contains(guests.get(3));
        assert gala.tables.get(1).contains(guests.get(4));
        assert gala.tables.get(2).contains(guests.get(5));
    }

    @Test
    void toJSON() {
        List<String> guests = genGuests(6);
        Gala gala = new Gala(3,guests,prefStandard(guests));
        gala.plan();
        String result = gala.toJSON();
        System.out.println(result);
        assertEquals(result,"{\"table_0\":[\"guest_0\",\"guest_1\"],\"table_1\":[\"guest_2\",\"guest_4\"],\"table_2\":[\"guest_3\",\"guest_5\"]}");
    }

    @Test
    void generateIndexes() {
        List<String> guests = genGuests(5);
        Gala gala = new Gala(0,guests,prefAll("avoid",guests));
        gala.generateIndexes();
        for(int i=0;i<5;++i) {
            for(int j=0;j<5;++j) {
                if(i==j){continue;}
                assert gala.avoidIndex.get(guests.get(i)).contains(guests.get(j));
            }
            assertNull(gala.preferIndex.get(guests.get(i)));
        }
        gala = new Gala(0,guests,prefAll("prefer",guests));
        gala.generateIndexes();
        for(int i=0;i<5;++i) {
            for(int j=0;j<5;++j) {
                if(i==j){continue;}
                assert gala.preferIndex.get(guests.get(i)).contains(guests.get(j));
            }
            assertNull(gala.avoidIndex.get(guests.get(i)));
        }
    }

    @Test
    void generateTables() {
        int numTables = 8;
        Gala gala = new Gala(8,new ArrayList<>(),new ArrayList<>());
        gala.generateTables();
        assertEquals(numTables,gala.tables.size());
    }

    @Test
    void seatGuest() {
        List<String> guests = genGuests(5);
        List<Preference> preferences = new ArrayList<>();
        preferences.add(new Preference("avoid",guests.get(0),guests.get(3)));
        preferences.add(new Preference("prefer",guests.get(1),guests.get(3)));
        preferences.add(new Preference("avoid",guests.get(2),guests.get(3)));
        preferences.add(new Preference("prefer",guests.get(1),guests.get(4)));
        preferences.add(new Preference("prefer",guests.get(3),guests.get(4)));

        Gala gala = new Gala(3,guests,preferences);
        gala.generateIndexes();
        gala.generateTables();
        gala.tables.get(0).add(guests.get(0));
        gala.tables.get(0).add(guests.get(1));
        gala.tables.get(1).add(guests.get(2));

        gala.seatGuest(guests.get(3));
        assert gala.tables.get(2).contains(guests.get(3));

        gala.seatGuest(guests.get(4));
        assert gala.tables.get(2).contains(guests.get(4));
    }

    @Test
    void lowestTablesFor() {
        List<String> guests = genGuests(4);
        Gala gala = new Gala(3,guests,new ArrayList<>());
        gala.generateTables();
        gala.tables.get(0).add(guests.get(0));
        gala.tables.get(0).add(guests.get(1));
        gala.tables.get(1).add(guests.get(2));

        List<Integer> result = gala.lowestTablesFor(guests,gala.allTables());
        assertEquals(1,result.size());
        assertEquals(2,result.get(0));

        gala.tables.get(2).add(guests.get(3));
        result = gala.lowestTablesFor(guests,gala.allTables());
        assertEquals(2,result.size());
        assertEquals(1,result.get(0));
        assertEquals(2,result.get(1));
    }

    @Test
    void highestTablesFor() {
        List<String> guests = genGuests(4);
        Gala gala = new Gala(3,guests,new ArrayList<>());
        gala.generateTables();
        gala.tables.get(0).add(guests.get(0));
        gala.tables.get(0).add(guests.get(1));
        gala.tables.get(2).add(guests.get(2));

        List<Integer> result = gala.highestTablesFor(guests,gala.allTables());
        assertEquals(1,result.size());
        assertEquals(0,result.get(0));

        gala.tables.get(2).add(guests.get(3));
        result = gala.highestTablesFor(guests,gala.allTables());
        assertEquals(2,result.size());
        assertEquals(0,result.get(0));
        assertEquals(2,result.get(1));
    }

    @Test
    void allTables() {
        int numTables = 8;
        Gala gala = new Gala(8,new ArrayList<>(),new ArrayList<>());
        List<Integer> tables = gala.allTables();
        assertEquals(numTables,tables.size());
    }

    @Test
    void smallestTable() {
        List<String> guests = genGuests(4);
        Gala gala = new Gala(3,guests,new ArrayList<>());
        gala.generateTables();
        gala.tables.get(0).add(guests.get(0));
        gala.tables.get(0).add(guests.get(1));
        gala.tables.get(2).add(guests.get(2));

        int result = gala.smallestTable(gala.allTables());
        assertEquals(1,result);

        gala.tables.get(1).add(guests.get(3));
        result = gala.smallestTable(gala.allTables());
        assertEquals(1,result);
    }

    private List<Preference> prefAll(String preference, List<String> guests){
        List<Preference> preferences = new ArrayList<>();
        for(int i=0;i<guests.size();++i) {
            for(int j=0;j<guests.size();++j) {
                if(i==j){continue;}
                preferences.add(new Preference(preference,guests.get(i),guests.get(j)));
            }
        }
        return preferences;
    }
    private List<Preference> prefStandard(List<String> guests){
        List<Preference> preferences = new ArrayList<>();
        preferences.add(new Preference("prefer",guests.get(0),guests.get(1)));
        preferences.add(new Preference("avoid",guests.get(0),guests.get(2)));
        preferences.add(new Preference("prefer",guests.get(1),guests.get(2)));
        preferences.add(new Preference("avoid",guests.get(0),guests.get(3)));
        preferences.add(new Preference("prefer",guests.get(1),guests.get(3)));
        preferences.add(new Preference("avoid",guests.get(2),guests.get(3)));
        preferences.add(new Preference("prefer",guests.get(0),guests.get(4)));
        preferences.add(new Preference("avoid",guests.get(1),guests.get(4)));
        preferences.add(new Preference("prefer",guests.get(2),guests.get(4)));
        preferences.add(new Preference("avoid",guests.get(3),guests.get(4)));
        return preferences;
    }
    private List<String> genGuests(int num){
        List<String> ret = new ArrayList<>();
        for(int i=0;i<num;++i){
            ret.add("guest_"+i);
        }
        return ret;
    }
}