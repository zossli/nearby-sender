package li.zoss.bfh.bsc.nearby_sender;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationPayload {
    private static String TAG = "NotificationPayload";

    public static JSONObject getNextStopJSON(Station nextStop) {
        JSONObject jsonObject = new JSONObject();
        jsonObject=addToJSON(jsonObject, "Type", NotType.PUBLISH_NEXT_STOP);
        jsonObject=addToJSON(jsonObject, "trainNextStopRequestNeeded", nextStop.getStationRequestStop());
        jsonObject=addToJSON(jsonObject, "trainNextStop", nextStop.getStationName());
        jsonObject=addToJSON(jsonObject, "trainNextStationInfo", nextStop.getStationInfo());
        return jsonObject;
    }

    public static JSONObject getDelayJSON(String delay) {
        JSONObject jsonObject = new JSONObject();
        jsonObject=addToJSON(jsonObject, "Type", NotType.PUBLISH_DELAY);
        jsonObject=addToJSON(jsonObject, "trainDelay", delay+" min");
        return jsonObject;
    }

    public static JSONObject soundWillPlay(Boolean willPlay) {
        JSONObject jsonObject = new JSONObject();
        jsonObject=addToJSON(jsonObject, "Type", NotType.RESPONSE_WITH_SOUND);
        jsonObject=addToJSON(jsonObject, "willPlaySound", willPlay);
        return jsonObject;
    }

    public static JSONObject getSpecialCoachInfo(Train train) {
        JSONObject jsonObject = new JSONObject();
        jsonObject=addToJSON(jsonObject, "Type", NotType.PUBLISH_COACH_INFO);
        jsonObject=addToJSON(jsonObject, "trainSpecialCoachInfo", train.getSpecialCoachInfo());
        return jsonObject;
    }

    public static JSONObject getTrainInfo(Train train, String delay) {
        JSONObject jsonObject = new JSONObject();
        jsonObject=addToJSON(jsonObject, "Type", NotType.REQUEST_TRAIN_INFO);
        jsonObject=addToJSON(jsonObject, "trainInfo", train.getTrain());
        jsonObject=addToJSON(jsonObject, "trainSpecialCoachInfo", train.getSpecialCoachInfo());
        jsonObject=addToJSON(jsonObject, "trainDirection", train.getDirection());
        jsonObject=addToJSON(jsonObject, "trainNextStop", train.getNext().getStationName());
        jsonObject=addToJSON(jsonObject, "trainNextStationInfo", train.getNext().getStationInfo());
        jsonObject=addToJSON(jsonObject, "trainNextStopRequestNeeded", train.getNext().getStationRequestStop());
        jsonObject=addToJSON(jsonObject, "trainCurrentDelay", delay);
        return jsonObject;
    }

    private static JSONObject addToJSON(JSONObject json, String key, NotType value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static JSONObject addToJSON(JSONObject json, String key, String value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static JSONObject addToJSON(JSONObject json, String key, Boolean value) {
        try {
            json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
