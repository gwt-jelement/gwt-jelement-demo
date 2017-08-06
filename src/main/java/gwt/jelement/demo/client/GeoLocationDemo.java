package gwt.jelement.demo.client;

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.resources.client.TextResource;
import gwt.jelement.core.CallbackFunction;
import gwt.jelement.core.JsObject;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.demo.client.jsinterop.GoogleMap;
import gwt.jelement.demo.client.jsinterop.GoogleMapMarker;
import gwt.jelement.geolocation.Coordinates;
import gwt.jelement.geolocation.Geolocation;
import gwt.jelement.html.HTMLDivElement;

import static gwt.jelement.Browser.*;

public class GeoLocationDemo extends AbstractDemo {
    /* replace with your own Google Maps API key*/
    private static final String GOOGLE_API_KEY = "AIzaSyCtNpX88CIyWAySoExky_bbWR7ZF-gaVPQ";
    private static final String MAP_INIT_CALLBACK = "initMap";
    private static boolean scriptLoaded;
    private Coordinates coordinates;

    @Override
    protected void execute() {
        if (!navigator.object().has("geolocation")) {
            window.alert("Your browser does not support geolocation");
            return;
        }

        Geolocation geolocation = navigator.getGeolocation();
        geolocation.getCurrentPosition(position -> {
            GeoLocationDemo.this.coordinates = position.getCoords();
            if (scriptLoaded) {
                ready();
            }
        });

        window.object().set(MAP_INIT_CALLBACK, (CallbackFunction) objects -> {
            scriptLoaded = true;
            window.object().delete(MAP_INIT_CALLBACK);
            if (coordinates != null) {
                ready();
            }
            return null;
        });

        if (!scriptLoaded) {
            ScriptInjector.fromUrl("https://maps.googleapis.com/maps/api/js?key=" +
                    GOOGLE_API_KEY + "&callback=" + MAP_INIT_CALLBACK)
                    .setWindow(ScriptInjector.TOP_WINDOW).inject();
        }
    }

    private void ready() {
        HTMLDivElement mapContainer = document.querySelector("div#geolocation-demo div#map");

        JsObject<?> position = new JsObject<>().with("lat", coordinates.getLatitude())
                .with("lng", coordinates.getLongitude());

        JsObject<?> mapOptions = new JsObject<>().with("zoom", 10).with("center", position);
        GoogleMap map = new GoogleMap(mapContainer, mapOptions);

        JsObject<?> markerOptions = new JsObject<>().with("position", position).with("map", map);
        new GoogleMapMarker(markerOptions);
    }

    @Override
    protected String getName() {
        return "geolocation";
    }

    @Override
    protected String getTitle() {
        return "Geolocation Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getGeoLocationHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getGeolocationSource();
    }
}
