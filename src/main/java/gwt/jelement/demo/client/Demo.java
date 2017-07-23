package gwt.jelement.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import gwt.jelement.canvas2d.CanvasRenderingContext2D;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.dom.Element;
import gwt.jelement.events.Event;
import gwt.jelement.events.EventListener;
import gwt.jelement.html.HTMLAnchorElement;
import gwt.jelement.html.HTMLButtonElement;
import gwt.jelement.html.HTMLCanvasElement;
import gwt.jelement.html.HTMLDivElement;
import gwt.jelement.webaudio.*;

import java.util.HashMap;
import java.util.Map;

import static gwt.jelement.Browser.document;
import static gwt.jelement.Browser.window;
import static gwt.jelement.Browser.location;

public class Demo implements EntryPoint {

    Map<String, AbstractDemo> demoMap=new HashMap<>();
    private Element listing;
    private HTMLDivElement demoFrame;

    @Override
    public void onModuleLoad() {
        HtmlClientBundle.INSTANCE.getStyle().ensureInjected();
        listing = document.getElementById("demo-listing");
        demoFrame = (HTMLDivElement) document.getElementById("demo-div");

        addDemo(new Canvas2DDemo());
        addDemo(new WebAudioDemo());

        window.addEventListener("hashchange", event -> {
            hashChanged(location.hash);
        });
        hashChanged(location.hash);
    }

    private void hashChanged(String hash) {
        AbstractDemo demo = demoMap.get(location.hash);
        if (demo!=null){
            demo.execute(demoFrame);
        }
    }

    private void addDemo(AbstractDemo demo) {
        HTMLAnchorElement anchor= (HTMLAnchorElement) document.createElement("a");
        String hash = "#" + demo.getName();
        anchor.setAttribute("href", hash);
        anchor.innerText=demo.getTitle();
        listing.appendChild(anchor);
        demoMap.put(hash, demo);
    }

}
