package li.zoss.bfh.bsc.nearby_sender;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * Created by Reto on 31.12.2017.
 */

public class NotificationPayload {
    public static JSONObject getJSON(NotType type, String station, Boolean requestNeeded) {
        JSONObject jsonObject = new JSONObject();
        try {
            switch (type) {
                case NEXT_STOP:
                    jsonObject.put("Type", NotType.NEXT_STOP);
                    jsonObject.put("requestNeeded", requestNeeded);
                    jsonObject.put("station", station);
                    break;
                case DELAY:
                    break;
                case INFO:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
