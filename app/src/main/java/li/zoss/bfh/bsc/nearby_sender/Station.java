package li.zoss.bfh.bsc.nearby_sender;

class Station {
    private String mName, mInfo;
    private Boolean mRequestStop;


    public Station(String name, Boolean requestStop, String info)
    {
        mName = name;
        mInfo = info;
        mRequestStop = requestStop;

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

}
