package gwt.jelement.demo.client;

import com.google.gwt.resources.client.TextResource;
import gwt.jelement.core.*;
import gwt.jelement.demo.client.html.HtmlClientBundle;
import gwt.jelement.html.HTMLDivElement;
import gwt.jelement.timing.MemoryInfo;
import gwt.jelement.timing.Performance;
import gwt.jelement.timing.PerformanceNavigation;
import gwt.jelement.timing.PerformanceTiming;

import java.util.*;

import static gwt.jelement.Browser.document;
import static gwt.jelement.Browser.window;

public class PerformanceDemo extends AbstractDemo {
    @Override
    protected void execute() {
        if (!window.object().has("performance")) {
            window.alert("The Performance API is not supported in your browser.");
            return;
        }

        ie11Polyfill();

        Performance performance = window.getPerformance();
        showPerformanceTiming(performance.getTiming());
        showNavigationInfo(performance.getNavigation());

        HTMLDivElement memoryInfoDiv = document.getElementById("memoryInfo");
        if (performance.getMemory() != null) { /*Chrome only*/
            showMemoryInfo(performance.getMemory(), memoryInfoDiv);
        } else {
            /* for IE 11, otherwise memoryInfoDiv.remove() would have sufficed */
            memoryInfoDiv.getParentElement().removeChild(memoryInfoDiv);
        }
    }

    private void showPerformanceTiming(PerformanceTiming timing) {
        Map<String, Double> timingMap = new HashMap<>();
        JsObject<ObjectPropertyDescriptor> propertyDescriptors =
                JsObject.getOwnPropertyDescriptors(timing.object().get__proto__());
        for (Object[] entry : JsObject.entries(propertyDescriptors)) {
            String propertyName = (String) entry[0];
            ObjectPropertyDescriptor descriptor = (ObjectPropertyDescriptor) entry[1];
            if (descriptor.isEnumerable() && descriptor.getGet() != null
                    && !propertyName.startsWith("unload")) {
                Function getter = (Function) descriptor.getGet();
                double timeElapsed = (double) getter.call(timing);
                if (timeElapsed != 0) {
                    timingMap.put(propertyName, timeElapsed);
                }
            }
        }
        HTMLDivElement timingDiv = document.querySelector("#demoContainer #timing");
        List<Map.Entry<String, Double>> entries = new ArrayList<>(timingMap.entrySet());
        if (!entries.isEmpty()) {
            entries.sort(Comparator.comparing(Map.Entry::getValue));
            double firstTime = entries.get(0).getValue();
            entries.forEach(entry -> {
                        timingDiv.appendChild(createDivWithText(entry.getKey()));
                        timingDiv.appendChild(createDivWithText(String.valueOf(entry.getValue() - firstTime)));
                    }
            );
        }
    }

    private void showNavigationInfo(PerformanceNavigation navigation) {
        HTMLDivElement navigationInfoDiv = document.getElementById("navigationInfo");
        String navigationType = "Unknown";
        /*FIXME could not use switch because of missing values */
        if (navigation.getType() == PerformanceNavigation.TYPE_NAVIGATE) {
            navigationType = "Navigated to page";
        } else if (navigation.getType() == PerformanceNavigation.TYPE_RELOAD) {
            navigationType = "Reloaded the page";
        } else if (navigation.getType() == PerformanceNavigation.TYPE_BACK_FORWARD) {
            navigationType = "Used back or forward to access page";
        }
        navigationInfoDiv.appendChild(createDivWithText("<b>Navigation type:</b> " + navigationType));
        navigationInfoDiv.appendChild(createDivWithText("<b>Redirect count:</b> " + navigation.getRedirectCount()));
    }

    private void showMemoryInfo(MemoryInfo memory, HTMLDivElement container) {
        container.append(createDivWithText("<b>Used JS heap size:</b> "
                + format(memory.getUsedJSHeapSize()) + " bytes"));
        container.append(createDivWithText("<b>Total JS heap size:</b> "
                + format(memory.getTotalJSHeapSize()) + " bytes"));
        container.append(createDivWithText("<b>Max JS heap size:</b> "
                + format(memory.getJsHeapSizeLimit()) + " bytes"));
    }

    /* fixme need Number class */
    private String format(double number) {
        return new JsObject(Any.of(number)).toLocaleString();
    }

    /*
        getOwnPropertyDescriptors and entries polyfill for IE11
     */
    @SuppressWarnings({"unchecked", "Convert2Lambda"})
    private void ie11Polyfill() {
        if (!Js.has(JsObject.prototype.getConstructor().object(), "getOwnPropertyDescriptors")) {
            JsObject.prototype.set("getOwnPropertyDescriptors", new CallbackFunction() {
                @Override
                public Object onInvoked(Object... objects) {
                    JsObject objectPrototype = (JsObject) objects[0];
                    JsObject<ObjectPropertyDescriptor> result = new JsObject<>();
                    for (String key : JsObject.getOwnPropertyNames(objectPrototype)) {
                        result.set(key, JsObject.getOwnPropertyDescriptor(objectPrototype, key));
                    }
                    return result;
                }
            });
        }
        if (!Js.has(JsObject.prototype.getConstructor().object(), "entries")) {
            JsObject.prototype.set("entries",
                    new Function("obj",
                            "return Object.keys(obj).reduce(function(e, k){" +
                                    "return e.concat(typeof k === 'string' && obj.propertyIsEnumerable(k)?" +
                                    "[[k, obj[k]]] : []);}, []);"));
        }
        /* One simple alternative using Java only
         if (!Js.has(JsObject.prototype.getConstructor().object(), "entries")) {
            JsObject.prototype.set("entries", new CallbackFunction() {
                @Override
                public Object onInvoked(Object... objects) {
                    JsObject object = (JsObject) objects[0];
                    Array<Array> result = new Array();
                    for (Object key : JsObject.keys(object)) {
                    if (Js.isTypeName(key, "String") &&
                            object.propertyIsEnumerable((String) key)) {
                        Array entry = new Array();
                        entry.push(key);
                        entry.push(object.get((String) key));
                        result.push(entry);
                    }
                }
                return result;
            }
        });
        */
    }

    private HTMLDivElement createDivWithText(String text) {
        HTMLDivElement result = document.createElement("div");
        result.setInnerHTML(text);
        return result;
    }

    @Override
    protected String getName() {
        return "performance";
    }

    @Override
    protected String getTitle() {
        return "Performance API Demo";
    }

    @Override
    protected TextResource getTemplate() {
        return HtmlClientBundle.INSTANCE.getPerformanceHtml();
    }

    @Override
    protected TextResource getSource() {
        return SourceClientBundle.INSTANCE.getPerformanceSource();
    }
}
