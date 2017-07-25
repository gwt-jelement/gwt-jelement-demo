package gwt.jelement.demo.client;


import gwt.jelement.dom.Element;
import gwt.jelement.dom.Text;
import gwt.jelement.events.EventListener;

import static gwt.jelement.Browser.document;

public class BootstrapButton {

    private final Element button;
    private final Text textNode;

    public BootstrapButton(String caption) {
        this(caption, ButtonStyle.DEFAULT, null, null);
    }

    public BootstrapButton(String caption, ButtonStyle buttonStyle, String icon) {
        this(caption, buttonStyle, null, icon);
    }

    public BootstrapButton(String caption, ButtonStyle buttonStyle) {
        this(caption, buttonStyle, null, null);
    }

    public BootstrapButton(String caption, ButtonStyle buttonStyle, ButtonSize buttonSize) {
        this(caption, buttonStyle, buttonSize, null);
    }

    public BootstrapButton(String caption, ButtonStyle buttonStyle, ButtonSize buttonSize, String icon) {
        this.button = document.createElement("button");
        button.setClassName("btn btn-" + buttonStyle.name().toLowerCase() +
                (buttonSize != null && buttonSize != ButtonSize.DEFAULT ? (" " + buttonSize.getStyle()) : ""));
        if (icon != null) {
            Element span = document.createElement("span");
            span.setClassName("glyphicon glyphicon-" + icon);
            span.setAttribute("aria-hidden", "true");
            button.appendChild(span);
        }
        this.textNode = document.createTextNode(caption);
        if (caption != null) {
            button.appendChild(textNode);
        }
    }

    public void setDisabled(boolean disabled){
        if (disabled){
            button.setAttribute("disabled", "disabaled");
        }else{
            button.removeAttribute("disabled");
        }
    }

    public void addClickListener(EventListener eventListener){
        button.addEventListener("click", eventListener);
    }

    public void removeClickListener(EventListener eventListener){
        button.removeEventListener("click", eventListener);
    }

    public void setCaption(String caption){
        textNode.setTextContent(caption);
    }

    public void appendTo(Element element){
        element.appendChild(button);
    }

    public enum ButtonStyle {
        DEFAULT, PRIMARY, SUCCESS, INFO, WARNING, DANGER, LINK
    }

    public enum ButtonSize {
        DEFAULT, LARGE, SMALL, EXTRA_SMALL;

        String getStyle() {
            switch (this) {
                case LARGE:
                    return "btn-lg";
                case SMALL:
                    return "btn-sm";
                case EXTRA_SMALL:
                    return "btn-xs";
                default:
                    return "";
            }
        }
    }
}
