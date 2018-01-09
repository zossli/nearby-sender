package li.zoss.bfh.bsc.nearby_sender;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class Station {
    private String mName, mInfo;
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
