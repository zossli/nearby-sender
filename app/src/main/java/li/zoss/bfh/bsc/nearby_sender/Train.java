package li.zoss.bfh.bsc.nearby_sender;


import org.json.JSONObject;

public class Train {
    private String mTrain;
    private Station[] mTrainRun;
    private int next = 0;
    public Train(String train, Station[] trainRun){
        mTrain = train;
        mTrainRun = trainRun;
    }

    public String getTrain() {
        return mTrain;
    }

    public Station[] getTrainRun() {
        return mTrainRun;
    }
    public String getDirection()
    {
        return mTrainRun[mTrainRun.length-1].getStationName();
    }
    public Station getNext()
    {
        if(mTrainRun.length>next)
        {
            next++;
            return mTrainRun[next-1];
        }
        return  mTrainRun[mTrainRun.length-1];
    }

    public Station getCurrentNext() {
        if(mTrainRun.length>next)
        {
            return mTrainRun[next];
        }
        return  mTrainRun[mTrainRun.length-1];    }
}
