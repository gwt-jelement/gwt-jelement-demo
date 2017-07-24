package gwt.jelement.demo.client;


import com.google.gwt.resources.client.TextResource;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.html.HTMLDivElement;
import gwt.jelement.html.HTMLPreElement;

import static gwt.jelement.Browser.document;

public abstract class AbstractDemo {

    public final void execute(HTMLDivElement demoFrame) {
        demoFrame.innerHTML = getTemplate().getText()+ HtmlClientBundle.INSTANCE.getGithubBanner().getText();
        try {
            execute();
        }catch(Exception e){
            //TODO something clever here
        }

        double contentHeight = demoFrame.offsetHeight;
                HTMLPreElement sourceContainer = (HTMLPreElement) document.createElement("pre");
        sourceContainer.innerText = getSource().getText();
        sourceContainer.style.setProperty("height", "calc(100vh - " + (contentHeight + 20) + "px)");
        sourceContainer.style.setProperty("overflow", "auto");
        sourceContainer.className="lang-Java";
        demoFrame.appendChild(sourceContainer);
        HighlightJs.highlightBlock(sourceContainer);
    }

    protected abstract String getName();

    protected abstract String getTitle();

    protected abstract TextResource getTemplate();

    protected abstract TextResource getSource();

    protected abstract void execute();


}
