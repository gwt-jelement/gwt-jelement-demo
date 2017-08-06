package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.notifications.Notification;
import gwt.jelement.notifications.NotificationOptions;

public class NotificationDemo extends AbstractDemo {
    @Override
    protected void execute() {
        if ("granted".equals(Notification.getPermission().getInternalValue())) {
            NotificationOptions notificationOptions = new NotificationOptions();
            notificationOptions.setBody("I hope you are enjoying the demos.");
            notificationOptions.setIcon("gwtlogo.png");
            notificationOptions.setImage("code.png");
            notificationOptions.setRequireInteraction(true);
            new Notification("Hello", notificationOptions);
        }
    }

    @Override
    protected String getName() {
        return "notification";
    }

    @Override
    protected String getTitle() {
        return "Notification Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getNotificationHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getNotificationSource();
    }
}
