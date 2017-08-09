package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.core.Array;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.dom.ClientRect;
import gwt.jelement.dom.Element;
import gwt.jelement.dom.Touch;
import gwt.jelement.dom.URL;
import gwt.jelement.events.MouseEvent;
import gwt.jelement.events.TouchEvent;
import gwt.jelement.fileapi.Blob;
import gwt.jelement.fileapi.BlobPropertyBag;
import gwt.jelement.html.HTMLAnchorElement;
import gwt.jelement.html.HTMLButtonElement;
import gwt.jelement.html.HTMLDivElement;
import gwt.jelement.html.HTMLInputElement;
import gwt.jelement.svg.SVGPathElement;

import java.util.ArrayList;

import static gwt.jelement.Browser.*;

public class SvgDemo extends AbstractDemo {
    /*
     Line smoothing code from https://stackoverflow.com/a/40700068/80075
      */
    private final int BUFFER_SIZE = 10;
    private String strokeColor;
    private Element svgElement;
    private ClientRect rect;
    private SVGPathElement path = null;
    private String strPath;
    private ArrayList<Point> buffer;

    @Override
    protected void execute() {
        svgElement = document.getElementById("svgElement");
        HTMLButtonElement btnUndo = document.getElementById("btnUndo");
        HTMLButtonElement btnRedo = document.getElementById("btnRedo");
        HTMLButtonElement btnDownload = document.getElementById("btnDownload");
        HTMLInputElement colorPicker = document.getElementById("colorPicker");

        if (isIE()) {
            HTMLDivElement toolbar = document.getElementById("toolbar");
            toolbar.removeChild(btnDownload);
            toolbar.removeChild(colorPicker);
        }

        strokeColor = colorPicker.getValue();

        ArrayList<SVGPathElement> undoBuffer = new ArrayList<>();
        ArrayList<SVGPathElement> redoBuffer = new ArrayList<>();

        svgElement.addEventListener("touchstart", event -> this.mapTouchEvent((TouchEvent) event, "mousedown"));
        svgElement.addEventListener("touchmove", event -> this.mapTouchEvent((TouchEvent) event, "mousemove"));
        svgElement.addEventListener("touchend", event -> this.mapTouchEvent((TouchEvent) event, "mouseup"));

        svgElement.addEventListener("mousedown", event -> {
            rect = svgElement.getBoundingClientRect();
            path = document.createElementNS("http://www.w3.org/2000/svg", "path");
            path.setAttribute("fill", "none");
            path.setAttribute("stroke", strokeColor);
            path.setAttribute("stroke-width", "2");
            buffer = new ArrayList<>();
            Point point = getMousePosition((MouseEvent) event);
            appendToBuffer(point);
            strPath = "M" + point.x + " " + point.y;
            path.setAttribute("d", strPath);
            svgElement.appendChild(path);
        });

        svgElement.addEventListener("mousemove", event -> {
            if (path != null) {
                appendToBuffer(getMousePosition((MouseEvent) event));
                updateSvgPath();
            }
        });

        svgElement.addEventListener("mouseup", event -> {
            undoBuffer.add(0, path);
            redoBuffer.clear();
            btnUndo.removeAttribute("disabled");
            btnRedo.setAttribute("disabled", "");
            path = null;
        });

        btnUndo.addEventListener("click", event -> {
            if (!undoBuffer.isEmpty()) {
                SVGPathElement oldPath = undoBuffer.remove(0);
                redoBuffer.add(0, oldPath);
                btnRedo.removeAttribute("disabled");
                svgElement.removeChild(oldPath);
                if (undoBuffer.isEmpty()) {
                    btnUndo.setAttribute("disabled", "");
                }
            }
        });
        btnRedo.addEventListener("click", event -> {
            if (!redoBuffer.isEmpty()) {
                SVGPathElement oldPath = redoBuffer.remove(0);
                undoBuffer.add(0, oldPath);
                btnUndo.removeAttribute("disabled");
                svgElement.appendChild(oldPath);
                if (redoBuffer.isEmpty()) {
                    btnRedo.setAttribute("disabled", "");
                }
            }
        });

        btnDownload.addEventListener("click", event -> {
            /* https://stackoverflow.com/a/38019175/80075 */
            String svgData = svgElement.getOuterHTML();
            BlobPropertyBag blobProperties = new BlobPropertyBag();
            blobProperties.setType("image/svg+xml;charset=utf-8");
            Blob svgBlob = new Blob(new Array<>(svgData), blobProperties);

            String svgUrl = URL.createObjectURL(svgBlob);
            HTMLAnchorElement downloadLink = document.createElement("a");
            downloadLink.setHref(svgUrl);
            downloadLink.setDownload("my_drawing.svg");
            document.getBody().appendChild(downloadLink);
            downloadLink.click();
            document.getBody().removeChild(downloadLink);
        });

        colorPicker.addEventListener("change", event -> strokeColor = colorPicker.getValue());
    }

    private void mapTouchEvent(TouchEvent event, String type) {
        event.preventDefault();
        if (event.getTouches().getLength() > 1 || ("touchend".equals(event.getType()) &&
                event.getTouches().getLength() > 0))
            return;
        Touch touch = event.getChangedTouches().get(0);
        MouseEvent newEvent = (MouseEvent) document.createEvent("MouseEvents");
        newEvent.initMouseEvent(type, true, true, window, 0,
                touch.getScreenX(), touch.getScreenY(), touch.getClientX(), touch.getClientY(),
                event.isCtrlKey(), event.isAltKey(), event.isShiftKey(), event.isMetaKey(),
                (short) 0, null);
        event.getTarget().dispatchEvent(newEvent);

    }

    private boolean isIE() {
        return "Microsoft Internet Explorer".equals(navigator.getAppName()) ||
                ("Netscape".equals(navigator.getAppName()) && navigator.getUserAgent().contains("Trident/"));
    }

    private Point getMousePosition(MouseEvent event) {
        return new Point(event.getClientX() - rect.getLeft() - svgElement.getClientLeft(),
                event.getPageY() - rect.getTop() - svgElement.getClientTop());
    }

    private void appendToBuffer(Point point) {
        buffer.add(point);
        if (buffer.size() > BUFFER_SIZE) {
            buffer.remove(0);
        }
    }

    private Point getAveragePoint(int offset) {
        int len = buffer.size();
        if (len % 2 == 1 || len >= BUFFER_SIZE) {
            double totalX = 0;
            double totalY = 0;
            int count = 0;

            for (int i = offset; i < len; i++) {
                count++;
                Point point = buffer.get(i);
                totalX += point.x;
                totalY += point.y;
            }
            return new Point(totalX / count, totalY / count);
        }
        return null;
    }

    private void updateSvgPath() {
        Point point = getAveragePoint(0);

        if (point != null) {
            strPath += " L" + point.x + " " + point.y;
            StringBuilder tmpPath = new StringBuilder();
            for (int offset = 2; offset < buffer.size(); offset += 2) {
                point = getAveragePoint(offset);
                if (point != null) {
                    tmpPath.append(" L").append(point.x).append(" ").append(point.y);
                }
            }
            path.setAttribute("d", strPath + tmpPath.toString());
        }
    }

    @Override
    protected String getName() {
        return "svg";
    }

    @Override
    protected String getTitle() {
        return "SVG Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getSvgHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getSvgSource();
    }

    private class Point {
        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
