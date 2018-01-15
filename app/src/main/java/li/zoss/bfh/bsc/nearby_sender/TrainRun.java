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
            new Station("Bern Wankdorf", false, "", R.raw.jinglepublic),
            new Station("Ostermundigen", false, "",R.raw.jinglepublic),
            new Station("Gümligen",false,"", R.raw.jinglepublic),
            new Station("Rubigen",false,"", R.raw.jinglepublic),
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.jinglepublic , new int[]{R.raw.jinglepublic ,R.raw.jinglepublic}, NextConnections.forMuensingen()),
            new Station("Wichtrach",false,"", R.raw.jinglepublic),
            new Station("Kiesen",false,"",R.raw.jinglepublic),
            new Station("Uttigen",false,"", R.raw.jinglepublic),
            new Station("Thun",false, "Endbahnhof, bitte alle aussteigen.", R.raw.jinglepublic, new int[]{R.raw.jinglepublic}, NextConnections.forThun())
    };
    public Station[] rezweisimmen = {
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.jinglepublic , new int[]{R.raw.jinglepublic ,R.raw.jinglepublic}, NextConnections.forMuensingen()),
            new Station("Thun", false, "", R.raw.jinglepublic,null, NextConnections.forThun()),
            new Station("Spiez",false,"", R.raw.jinglepublic,null, NextConnections.forThun()),
            new Station("Lattigen bei Spiez",false,"", R.raw.jinglepublic),
            new Station("Wimmis", false, "", R.raw.jinglepublic),
            new Station("Burgholz",false,"", R.raw.jinglepublic),
            new Station("Oey-Diemtigen",false,"", R.raw.jinglepublic),
            new Station("Erlenbach im Simmental",false,"", R.raw.jinglepublic),
            new Station("Ringoldingen",true,"", R.raw.jinglepublic, new int[]{R.raw.jinglepublic}),
            new Station("Därstetten",true,"", R.raw.jinglepublic, new int[]{R.raw.jinglepublic}),
            new Station("Weissenburg",true,"", R.raw.d_h2wg, new int[]{R.raw.jinglepublic}),
            new Station("Oberwil im Simmental",false,"", R.raw.jinglepublic),
            new Station("Enge im Simmental",false,"", R.raw.jinglepublic),
            new Station("Boltigen",false,"", R.raw.jinglepublic),
            new Station("Zweisimmen",false,"Endbahnhof, bitte alle aussteigen.", R.raw.jinglepublic,new int[]{R.raw.jinglepublic})
    };
    public Station[] redomo = {
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.jinglepublic , new int[]{R.raw.jinglepublic ,R.raw.jinglepublic}, NextConnections.forMuensingen()),
            new Station("Thun", false, "", R.raw.jinglepublic,null, NextConnections.forThun()),
            new Station("Spiez",false,"", R.raw.jinglepublic,null, NextConnections.forThun()),
            new Station("Mülenen",true,"", R.raw.jinglepublic),
            new Station("Reichenbach im Kandertal",false,"", R.raw.jinglepublic),
            new Station("Frutigen",false,"", R.raw.jinglepublic),
            new Station("Kandersteg",false,"", R.raw.jinglepublic),
            new Station("Goppenstein",false,"", R.raw.jinglepublic),
            new Station("Hohtenn",false,"", R.raw.jinglepublic),
            new Station("Ausserberg",true,"", R.raw.jinglepublic, new int[]{R.raw.jinglepublic}),
            new Station("Eggerberg",false,"", R.raw.jinglepublic),
            new Station("Lalden",false,"", R.raw.jinglepublic),
            new Station("Brig",false,"", R.raw.jinglepublic),
            new Station("Iselle di Trasquera",false,""),
            new Station("Varzo",false,""),
            new Station("Domodossola (I)",false,"")
    };
    public Station[] shorttrain = {
            new Station("Münsingen", true, "Infolge Bauarbeiten - Halt auf Verlangen.", R.raw.jinglepublic , new int[]{R.raw.jinglepublic ,R.raw.jinglepublic}, NextConnections.forMuensingen()),
            new Station("Wichtrach",false,"Geschätzte Fahrgäste. Unser Zug kann nicht weiterfahren. Wir bitten Sie auszusteigen und für Ihre Weiterreise die Informationen am Bahnhof zu beachten. Für die Unannehmlichkeiten bitten wir Sie um Entschuldigung.\n", R.raw.jinglepublic, new int[]{R.raw.jinglepublic})
    };


}
