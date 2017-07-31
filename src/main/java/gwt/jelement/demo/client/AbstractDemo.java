package gwt.jelement.demo.client;


import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.resources.client.TextResource;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.demo.client.jsinterop.HighlightJs;
import gwt.jelement.html.HTMLDivElement;
import gwt.jelement.html.HTMLPreElement;

import static gwt.jelement.Browser.document;
import static gwt.jelement.Browser.window;

public abstract class AbstractDemo {

    private boolean active;

    final void execute(HTMLDivElement demoFrame) {
        demoFrame.setInnerHTML(getTemplate().getText() + HtmlClientBundle.INSTANCE.getGithubBanner().getText());
        active = true;
        try {
            execute();
        } catch (JavaScriptException e) {
            window.getConsole().error(e.getThrown());
        }

        double contentHeight = demoFrame.getOffsetHeight();
        HTMLPreElement sourceContainer = document.createElement("pre");
        sourceContainer.setInnerText(getSource().getText());
        sourceContainer.getStyle().setProperty("height", "calc(100vh - " + (contentHeight + 20) + "px)");
        sourceContainer.getStyle().setProperty("overflow", "auto");
        sourceContainer.setClassName("lang-Java");
        demoFrame.appendChild(sourceContainer);
        HighlightJs.highlightBlock(sourceContainer);

    }

    protected abstract String getName();

    protected abstract String getTitle();

    protected abstract TextResource getTemplate();

    protected abstract TextResource getSource();

    protected abstract void execute();

    protected void setIntactive() {
        active = false;
    }

    boolean isDemoActive() {
        return active;
    }

}
