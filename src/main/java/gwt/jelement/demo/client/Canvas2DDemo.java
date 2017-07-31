package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.canvas2d.CanvasRenderingContext2D;
import gwt.jelement.core.Date;
import gwt.jelement.core.Math;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.html.HTMLCanvasElement;
import gwt.jelement.html.HTMLImageElement;
import gwt.jelement.html.Image;

import static gwt.jelement.Browser.*;

public class Canvas2DDemo extends AbstractDemo {

    private double timer;

    @Override
    protected void execute() {
        clockDemo();
        planetsDemo();
    }

    /*
        Planet demo by Mozilla Developer Network
        https://jsfiddle.net/api/mdn/
     */
    private void planetsDemo() {
        Image sun = new Image();
        Image moon = new Image();
        Image earth = new Image();
        sun.setSrc("https://mdn.mozillademos.org/files/1456/Canvas_sun.png");
        moon.setSrc("https://mdn.mozillademos.org/files/1443/Canvas_moon.png");
        earth.setSrc("https://mdn.mozillademos.org/files/1429/Canvas_earth.png");

        HTMLCanvasElement canvas = document.getElementById("sun-canvas");
        CanvasRenderingContext2D ctx = canvas.getContext("2d").asCanvasRenderingContext2D();
        window.requestAnimationFrame(v -> drawPlanets(ctx, earth, moon, sun));
    }

    private void drawPlanets(CanvasRenderingContext2D ctx, HTMLImageElement earth, HTMLImageElement moon, HTMLImageElement sun) {
        ctx.setGlobalCompositeOperation("destination-over");
        ctx.clearRect(0, 0, 300, 300);
        ctx.setFillStyle("rgba(0, 0, 0, 0.4)");
        ctx.setStrokeStyle("rgba(0, 153, 255, 0.4)");
        ctx.save();
        ctx.translate(150, 150);

        Date time = new Date();
        ctx.rotate(((2 * Math.PI) / 60) * time.getSeconds() + ((2 * Math.PI) / 60000) * time.getMilliseconds());
        ctx.translate(105, 0);
        ctx.fillRect(0, -12, 50, 24);
        ctx.drawImage(earth, -12, -12);

        ctx.save();
        ctx.rotate(((2 * Math.PI) / 6) * time.getSeconds() + ((2 * Math.PI) / 6000) * time.getMilliseconds());
        ctx.translate(0, 28.5);
        ctx.drawImage(moon, -3.5, -3.5);
        ctx.restore();

        ctx.restore();

        ctx.beginPath();
        ctx.arc(150, 150, 105, 0, Math.PI * 2, false);
        ctx.stroke();

        ctx.drawImage(sun, 0, 0, 300, 300);
        if (isDemoActive()) {
            window.requestAnimationFrame(v -> drawPlanets(ctx, earth, moon, sun));
        }
    }

    private void clockDemo() {
        HTMLCanvasElement clockCanvas = document.getElementById("clock-canvas");
        CanvasRenderingContext2D context = clockCanvas.getContext("2d").asCanvasRenderingContext2D();
        timer = window.setInterval(args -> {
            Date time = new Date();
            double hours = time.getHours() % 12 + time.getMinutes() / 60d;
            double minutes = time.getMinutes() + time.getSeconds() / 60d;
            double seconds = time.getSeconds() + time.getMilliseconds() / 1000d;
            double offset = -Math.PI / 2;
            double hoursX = 75 * Math.cos(Math.PI * 2 * hours / 12 + offset) + 100;
            double hoursY = 75 * Math.sin(Math.PI * 2 * hours / 12 + offset) + 100;
            double minX = 80 * Math.cos(Math.PI * 2 * minutes / 60 + offset) + 100;
            double minY = 80 * Math.sin(Math.PI * 2 * minutes / 60 + offset) + 100;
            double secX = 80 * Math.cos(Math.PI * 2 * seconds / 60 + offset) + 100;
            double secY = 80 * Math.sin(Math.PI * 2 * seconds / 60 + offset) + 100;
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
        }, 50);
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
