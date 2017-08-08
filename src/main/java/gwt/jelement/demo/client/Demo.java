package gwt.jelement.demo.client;

import com.google.gwt.core.client.EntryPoint;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.dom.Element;
import gwt.jelement.html.HTMLAnchorElement;
import gwt.jelement.html.HTMLDivElement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static gwt.jelement.Browser.*;

public class Demo implements EntryPoint {

    private final Map<String, Supplier<AbstractDemo>> demoMap = new HashMap<>();
    private Element listing;
    private HTMLDivElement demoFrame;
    private AbstractDemo demo;

    @Override
    public void onModuleLoad() {
        HtmlClientBundle.INSTANCE.getStyle().ensureInjected();
        listing = document.getElementById("demo-listing");
        demoFrame = document.getElementById("demo-div");

        addDemo(BatteryDemo::new);
        addDemo(Canvas2DDemo::new);
        addDemo(ElementAnimateDemo::new);
        addDemo(GeoLocationDemo::new);
        addDemo(NotificationDemo::new);
        addDemo(SpeechSynthesisDemo::new);
        addDemo(SvgDemo::new);
        addDemo(WebAudioDemo::new);
        addDemo(WebGlDemo::new);

        window.addEventListener("hashchange", event -> hashChanged());
        hashChanged();
    }

    private void hashChanged() {
        if (demo != null) {
            demo.setInactive();
        }
        Supplier<AbstractDemo> demoSuplier = demoMap.get(location.getHash());
        if (demoSuplier != null) {
            demo=demoSuplier.get();
            demo.execute(demoFrame);
        }
    }

    private void addDemo(Supplier<AbstractDemo> demoSupplier) {
        HTMLAnchorElement anchor = document.createElement("a");
        AbstractDemo demoInfo = demoSupplier.get();
        String hash = "#" + demoInfo.getName();
        anchor.setAttribute("href", hash);
        anchor.setInnerText(demoInfo.getTitle());
        listing.appendChild(anchor);
        demoMap.put(hash, demoSupplier);
    }

}
