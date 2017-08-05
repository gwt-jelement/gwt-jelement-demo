package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.battery.BatteryManager;
import gwt.jelement.core.Math;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.events.EventHandlerNonNull;
import gwt.jelement.html.HTMLDivElement;
import jsinterop.base.Any;

import static gwt.jelement.Browser.document;
import static gwt.jelement.Browser.navigator;

public class BatteryDemo extends AbstractDemo {

    private BatteryManager batteryManager;

    @Override
    protected void execute() {
        if (navigator.object().has("getBattery")) {
            navigator.getBattery().then(this::setup);
        } else {
            HTMLDivElement statusDiv = document.querySelector("div.status");
            statusDiv.setInnerHTML("Not supported in this browser");
            statusDiv.getClassList().add("error");
        }
    }

    private Any setup(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
        EventHandlerNonNull updateEventHandler = event -> {
            updateStatus(batteryManager);
            return null;
        };
        batteryManager.setOnChargingchange(updateEventHandler);
        batteryManager.setOnChargingtimechange(updateEventHandler);
        batteryManager.setOnDischargingtimechange(updateEventHandler);
        batteryManager.setOnLevelchange(updateEventHandler);
        updateStatus(batteryManager);
        return null;
    }

    private void updateStatus(BatteryManager batteryManager) {
        if (isDemoActive()) {
            HTMLDivElement batteryInner = document.querySelector(".battery-inner");
            String statusString;
            double level = Math.trunc(100 * batteryManager.getLevel());
            if (batteryManager.getCharging()) {
                statusString = "Plugged in, " + level + "%<br>" + toTimString(batteryManager.getChargingTime());
            } else {
                statusString = ("On Battery, " + level +
                        "%<br>" + toTimString(batteryManager.getDischargingTime()));
            }
            batteryInner.getStyle().setProperty("width", level + "%");
            batteryInner.getClassList().remove("high", "medium", "low");
            batteryInner.getClassList().add(level > 66 ? "high" : level > 33 ? "medium" : "low");
            document.querySelector("div.status").setInnerHTML(statusString);
        }
    }

    private String toTimString(double time) {
        if (Double.isInfinite(time)) {
            return "time left: not available";
        }
        if (time == 0) {
            return "&nbsp;";
        }
        String result = "";
        double hours = Math.trunc(time / 3600d);
        if (hours != 0) {
            result += hours + " hr ";
        }
        double minutes = Math.trunc((time - hours * 3600) / 60);
        if (minutes != 0) {
            result += minutes + " min";
        }
        return "time left: " + result;
    }

    @Override
    protected void setInactive() {
        batteryManager.setOnDischargingtimechange(null);
        batteryManager.setOnLevelchange(null);
        batteryManager.setOnChargingtimechange(null);
        batteryManager.setOnChargingchange(null);
        super.setInactive();
    }

    @Override
    protected String getName() {
        return "battery";
    }

    @Override
    protected String getTitle() {
        return "Battery Status Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getBatteryHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getBatterySource();
    }
}
