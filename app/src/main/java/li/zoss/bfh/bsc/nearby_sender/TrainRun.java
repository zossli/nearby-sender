package li.zoss.bfh.bsc.nearby_sender;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Reto on 01.01.2018.
 */

class TrainRun {

    public void setAll(ArrayList<Train> list) {
        list.add(new Train("S1",s1BernThun,"Achtung: Kein Ausstieg aus diesem Wagen in Münsingen. Perron zu kurz. Bitte begeben Sie sich zum Aussteigen in den vorderen Wagen."));
        list.add(new Train("RE",redomo));
        list.add(new Train("RE",rezweisimmen));

    }
    public Station[] s1BernThun = {
            new Station("Bern Wankdorf", false, ""),
            new Station("Ostermundigen", false, ""),
            new Station("Gümligen",false,""),
            new Station("Rubigen",false,""),
            new Station("Münsingen", true, "Infolge Bauarbeiten wird dieser Bahnhof nur als Halt auf Verlangen angefahren."),
            new Station("Wichtrach",false,""),
            new Station("Kiesen",false,""),
            new Station("Uttigen",false,""),
            new Station("Thun",false, "Endbahnhof, bitte alle aussteigen.")
    };
    public Station[] rezweisimmen = {
            new Station("Münsingen", false, ""),
            new Station("Thun", false, ""),
            new Station("Spiez",false,""),
            new Station("Lattigen bei Spiez",false,""),
            new Station("Wimmis", false, ""),
            new Station("Burgholz",false,""),
            new Station("Oey-Diemtigen",false,""),
            new Station("Erlenbach im Simmental",false,""),
            new Station("Ringoldingen",true,""),
            new Station("Därstetten",true,""),
            new Station("Weissenburg",true,""),
            new Station("Oberwil im Simmental",false,""),
            new Station("Enge im Simmental",false,""),
            new Station("Boltigen",false,""),
            new Station("Zweisimmen",false,"Endbahnhof, bitte alle aussteigen.")
    };
    public Station[] redomo = {
            new Station("Münsingen", false, ""),
            new Station("Thun", false, ""),
            new Station("Spiez",false,""),
            new Station("Mülenen",true,""),
            new Station("Reichenbach im Kandertal",false,""),
            new Station("Frutigen",false,""),
            new Station("Kandersteg",false,""),
            new Station("Goppenstein",false,""),
            new Station("Hohtenn",false,""),
            new Station("Ausserberg",true,""),
            new Station("Eggerberg",false,""),
            new Station("Lalden",false,""),
            new Station("Brig",false,""),
            new Station("Iselle di Trasquera",false,""),
            new Station("Varzo",false,""),
            new Station("Domodossola (I)",false,"")
    };


}
