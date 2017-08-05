package gwt.jelement.demo.client.jsinterop;

import gwt.jelement.core.JsObject;
import jsinterop.annotations.JsType;
import net.sourceforge.htmlunit.corejs.javascript.annotations.JSConstructor;

@JsType(isNative = true, namespace = "google.maps", name="Marker")
public class GoogleMapMarker {

    @JSConstructor
    public GoogleMapMarker(JsObject<?> options){}
}
