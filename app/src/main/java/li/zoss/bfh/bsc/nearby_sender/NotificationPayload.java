package li.zoss.bfh.bsc.nearby_sender;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationPayload {
    private static String TAG = "NotificationPayload";

    public static JSONObject getNextStopJSON(String station, Boolean requestNeeded, Station nextStop) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Type", NotType.NEXT_STOP);
            jsonObject.put("requestNeeded", requestNeeded);
            jsonObject.put("station", station);

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject getDelayJSON(String delay) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Type", NotType.DELAY);
                jsonObject.put("delay", delay);

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject getTrainInfo(String trainInfo, String direction, Station nextStop) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Type", NotType.TRAIN_INFO);
            jsonObject.put("trainInfo", trainInfo);
            jsonObject.put("trainDirection", direction);
            jsonObject.put("trainNextStop", nextStop.getStationName());
            jsonObject.put("trainNextStopRequestNeeded", nextStop.getStationRequestStop());
            Log.i(TAG, "getTrainInfo: traininfo: json is "+jsonObject.toString());
        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
