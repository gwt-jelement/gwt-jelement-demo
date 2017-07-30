package gwt.jelement.demo.client.jsinterop;

import gwt.jelement.core.JsObject;
import gwt.jelement.dom.Element;
import jsinterop.annotations.JsType;
import net.sourceforge.htmlunit.corejs.javascript.annotations.JSConstructor;

@JsType(isNative = true, namespace = "google.maps", name="Map")
public class GoogleMap {
    @JSConstructor
    public GoogleMap(Element element, JsObject mapOptions){}
}
