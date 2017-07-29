package gwt.jelement.demo.client;

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.resources.client.TextResource;
import gwt.jelement.core.CallbackFunction;
import gwt.jelement.core.JsObject;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.demo.client.maps.Map;
import gwt.jelement.demo.client.maps.Marker;
import gwt.jelement.geolocation.Coordinates;
import gwt.jelement.geolocation.Geolocation;
import gwt.jelement.geolocation.Position;
import gwt.jelement.geolocation.PositionCallback;
import gwt.jelement.html.HTMLDivElement;

import static gwt.jelement.Browser.*;

public class GeoLocationDemo extends AbstractDemo {

    private Coordinates coordinates;
    private static boolean scriptLoaded;

    @Override
    protected void execute() {
        HTMLDivElement demoContainer = document.getElementById("geolocation-demo");
        Geolocation geolocation = navigator.getGeolocation();
        geolocation.getCurrentPosition(new PositionCallback() {

            @Override
            public void handleEvent(Position position) {
                GeoLocationDemo.this.coordinates = position.getCoords();
                if (scriptLoaded) {
                    ready();
                }
            }
        });

        window.set("initMap", new CallbackFunction() {
            @Override
            public Object onInvoked(Object... objects) {
                scriptLoaded = true;
                if (coordinates != undefined) {
                    ready();
                }
                return null;
            }
        });
        if (!scriptLoaded) {
            ScriptInjector.fromUrl("https://maps.googleapis.com/maps/api/js?key=AIzaSyCtNpX88CIyWAySoExky_bbWR7ZF-gaVPQ&callback=initMap    ")
                    .setWindow(ScriptInjector.TOP_WINDOW).inject();
        }
    }

    private void ready() {
        JsObject position = new JsObject();
        position.set("lat", coordinates.getLatitude());
        position.set("lng", coordinates.getLongitude());

        /* FIXME get("0") */
        HTMLDivElement mapContainer = (HTMLDivElement) document.querySelectorAll("div#geolocation-demo div#map").get("0");
        JsObject mapOptions = new JsObject();
        mapOptions.set("zoom", 4);
        mapOptions.set("center", position);
        Map map = new Map(mapContainer, mapOptions);

        JsObject markerOptions = new JsObject();
        markerOptions.set("position", position);
        markerOptions.set("map", map);
        new Marker(markerOptions);
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
