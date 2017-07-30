package gwt.jelement.demo.client.jsinterop;

import gwt.jelement.core.Float32Array;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * a very bad mapping of a very old version of glMatrix mat3 class
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "mat3")
public class Mat3 {
    public static native Float32Array create();
    public static native void transpose(Float32Array normalMatrix);
}
