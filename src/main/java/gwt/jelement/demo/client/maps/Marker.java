package gwt.jelement.demo.client.maps;

import gwt.jelement.core.JsObject;
import jsinterop.annotations.JsType;
import net.sourceforge.htmlunit.corejs.javascript.annotations.JSConstructor;

@JsType(isNative = true, namespace = "google.maps")
public class Marker {

    @JSConstructor
    public Marker(JsObject options){}
}
