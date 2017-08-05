package gwt.jelement.demo.client.jsinterop;

import gwt.jelement.core.JsObject;
import gwt.jelement.dom.Element;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = "google.maps", name="Map")
public class GoogleMap {
    @JsConstructor
    public GoogleMap(Element element, JsObject<?> mapOptions){}
}
