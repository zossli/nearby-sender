package li.zoss.bfh.bsc.nearby_sender;

import org.json.JSONException;
import org.json.JSONObject;


public  class NextConnections {


    public static String[] forMuensingen() {

        String[] trains = {"S1", "RE"};
        String[] dep = {"5min", "10min",};
        String[] dest = {"Bern", "Thun"};

        return returner(trains, dep, dest);
    }

    public static String[] forThun() {

        String[] trains = {"NFB 6", "NFB 5", "NFB 1", "IC 61", "IC 8"};
        String[] dep = {"5min", "6min", "7min", "9min", "12min"};
        String[] dest = {"Westquartier", "Schorenfriedhof", "Steffisburg, Flühli", "Basel SBB", "Romanshorn"};

        return returner(trains, dep, dest);
    }

    public static String[] forSpiez() {

        String[] trains = {"NFB 1", "IC 61", "IC 8", "BUS 62"};
        String[] dep = {"6min", "10min", "12min", "15min"};
        String[] dest = {"Steffisburg, Flühli", "Interlaken Ost", "Brig", "Aeschiried, Schulhaus"};

        return returner(trains, dep, dest);
    }

    private static String[] returner(String[] trains, String[] dep, String[] dest) {
        String[] returner = new String[trains.length];
        for (int i = 0; trains.length > i; i++) {
            returner[i] = dep[i] + " " + trains[i] + " " + dest[i];
        }
        return returner;

    }
}
