package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import elemental2.core.Array;
import gwt.jelement.animation.KeyframeAnimationOptions;
import gwt.jelement.animation.PlaybackDirection;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.html.HTMLDivElement;
import gwt.jelement.html.HTMLImageElement;
import jsinterop.base.JsPropertyMap;

import static gwt.jelement.Browser.Infinity;
import static gwt.jelement.Browser.document;


public class ElementAnimateDemo extends AbstractDemo {

    @Override
    protected void execute() {
        HTMLDivElement mainDiv= (HTMLDivElement) document.querySelector("div.clouds");

        Array<JsPropertyMap<String>> cloudTransitions = new Array<>(
                JsPropertyMap.of("backgroundPosition", "0 302px"),
                JsPropertyMap.of("backgroundPosition", "0 0")
        );
        KeyframeAnimationOptions cloudAnimationOptions = new KeyframeAnimationOptions();
        cloudAnimationOptions.setDuration(10_000);
        cloudAnimationOptions.setIterations(Infinity);
        mainDiv.animate(cloudTransitions, cloudAnimationOptions);

        HTMLImageElement logo = document.createElement("img");
        logo.setSrc("gwtlogo.png");
        logo.getStyle().setProperty("position","relative");
        logo.getStyle().setProperty("left","20px");
        mainDiv.appendChild(logo);

        Array<JsPropertyMap<String>> logoTransitions = new Array<>(
                JsPropertyMap.of("transform", "translateY(0px)"),
                JsPropertyMap.of("transform", "translateY(45px)")
        );
        KeyframeAnimationOptions logoAnimationOptions = new KeyframeAnimationOptions();
        logoAnimationOptions.setDuration(4_000);
        logoAnimationOptions.setIterations(Infinity);
        logoAnimationOptions.setDirection(PlaybackDirection.ALTERNATE);
        logo.animate(logoTransitions, logoAnimationOptions);

        HTMLDivElement tickerDiv=document.createElement("div");
        tickerDiv.setClassName("ticker");
        HTMLDivElement textDiv=document.createElement("div");
        textDiv.setClassName("ticker-inner");
        textDiv.setInnerHTML("⚠️This demo uses the <a href=\"https://developer.mozilla.org/en-US/docs/Web/API/Element/animate\" " +
                "target=\"_blank\">Element.animate()</a> API, which is only supported in Chrome and Firefox. As this " +
                "API is fairly new, it is  not yet defined in Elemental.");
        tickerDiv.appendChild(textDiv);
        mainDiv.appendChild(tickerDiv);

        Array<JsPropertyMap<String>> tickerTransitions = new Array<>(
                JsPropertyMap.of("marginLeft", "100%"),
                JsPropertyMap.of("marginLeft", "-1200px")
        );
        KeyframeAnimationOptions tickerAnimationOptions = new KeyframeAnimationOptions();
        tickerAnimationOptions.setDuration(20_000);
        tickerAnimationOptions.setIterations(Infinity);
        textDiv.animate(tickerTransitions, tickerAnimationOptions);
    }

    @Override
    protected String getName() {
        return "element-animate";
    }

    @Override
    protected String getTitle() {
        return "Element.animate() demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getElementAnimateHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getElementAnimateDemoSource();
    }
}
