package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.canvas2d.CanvasRenderingContext2D;
import gwt.jelement.core.CallbackFunction;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.html.HTMLCanvasElement;

import java.util.Date;

import static gwt.jelement.Browser.*;

public class Canvas2DDemo extends AbstractDemo {

    private double timer;

    @Override
    protected void execute() {
        clockDemo();

        HTMLCanvasElement canvas = document.getElementById("my-canvas");
        CanvasRenderingContext2D context = canvas.getContext("2d").asCanvasRenderingContext2D();

        context.setFillStyle("green");
        context.fillRect(10, 10, 150, 150);

        context.beginPath();
        context.setFillStyle("yellow");
        context.ellipse(50, 50, 25, 25, 0, 0, (float) (JsMath.PI * 2));
        context.fill();
    }

    private void clockDemo() {
        HTMLCanvasElement clockCanvas = document.getElementById("clock-canvas");
        CanvasRenderingContext2D context = clockCanvas.getContext("2d").asCanvasRenderingContext2D();
        timer=window.setInterval(new CallbackFunction() {
            @Override
            public Object onInvoked(Object... objects) {
                Date time = new Date();
                double hours = (double) (time.getHours() % 12) + time.getMinutes() / 60d;
                double minutes = time.getMinutes() + time.getSeconds() / 60d;
                double seconds = time.getSeconds() + (time.getTime() % 1000) / 1000d;
                double offset = -JsMath.PI / 2;
                float hoursX = (float) (75 * Math.cos(JsMath.PI * 2 * hours / 12 + offset) + 100);
                float hoursY = (float) (75 * Math.sin(JsMath.PI * 2 * hours / 12 + offset) + 100);
                float minX = (float) (80 * Math.cos(JsMath.PI * 2 * minutes / 60 + offset) + 100);
                float minY = (float) (80 * Math.sin(JsMath.PI * 2 * minutes / 60 + offset) + 100);
                float secX = (float) (80 * Math.cos(JsMath.PI * 2 * seconds / 60 + offset) + 100);
                float secY = (float) (80 * Math.sin(JsMath.PI * 2 * seconds / 60 + offset) + 100);
                context.clearRect(0, 0, 200, 200);
                context.beginPath();
                context.setStrokeStyle("black");
                context.setLineWidth(4);
                context.moveTo(100, 100);
                context.lineTo(hoursX, hoursY);
                context.stroke();
                context.beginPath();
                context.setStrokeStyle("gray");
                context.setLineWidth(3);
                context.moveTo(100, 100);
                context.lineTo(minX, minY);
                context.stroke();
                context.beginPath();
                context.setLineWidth(1);
                context.setStrokeStyle("red");
                context.moveTo(100, 100);
                context.lineTo(secX, secY);
                context.stroke();
                return null;
            }
        }, 100);
    }

    @Override
    protected void setIntactive() {
        window.clearInterval(timer);
        super.setIntactive();
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
