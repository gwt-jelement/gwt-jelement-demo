package gwt.jelement.demo.client.maps;

import gwt.jelement.core.JsObject;
import gwt.jelement.dom.Element;
import jsinterop.annotations.JsType;
import net.sourceforge.htmlunit.corejs.javascript.annotations.JSConstructor;

@JsType(isNative = true, namespace = "google.maps")
public class Map {
    @JSConstructor
    public Map(Element element, JsObject mapOptions){}
}
