package gwt.jelement.demo.client.jsinterop;

import gwt.jelement.core.JsObject;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "google.maps", name="Marker")
public class GoogleMapMarker {

    @JsConstructor
    public GoogleMapMarker(JsObject<?> options){}
}
