package li.zoss.bfh.bsc.nearby_sender;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Reto on 31.12.2017.
 */

public class NotificationPayload {
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
    public static JSONObject getTrainInfo(String trainInfo, String direction) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Type", NotType.TRAIN_INFO);
            jsonObject.put("trainInfo", trainInfo);
            jsonObject.put("direction", direction);

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
