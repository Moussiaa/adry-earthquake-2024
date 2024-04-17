package adry.earthquake.json;

import java.util.List;

public class Geometry {
    public List<Double> coordinates;

    public double getLongitude() {

        return coordinates.get(0);
    }

    public double getLatitude() {

        return coordinates.get(1);
    }
}
