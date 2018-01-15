package li.zoss.bfh.bsc.nearby_sender;

import java.util.ArrayList;


class TrainRun {

    public void setAll(ArrayList<Train> list) {
        list.add(new Train("S1",s1BernThun,"Achtung: Kein Ausstieg aus diesem Wagen in Münsingen. Perron zu kurz. Bitte begeben Sie sich zum Aussteigen in den vorderen Wagen."));
        list.add(new Train("RE",redomo));
        list.add(new Train("RE",rezweisimmen));
        list.add(new Train("Testrain",shorttrain));

    }
    public Station[] s1BernThun = {
            new Station("Bern Wankdorf", false, "", R.raw.d_h2wd),
            new Station("Ostermundigen", false, "",R.raw.d_h2ost),
            new Station("Gümligen",false,"", R.raw.d_h2gue),
            new Station("Rubigen",false,"", R.raw.d_h2rub),
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.d_h2ms , new int[]{R.raw.d_bf256 ,R.raw.d_f01}, NextConnections.forMuensingen()),
            new Station("Wichtrach",false,"", R.raw.d_h2wch),
            new Station("Kiesen",false,"",R.raw.d_h2ki),
            new Station("Uttigen",false,"", R.raw.d_h2uti),
            new Station("Thun",false, "Endbahnhof, bitte alle aussteigen.", R.raw.d_h2th, new int[]{R.raw.d_e9104}, NextConnections.forThun())
    };
    public Station[] rezweisimmen = {
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.d_h2ms , new int[]{R.raw.d_bf256 ,R.raw.d_f01}, NextConnections.forMuensingen()),
            new Station("Thun", false, "", R.raw.d_h2th,null, NextConnections.forThun()),
            new Station("Spiez",false,"", R.raw.d_h2sp,null, NextConnections.forThun()),
            new Station("Lattigen bei Spiez",false,"", R.raw.d_h2lat),
            new Station("Wimmis", false, "", R.raw.d_h2wm),
            new Station("Burgholz",false,"", R.raw.d_h2bh),
            new Station("Oey-Diemtigen",false,"", R.raw.d_h2oed),
            new Station("Erlenbach im Simmental",false,"", R.raw.d_h2ebis),
            new Station("Ringoldingen",true,"", R.raw.d_h2rgd, new int[]{R.raw.d_f01}),
            new Station("Därstetten",true,"", R.raw.d_h1dr, new int[]{R.raw.d_f01}),
            new Station("Weissenburg",true,"", R.raw.d_h2wg, new int[]{R.raw.d_f01}),
            new Station("Oberwil im Simmental",false,"", R.raw.d_h2ois),
            new Station("Enge im Simmental",false,"", R.raw.d_h2enge),
            new Station("Boltigen",false,"", R.raw.d_h2bolt),
            new Station("Zweisimmen",false,"Endbahnhof, bitte alle aussteigen.", R.raw.d_h2zw,new int[]{R.raw.d_e9104})
    };
    public Station[] redomo = {
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.d_h2ms , new int[]{R.raw.d_bf256 ,R.raw.d_f01}, NextConnections.forMuensingen()),
            new Station("Thun", false, "", R.raw.d_h2th,null, NextConnections.forThun()),
            new Station("Spiez",false,"", R.raw.d_h2sp,null, NextConnections.forThun()),
            new Station("Mülenen",true,"", R.raw.d_h2mue),
            new Station("Reichenbach im Kandertal",false,"", R.raw.d_h2reik),
            new Station("Frutigen",false,"", R.raw.d_h2fr),
            new Station("Kandersteg",false,"", R.raw.d_h2ka),
            new Station("Goppenstein",false,"", R.raw.d_h2go),
            new Station("Hohtenn",false,"", R.raw.d_h2ht),
            new Station("Ausserberg",true,"", R.raw.d_h2ab, new int[]{R.raw.d_f01}),
            new Station("Eggerberg",false,"", R.raw.d_h2eb),
            new Station("Lalden",false,"", R.raw.d_h2ll),
            new Station("Brig",false,"", R.raw.d_h2br),
            new Station("Iselle di Trasquera",false,""),
            new Station("Varzo",false,""),
            new Station("Domodossola (I)",false,"")
    };
    public Station[] shorttrain = {
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.d_h2ms , new int[]{R.raw.d_bf256 ,R.raw.d_f01}, NextConnections.forMuensingen()),
            new Station("Wichtrach",false,"Geschätzte Fahrgäste. Unser Zug kann nicht weiterfahren. Wir bitten Sie auszusteigen und für Ihre Weiterreise die Informationen am Bahnhof zu beachten. Für die Unannehmlichkeiten bitten wir Sie um Entschuldigung.\n", R.raw.d_h2wch, new int[]{R.raw.d_bf223})
    };


}
