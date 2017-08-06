package gwt.jelement.demo.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface SourceClientBundle extends ClientBundle {

    SourceClientBundle INSTANCE = GWT.create(SourceClientBundle.class);

    @Source("WebAudioDemo.java")
    TextResource getWebAudioDemoSource();

    @Source("Canvas2DDemo.java")
    TextResource getCanvas2DDemoSource();

    @Source("ElementAnimateDemo.java")
    TextResource getElementAnimateDemoSource();

    @Source("GeoLocationDemo.java")
    TextResource getGeolocationSource();

    @Source("WebGlDemo.java")
    TextResource getWebGlSource();

    @Source("BatteryDemo.java")
    TextResource getBatterySource();

    @Source("NotificationDemo.java")
    TextResource getNotificationSource();

    @Source("SvgDemo.java")
    TextResource getSvgSource();
}
