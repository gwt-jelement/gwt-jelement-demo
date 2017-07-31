package gwt.jelement.demo.client.jsinterop;

import gwt.jelement.core.Float32Array;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * a very bad mapping of a very old version of glMatrix mat4 class
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "mat4")
public class Mat4 {
    public static native Float32Array create();
    public static native void perspective(int i, double v, double v1, double v2, Float32Array pMatrix);
    public static native void identity(Float32Array mvMatrix);
    public static native void translate(Float32Array mvMatrix, double[] floats);
    public static native void rotate(Float32Array mvMatrix, double v, double[] floats);
    public static native void toInverseMat3(Float32Array mvMatrix, Float32Array normalMatrix);
}
