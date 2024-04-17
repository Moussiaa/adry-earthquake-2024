package adry.earthquake;

import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import adry.earthquake.json.Feature;
import adry.earthquake.json.FeatureCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class EarthquakeFrame extends JFrame {

    private JList<String> jlist = new JList<>();
    private FeatureCollection ftCollection;

    public EarthquakeFrame() {

        setTitle("Earthquake Frame");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        JRadioButton hour;
        JRadioButton month;

        EarthquakeService service = new EarthquakeServiceFactory().getService();

        hour = new JRadioButton("One Hour");
        hour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Disposable disposable = service.oneHour()
                        // tells Rx to request the data on a background Thread
                        .subscribeOn(Schedulers.io())
                        // tells Rx to handle the response on Swing's main Thread
                        .observeOn(SwingSchedulers.edt())
                        //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                        .subscribe(
                                (response) -> handleResponse(response),
                                Throwable::printStackTrace);
            }
        });


        month = new JRadioButton("30 Days");
        month.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Disposable disposable = service.thirtyDays()
                        // tells Rx to request the data on a background Thread
                        .subscribeOn(Schedulers.io())
                        // tells Rx to handle the response on Swing's main Thread
                        .observeOn(SwingSchedulers.edt())
                        //.observeOn(AndroidSchedulers.mainThread()) // Instead use this on Android only
                        .subscribe(
                                (response) -> handleResponse(response),
                                Throwable::printStackTrace);
            }
        });


        {


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
            jlist.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent listSelectionEvent) {
                    int selectedIndex = jlist.getSelectedIndex();
                    Feature ft = ftCollection.features[selectedIndex];
                    double latitude = ft.geometry.getLatitude();
                    double longitude = ft.geometry.getLongitude();
                    try {
                        Desktop.getDesktop().browse(new URI("https://maps.google.com/?q=" + latitude
                                + "," + longitude));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    private void handleResponse(FeatureCollection response) {
        ftCollection = response;
        String[] listData = new String[response.features.length];
        for (int i = 0; i < response.features.length; i++) {
            Feature feature = response.features[i];
            listData[i] = feature.properties.mag + " " + feature.properties.place;
        }
        jlist.setListData(listData);
    }

    public static void main(String[] args) {
        new EarthquakeFrame().setVisible(true);
    }

}

