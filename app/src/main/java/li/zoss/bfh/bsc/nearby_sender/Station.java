package li.zoss.bfh.bsc.nearby_sender;

import com.google.android.gms.nearby.messages.Strategy;

import java.util.ArrayList;
import java.util.List;

class Station {
    private String mName;
    private String mInfo;
    private String[] mNextDep = null;
    private int mStationSound = 0;
    private int[] mAdditionalSounds = null;
    private Boolean mRequestStop;

    public Station(String name, Boolean requestStop, String info) {
        mName = name;
        mInfo = info;
        mRequestStop = requestStop;
    }

    public Station(String name, Boolean requestStop, String info, int stationSound) {
        mName = name;
        mInfo = info;
        mRequestStop = requestStop;
        mStationSound = stationSound;
    }

    public Station(String name, Boolean requestStop, String info, int stationSound, int[] additionalSounds) {
        mName = name;
        mInfo = info;
        mRequestStop = requestStop;
        mStationSound = stationSound;
        mAdditionalSounds = additionalSounds;
    }

    public Station(String name, Boolean requestStop, String info, int stationSound, int[] additionalSounds, String[] nextDep) {
        mName = name;
        mInfo = info;
        mRequestStop = requestStop;
        mStationSound = stationSound;
        mAdditionalSounds = additionalSounds;
        mNextDep = nextDep;
    }

    public String getStationName() {
        return mName;
    }

    public String getStationInfo() {
        return mInfo;
    }

    public Boolean getStationRequestStop() {
        return mRequestStop;
    }

    public Object getStationSound() {
        return mStationSound;
    }

    public boolean hasStationSound() {
        return !(mStationSound == 0);
    }

    public boolean hasAdditionalSound() {
        return !(mAdditionalSounds == null);
    }

    public String getNextDepartures(){
        String ret = "";
        if(mNextDep==null)
            return "";
        for (int i=0;mNextDep.length>i;i++)
        {
            if(i>0)
                ret = ret+"\n";
            ret = ret+" "+mNextDep[i];
        }
        return ret;
    }

    public List getmAdditionalSounds() {
        if (mAdditionalSounds == null) {
            return new ArrayList(0);
        } else {
            int size = mAdditionalSounds.length;
            List<Integer> list = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                list.add(mAdditionalSounds[i]);
            }
            return list;
        }
    }


}
