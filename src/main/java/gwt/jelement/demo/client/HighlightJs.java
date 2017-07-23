package gwt.jelement.demo.client;


import gwt.jelement.dom.Node;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(name = "hljs", namespace = JsPackage.GLOBAL, isNative = true)
public class HighlightJs {
    static native void highlightBlock(Node node);
}
