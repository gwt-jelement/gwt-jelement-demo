package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.canvas2d.CanvasRenderingContext2D;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.dom.FrameRequestCallback;
import gwt.jelement.html.HTMLCanvasElement;
import gwt.jelement.jelement.Function;

import java.util.Date;

import static gwt.jelement.Browser.document;
import static gwt.jelement.Browser.window;

public class Canvas2DDemo extends AbstractDemo {

    @Override
    protected void execute() {
        clockDemo();

        HTMLCanvasElement canvas = (HTMLCanvasElement) document.getElementById("my-canvas");
        CanvasRenderingContext2D context = (CanvasRenderingContext2D) canvas.getContext("2d");

        context.fillStyle = CanvasRenderingContext2D.StringOrCanvasGradientOrCanvasPatternUnionType.of("green");
        context.fillRect(10, 10, 150, 150);

        context.beginPath();
        context.fillStyle = CanvasRenderingContext2D.StringOrCanvasGradientOrCanvasPatternUnionType.of("yellow");
        context.ellipse(50, 50, 25, 25, 0, 0, (float) (Math.PI * 2));
        context.fill();
    }

    private void clockDemo() {
        HTMLCanvasElement clockCanvas = (HTMLCanvasElement) document.getElementById("clock-canvas");
        CanvasRenderingContext2D context = (CanvasRenderingContext2D) clockCanvas.getContext("2d");
        window.setInterval(new Function() {
            @Override
            public Object callback(Object... objects) {
                Date time = new Date();
                double hours = (double)(time.getHours() % 12) +time.getMinutes()/60d;
                double minutes = time.getMinutes() + time.getSeconds()/60d;
                double seconds = time.getSeconds()+(time.getTime()%1000)/1000d;
                double offset = -Math.PI / 2;
                float hoursX = (float) (75 * Math.cos(Math.PI * 2 * hours / 12 + offset) + 100);
                float hoursY = (float) (75 * Math.sin(Math.PI * 2 * hours / 12 + offset) + 100);
                float minX = (float) (80 * Math.cos(Math.PI * 2 * minutes / 60 + offset) + 100);
                float minY = (float) (80 * Math.sin(Math.PI * 2 * minutes / 60 + offset) + 100);
                float secX = (float) (80 * Math.cos(Math.PI * 2 * seconds / 60 + offset) + 100);
                float secY = (float) (80 * Math.sin(Math.PI * 2 * seconds / 60 + offset) + 100);
                window.requestAnimationFrame(new FrameRequestCallback() {
                    @Override
                    public void handleEvent(double v) {
                        context.clearRect(0, 0, 200, 200);
                        context.beginPath();
                        context.strokeStyle = CanvasRenderingContext2D.StringOrCanvasGradientOrCanvasPatternUnionType.of("black");
                        context.lineWidth = 4;
                        context.moveTo(100, 100);
                        context.lineTo(hoursX, hoursY);
                        context.stroke();
                        context.beginPath();
                        context.strokeStyle = CanvasRenderingContext2D.StringOrCanvasGradientOrCanvasPatternUnionType.of("gray");
                        context.lineWidth = 3;
                        context.moveTo(100, 100);
                        context.lineTo(minX, minY);
                        context.stroke();
                        context.beginPath();
                        context.lineWidth = 1;
                        context.strokeStyle = CanvasRenderingContext2D.StringOrCanvasGradientOrCanvasPatternUnionType.of("red");
                        context.moveTo(100, 100);
                        context.lineTo(secX, secY);
                        context.stroke();
                    }
                });
                return null;
            }
        }, 100);
    }

    @Override
    protected String getName() {
        return "canvas2d";
    }

    @Override
    protected String getTitle() {
        return "Canvas 2D Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getCanvas2DHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getCanvas2DDemoSource();
    }
}
