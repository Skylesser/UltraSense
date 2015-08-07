package jakobkarolus.de.pulseradar.features.activities;

import java.util.List;

import jakobkarolus.de.pulseradar.features.Feature;

/**
 * Interface for each ActivityExtractor. Given a list of Feature each subclass should decide whether to
 * extract an activity or not and modify the list accordingly.
 *
 * <br><br>
 * Created by Jakob on 04.08.2015.
 */
public abstract class ActivityExtractor {


    private InferredContextCallback callback;
    private InferredContext currentContext;


    public ActivityExtractor(InferredContextCallback callback) {
        this.callback = callback;
        this.currentContext = InferredContext.UNKNOWN;
    }

    public InferredContextCallback getCallback() {
        return callback;
    }

    /**
     * Gets called by an ActivityFP everytime a new feature is detected.<br>
     *
     * @param feature the newest feature that was detected
     * @return true if the feature has been consumed; otherwise false
     */
    public abstract boolean processNewFeature(Feature feature);

    /**
     * Detects activities based on the current features.<br>
     * Contrary to the GestureFP, this method is called in regular time intervals to enable time sensitive context.<br>
     * ActivityExtractor should modify the list upon detecting an activity!
     *
     * @param features the current feature list
     */
    public abstract void processFeatureList(List<Feature> features);

    /**
     *
     * @return the current InferredContext based on previously seen features
     */
    public InferredContext getCurrentContext(){
        return this.currentContext;
    }

    /**
     * changes the internal state to the new context
     * @param newContext the new context
     * @param reason possibility to state a reason why the context was changed
     * @return whether the context was changed (in other words is it is different that the old one)
     */
    protected boolean changeContext(InferredContext newContext, String reason){
        InferredContext oldContext = getCurrentContext();
        this.currentContext = newContext;
        getCallback().onInferredContextChange(oldContext, newContext, reason);
        return oldContext != newContext;
    }
}
