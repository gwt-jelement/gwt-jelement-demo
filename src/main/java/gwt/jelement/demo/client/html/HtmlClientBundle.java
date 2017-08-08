package gwt.jelement.demo.client.html;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface HtmlClientBundle extends ClientBundle {

    HtmlClientBundle INSTANCE = GWT.create(HtmlClientBundle.class);

    @Source("webaudio.html")
    TextResource getWebAudioHtml();

    @Source("canvas2d.html")
    TextResource getCanvas2DHtml();

    @Source("github-banner.html")
    TextResource getGitHubBanner();

    @Source("element-animate.html")
    TextResource getElementAnimateHtml();

    @Source("geolocation.html")
    TextResource getGeoLocationHtml();

    @Source("webgl.html")
    TextResource getWebGlHtml();

    @Source("battery.html")
    TextResource getBatteryHtml();

    @Source("svg.html")
    TextResource getSvgHtml();

    @Source("notification-demo.html")
    TextResource getNotificationHtml();

    @Source("speech-synth.html")
    TextResource getSpeechSynthHtml();

    @Source("glmatrix.0.9.5.min.js")
    TextResource getGlMatrixJs();

    @CssResource.NotStrict
    @Source("default.css")
    CssResource getStyle();
}
