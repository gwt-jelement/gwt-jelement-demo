package gwt.jelement.demo.client;

import com.google.gwt.core.client.EntryPoint;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.dom.Element;
import gwt.jelement.html.HTMLAnchorElement;
import gwt.jelement.html.HTMLDivElement;

import java.util.HashMap;
import java.util.Map;

import static gwt.jelement.Browser.*;

public class Demo implements EntryPoint {

    private Map<String, AbstractDemo> demoMap = new HashMap<>();
    private Element listing;
    private HTMLDivElement demoFrame;
    private AbstractDemo demo;

    @Override
    public void onModuleLoad() {
        HtmlClientBundle.INSTANCE.getStyle().ensureInjected();
        listing = document.getElementById("demo-listing");
        demoFrame = document.getElementById("demo-div");

        addDemo(new BatteryDemo());
        addDemo(new Canvas2DDemo());
        addDemo(new ElementAnimateDemo());
        addDemo(new GeoLocationDemo());
        addDemo(new WebAudioDemo());
        addDemo(new WebGlDemo());

        window.addEventListener("hashchange", event -> hashChanged());
        hashChanged();
    }

    private void hashChanged() {
        if (demo != null) {
            demo.setIntactive();
        }
        demo = demoMap.get(location.getHash());
        if (demo != null) {
            demo.execute(demoFrame);
        }
    }

    private void addDemo(AbstractDemo demo) {
        HTMLAnchorElement anchor = document.createElement("a");
        String hash = "#" + demo.getName();
        anchor.setAttribute("href", hash);
        anchor.setInnerText(demo.getTitle());
        listing.appendChild(anchor);
        demoMap.put(hash, demo);
    }

}
