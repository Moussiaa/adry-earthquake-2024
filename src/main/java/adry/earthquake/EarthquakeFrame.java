package adry.earthquake;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import adry.earthquake.json.Feature;
import adry.earthquake.json.FeatureCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EarthquakeFrame extends JFrame {

    private JList<String> jlist = new JList<>();

    public EarthquakeFrame() {

        setTitle("Earthquake Frame");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        JRadioButton hour;
        JRadioButton month;

        hour = new JRadioButton("One Hour");
        hour.addActionListener(e -> {
            adry.earthquake.EarthquakeService service = new EarthquakeServiceFactory().getService();
            Disposable disposable = service.oneHour()
                    .subscribeOn(Schedulers.io())
                    .observeOn(SwingSchedulers.edt())
                    .subscribe(
                            response -> handleResponse(response),
                            Throwable::printStackTrace);
        });

        month = new JRadioButton("30 Days");
        month.addActionListener(e -> {
            adry.earthquake.EarthquakeService service = new EarthquakeServiceFactory().getService();
            Disposable disposable = service.thirtyDays()
                    .subscribeOn(Schedulers.io())
                    .observeOn(SwingSchedulers.edt())
                    .subscribe(
                            response -> handleResponse2(response),
                            Throwable::printStackTrace);
        });


        ButtonGroup radioButtons = new ButtonGroup();
        radioButtons.add(hour);
        radioButtons.add(month);

        JPanel radioMenu = new JPanel(new GridLayout(1, 2));
        Color periwinkle = new Color(190, 210, 255);
        radioMenu.setBackground(periwinkle);
        radioMenu.add(hour);
        radioMenu.add(month);

        add(radioMenu, BorderLayout.NORTH);
        add(jlist, BorderLayout.CENTER);


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
