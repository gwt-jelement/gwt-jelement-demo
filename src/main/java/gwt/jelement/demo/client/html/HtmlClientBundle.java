package gwt.jelement.demo.client.html;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface HtmlClientBundle extends ClientBundle {

    HtmlClientBundle INSTANCE= GWT.create(HtmlClientBundle.class);

    @Source("webaudio.html")
    TextResource getWebAudioHtml();

    @Source("canvas2d.html")
    TextResource getCanvas2DHtml();

    @CssResource.NotStrict
    @Source("default.css")
    CssResource getStyle();
}
