package adry.earthquake;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import adry.earthquake.json.Feature;
import adry.earthquake.json.FeatureCollection;

import javax.swing.*;
import java.awt.*;

public class EarthquakeFrame extends JFrame {

    private JList<String> jlist = new JList<>();

    public EarthquakeFrame() {

        setTitle("Earthquake Frame");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        add(jlist, BorderLayout.CENTER);

        adry.earthquake.EarthquakeService service = new EarthquakeServiceFactory().getService();

        Disposable disposable = service.oneHour()
                // tells Rx to request the data on a background Thread
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread
                .observeOn(SwingSchedulers.edt())
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(
                        (response) -> handleResponse(response),
                        Throwable::printStackTrace);

        adry.earthquake.EarthquakeService service1 = new EarthquakeServiceFactory().getService();
        Disposable disposable2 = service.thirtyDays()
                // tells Rx to request the data on a background Thread
                .subscribeOn(Schedulers.io())
                // tells Rx to handle the response on Swing's main Thread
                .observeOn(SwingSchedulers.edt())
                //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                .subscribe(
                        (response) -> handleResponse2(response),
                        Throwable::printStackTrace);
    }

    private void handleResponse(FeatureCollection response) {

        String[] listData = new String[response.features.length];
        for (int i = 0; i < response.features.length; i++) {
            Feature feature = response.features[i];
            listData[i] = feature.properties.mag + " " + feature.properties.place;
        }
        jlist.setListData(listData);
    }

    private void handleResponse2(FeatureCollection response) {

        String[] listData1 = new String[response.features.length];
        for (int i = 0; i < response.features.length; i++) {
            Feature feature = response.features[i];
            listData1[i] = feature.properties.mag + " " + feature.properties.place;
        }
        jlist.setListData(listData1);
    }

    public static void main(String[] args) {
        new EarthquakeFrame().setVisible(true);
    }

}
