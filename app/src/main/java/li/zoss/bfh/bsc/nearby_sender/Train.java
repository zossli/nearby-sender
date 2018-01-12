package li.zoss.bfh.bsc.nearby_sender;


import java.util.ArrayList;
import java.util.Arrays;

public class Train {
    private String mTrain;
    private Station[] mTrainRun;
    final String TAG = "Train";

    private int next = 0;
    private String mSpecialCoachInfo = "";
    private boolean trainOnCourse = false;

    public Train(String train, Station[] trainRun) {
        mTrain = train;
        mTrainRun = trainRun;
    }

    public Train(String train, Station[] trainRun, String specialCoachInfo) {
        mSpecialCoachInfo = specialCoachInfo;
        mTrain = train;
        mTrainRun = trainRun;
    }

    public String getTrain() {
        return mTrain;
    }

    public ArrayList<Station> getTrainRun() {
        ArrayList<Station> returnRun = new ArrayList();

        if (mTrainRun.length <= next)
            returnRun.add(mTrainRun[mTrainRun.length - 1]);

        for (int i = next;mTrainRun.length > i; i++) {
            returnRun.add(mTrainRun[i]);
        }

        return returnRun;
    }

    public String getDirection() {
        return mTrainRun[mTrainRun.length - 1].getStationName();
    }

    public Station getNext() {
        if (mTrainRun.length > next) {
            return mTrainRun[next];
        }
        return mTrainRun[mTrainRun.length - 1];
    }

    public void setNextValue() {
        if (trainOnCourse)
            next++;
        else
            trainOnCourse = true;
    }

    @Override
    public String toString() {
        return getTrain() + " Richtung " + getDirection();
    }


    public void resetTrain() {
        next = 0;
        trainOnCourse = false;
    }

    public String getSpecialCoachInfo() {
        return mSpecialCoachInfo;
    }
}
