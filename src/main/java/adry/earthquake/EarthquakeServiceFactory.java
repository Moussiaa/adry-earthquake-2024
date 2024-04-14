package adry.earthquake;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class EarthquakeServiceFactory {
    public adry.earthquake.EarthquakeService getService() {
        // configure Retrofit for the website
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://earthquake.usgs.gov/")
                // Configure Retrofit to use Gson to turn the Json into Objects
                .addConverterFactory(GsonConverterFactory.create())
                // Configure Retrofit to use Rx
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build();

        return retrofit.create(adry.earthquake.EarthquakeService.class);
    }
}
