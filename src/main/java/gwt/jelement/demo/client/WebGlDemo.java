package gwt.jelement.demo.client;

import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.resources.client.TextResource;
import elemental2.core.Array;
import gwt.jelement.core.*;
import gwt.jelement.core.Math;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.demo.client.jsinterop.Mat3;
import gwt.jelement.demo.client.jsinterop.Mat4;
import gwt.jelement.dom.*;
import gwt.jelement.html.HTMLCanvasElement;
import gwt.jelement.html.HTMLImageElement;
import gwt.jelement.html.HTMLScriptElement;
import gwt.jelement.webgl.*;
import gwt.jelement.xmlhttprequest.XMLHttpRequest;

import static gwt.jelement.Browser.*;

/*
This is a port of the JavaScript code in "WebGL Lesson 14 – specular highlights and loading a JSON model",
http://learningwebgl.com/blog/?p=1658, by Giles Thomas

As with the original code, this code is licensed under a Creative Commons Attribution/Share-Alike license
(http://creativecommons.org/licenses/by-sa/3.0/)
 */

public class WebGlDemo extends AbstractDemo {

    static {
        /* inject a matrix manipulation library */
        ScriptInjector.fromString(HtmlClientBundle.INSTANCE.getGlMatrixJs().getText())
                .setWindow(ScriptInjector.TOP_WINDOW).inject();
    }

    Array mvMatrixStack = new Array(); /* FIXME implement Array */
    private WebGLRenderingContext gl;
    private double viewportWidth;
    private double viewportHeight;
    private WebGLTexture galvanizedTexture;
    private WebGLBuffer teapotVertexNormalBuffer;
    private WebGLBuffer teapotVertexPositionBuffer;
    private WebGLBuffer teapotVertexTextureCoordBuffer;
    private WebGLBuffer teapotVertexIndexBuffer;
    private WebGLProgram shaderProgram;
    private Float32Array mvMatrix = Mat4.create();
    private Float32Array pMatrix = Mat4.create();
    private float teapotAngle = 180f;
    private double vertexPositionAttribute;
    private double vertexNormalAttribute;
    private double textureCoordAttribute;
    private WebGLUniformLocation pMatrixUniform;
    private WebGLUniformLocation mvMatrixUniform;
    private WebGLUniformLocation nMatrixUniform;
    private WebGLUniformLocation samplerUniform;
    private WebGLUniformLocation materialShininessUniform;
    private WebGLUniformLocation showSpecularHighlightsUniform;
    private WebGLUniformLocation useTexturesUniform;
    private WebGLUniformLocation useLightingUniform;
    private WebGLUniformLocation ambientColorUniform;
    private WebGLUniformLocation pointLightingLocationUniform;
    private WebGLUniformLocation pointLightingDiffuseColorUniform;
    private WebGLUniformLocation pointLightingSpecularColorUniform;
    private int teapotVertexNormalBufferItemSize;
    private int teapotVertexTextureCoordBufferItemSize;
    private int teapotVertexPositionBufferItemSize;
    private int teapotVertexIndexBufferNumItems;
    private double lastTime = 0;

    @Override
    protected void execute() {
        HTMLCanvasElement canvas = document.getElementById("webgl-canvas");
        viewportWidth = canvas.getWidth();
        viewportHeight = canvas.getHeight();

        gl = canvas.getContext("experimental-webgl").asWebGLRenderingContext();

        initShaders();

        galvanizedTexture = createTexture("galvanized.jpg");

        loadTeapot();

        gl.clearColor((float) 0.0, (float) 0.0, (float) 0.0, (float) 1.0);
        gl.enable(WebGLRenderingContext.DEPTH_TEST);

        tick();
    }

    private void initShaders() {
        WebGLShader fragmentShader = getShader(gl, "per-fragment-lighting-fs");
        WebGLShader vertexShader = getShader(gl, "per-fragment-lighting-vs");

        shaderProgram = gl.createProgram();
        gl.attachShader(shaderProgram, vertexShader);
        gl.attachShader(shaderProgram, fragmentShader);
        gl.linkProgram(shaderProgram);

        if (!Js.isTrue(gl.getProgramParameter(shaderProgram, WebGLRenderingContext.LINK_STATUS))) {
            window.alert("Could not initialise shaders");
        }

        gl.useProgram(shaderProgram);

        vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
        gl.enableVertexAttribArray(vertexPositionAttribute);

        vertexNormalAttribute = gl.getAttribLocation(shaderProgram, "aVertexNormal");
        gl.enableVertexAttribArray(vertexNormalAttribute);

        textureCoordAttribute = gl.getAttribLocation(shaderProgram, "aTextureCoord");
        gl.enableVertexAttribArray(textureCoordAttribute);

        pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
        mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
        nMatrixUniform = gl.getUniformLocation(shaderProgram, "uNMatrix");
        samplerUniform = gl.getUniformLocation(shaderProgram, "uSampler");
        materialShininessUniform = gl.getUniformLocation(shaderProgram, "uMaterialShininess");
        showSpecularHighlightsUniform = gl.getUniformLocation(shaderProgram, "uShowSpecularHighlights");
        useTexturesUniform = gl.getUniformLocation(shaderProgram, "uUseTextures");
        useLightingUniform = gl.getUniformLocation(shaderProgram, "uUseLighting");
        ambientColorUniform = gl.getUniformLocation(shaderProgram, "uAmbientColor");
        pointLightingLocationUniform = gl.getUniformLocation(shaderProgram, "uPointLightingLocation");
        pointLightingSpecularColorUniform = gl.getUniformLocation(shaderProgram, "uPointLightingSpecularColor");
        pointLightingDiffuseColorUniform = gl.getUniformLocation(shaderProgram, "uPointLightingDiffuseColor");
    }

    private WebGLShader getShader(WebGLRenderingContext gl, String id) {
        HTMLScriptElement shaderScript = document.getElementById(id);
        String shaderSource = "";
        Node node = shaderScript.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.TEXT_NODE) {
                shaderSource += node.getTextContent();
            }
            node = node.getNextSibling();
        }

        WebGLShader shader;
        if ("x-shader/x-fragment".equals(shaderScript.getType())) {
            shader = gl.createShader(WebGLRenderingContext.FRAGMENT_SHADER);
        } else if (shaderScript.getType() == "x-shader/x-vertex") {
            shader = gl.createShader(WebGLRenderingContext.VERTEX_SHADER);
        } else {
            return null;
        }

        gl.shaderSource(shader, shaderSource);
        gl.compileShader(shader);

        if (!Js.isTrue(gl.getShaderParameter(shader, WebGLRenderingContext.COMPILE_STATUS))) {
            window.alert(gl.getShaderInfoLog(shader));
            return null;
        }

        return shader;
    }

    private WebGLTexture createTexture(String imageSource) {
        WebGLTexture texture = gl.createTexture();
        /* FIXME support named constructors: Image image=new Image(); */
        final HTMLImageElement image = document.createElement("img");
        image.setOnLoad(event -> {
            handleLoadedTexture(gl, texture, image);
            return null;
        });
        image.setSrc(imageSource);
        return texture;
    }

    private void handleLoadedTexture(WebGLRenderingContext gl, WebGLTexture texture, HTMLImageElement image) {
        gl.pixelStorei(WebGLRenderingContext.UNPACK_FLIP_Y_WEBGL, 1);
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, texture);
        gl.texImage2D(WebGLRenderingContext.TEXTURE_2D, 0, WebGLRenderingContext.RGBA,
                WebGLRenderingContext.RGBA, WebGLRenderingContext.UNSIGNED_BYTE, image);
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MAG_FILTER,
                WebGLRenderingContext.LINEAR);
        gl.texParameteri(WebGLRenderingContext.TEXTURE_2D, WebGLRenderingContext.TEXTURE_MIN_FILTER,
                WebGLRenderingContext.LINEAR_MIPMAP_NEAREST);
        gl.generateMipmap(WebGLRenderingContext.TEXTURE_2D);
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, null);
    }

    private void loadTeapot() {
        XMLHttpRequest request = new XMLHttpRequest();
        request.open("GET", "Teapot.json");
        request.setOnReadystatechange(event -> {
            if (request.getReadyState() == XMLHttpRequest.DONE) {
                handleLoadedTeapot(gl, (JsObject) JSON.parse(request.getResponseText()));
            }
            return null;
        });
        request.send();
    }

    private void handleLoadedTeapot(WebGLRenderingContext gl, JsObject teapotData) {
        teapotVertexNormalBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, teapotVertexNormalBuffer);
        float[] vertexNormals = toFloatArray((Array) teapotData.get("vertexNormals"));
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, new Float32Array(vertexNormals),
                WebGLRenderingContext.STATIC_DRAW);
        teapotVertexNormalBufferItemSize = 3;

        teapotVertexTextureCoordBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, teapotVertexTextureCoordBuffer);
        float[] vertexTextureCoords = toFloatArray((Array) teapotData.get("vertexTextureCoords"));
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, new Float32Array(vertexTextureCoords),
                WebGLRenderingContext.STATIC_DRAW);
        teapotVertexTextureCoordBufferItemSize = 2;

        teapotVertexPositionBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, teapotVertexPositionBuffer);
        float[] vertexPositions = toFloatArray((Array) teapotData.get("vertexPositions"));
        gl.bufferData(WebGLRenderingContext.ARRAY_BUFFER, new Float32Array(vertexPositions),
                WebGLRenderingContext.STATIC_DRAW);
        teapotVertexPositionBufferItemSize = 3;

        teapotVertexIndexBuffer = gl.createBuffer();
        gl.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, teapotVertexIndexBuffer);
        short[] indices = toShortArray((Array) teapotData.get("indices"));
        gl.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, new Uint16Array(indices),
                WebGLRenderingContext.STATIC_DRAW);
        teapotVertexIndexBufferNumItems = indices.length;
    }

    private float[] toFloatArray(Array array) {
        int size = (int) array.length;
        float[] result = new float[size];
        for (int index = 0; index < size; ++index) {
            result[index] = ((Double) array.getAt(index)).floatValue();
        }
        return result;
    }

    private short[] toShortArray(Array array) {
        int size = (int) array.length;
        short[] result = new short[size];
        for (int index = 0; index < size; ++index) {
            result[index] = ((Double) array.getAt(index)).shortValue();
        }
        return result;
    }


    private void tick() {
        if (isActiveDemo()) {
            window.requestAnimationFrame(v -> tick());
            drawScene();
            animate();
        }
    }

    private void drawScene() {
        gl.viewport(0, 0, viewportWidth, viewportHeight);
        gl.clear((int) WebGLRenderingContext.COLOR_BUFFER_BIT | (int) WebGLRenderingContext.DEPTH_BUFFER_BIT);

        if (teapotVertexPositionBuffer == null || teapotVertexNormalBuffer == null ||
                teapotVertexTextureCoordBuffer == null || teapotVertexIndexBuffer == null) {
            return;
        }
        Mat4.perspective(45, viewportWidth / viewportHeight, 0.1, 100.0, pMatrix);
        gl.uniform1i(showSpecularHighlightsUniform, 1);
        gl.uniform1i(useLightingUniform, 1);
        gl.uniform3f(ambientColorUniform, 0.2f, 0.2f, 0.2f);
        gl.uniform3f(pointLightingLocationUniform, -10f, 4, -20f);
        gl.uniform3f(pointLightingSpecularColorUniform, .8f, .8f, .8f);
        gl.uniform3f(pointLightingDiffuseColorUniform, .8f, .8f, .8f);
        gl.uniform1i(useTexturesUniform, 1);

        Mat4.identity(mvMatrix);
        Mat4.translate(mvMatrix, new float[]{0, 0, -40});
        Mat4.rotate(mvMatrix, degToRad(23.4f), new float[]{1, 0, -1});
        Mat4.rotate(mvMatrix, degToRad(teapotAngle), new float[]{0, 1, 0});

        gl.activeTexture(WebGLRenderingContext.TEXTURE0);
        gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, galvanizedTexture);
        gl.uniform1i(samplerUniform, 0);
        gl.uniform1f(materialShininessUniform, 32f);

        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, teapotVertexPositionBuffer);
        gl.vertexAttribPointer(vertexPositionAttribute, teapotVertexPositionBufferItemSize,
                WebGLRenderingContext.FLOAT, false, 0, 0);

        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, teapotVertexTextureCoordBuffer);
        gl.vertexAttribPointer(textureCoordAttribute, teapotVertexTextureCoordBufferItemSize,
                WebGLRenderingContext.FLOAT, false, 0, 0);

        gl.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, teapotVertexNormalBuffer);
        gl.vertexAttribPointer(vertexNormalAttribute, teapotVertexNormalBufferItemSize, WebGLRenderingContext.FLOAT,
                false, 0, 0);

        gl.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, teapotVertexIndexBuffer);
        setMatrixUniforms();
        gl.drawElements(WebGLRenderingContext.TRIANGLES, teapotVertexIndexBufferNumItems,
                WebGLRenderingContext.UNSIGNED_SHORT, 0);
    }

    private void setMatrixUniforms() {
        gl.uniformMatrix4fv(pMatrixUniform, false, pMatrix);
        gl.uniformMatrix4fv(mvMatrixUniform, false, mvMatrix);

        Float32Array normalMatrix = Mat3.create();
        Mat4.toInverseMat3(mvMatrix, normalMatrix);
        Mat3.transpose(normalMatrix);
        gl.uniformMatrix3fv(nMatrixUniform, false, normalMatrix);
    }

    private float degToRad(float degrees) {
        return (float) (degrees * Math.PI / 180f);
    }

    private void animate() {
        double timeNow = new Date().getTime();
        if (lastTime != 0) {
            double elapsed = timeNow - lastTime;
            teapotAngle += 0.05 * elapsed;
        }
        lastTime = timeNow;
    }

    @Override
    protected String getName() {
        return "webgl";
    }

    @Override
    protected String getTitle() {
        return "WebGL Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getWebGlHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getWebGlSource();
    }
}
