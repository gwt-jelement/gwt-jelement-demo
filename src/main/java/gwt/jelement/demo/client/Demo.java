package gwt.jelement.demo.client;

import com.google.gwt.core.client.EntryPoint;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.dom.Element;
import gwt.jelement.html.HTMLAnchorElement;
import gwt.jelement.html.HTMLDivElement;

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
        demoFrame = document.getElementById("demo-div");

        addDemo(new Canvas2DDemo());
        addDemo(new ElementAnimateDemo());
        addDemo(new GeoLocationDemo());
        addDemo(new WebAudioDemo());

        window.addEventListener("hashchange", event -> {
            hashChanged(location.getHash());
        });
        hashChanged(location.getHash());
    }

    private void hashChanged(String hash) {
        AbstractDemo demo = demoMap.get(location.getHash());
        if (demo!=null){
            demo.execute(demoFrame);
        }
    }

    private void addDemo(AbstractDemo demo) {
        HTMLAnchorElement anchor= (HTMLAnchorElement) document.createElement("a");
        String hash = "#" + demo.getName();
        anchor.setAttribute("href", hash);
        anchor.setInnerText(demo.getTitle());
        listing.appendChild(anchor);
        demoMap.put(hash, demo);
    }

}
