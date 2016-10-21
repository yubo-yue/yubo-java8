package yubo.v1.jcip.composingobject;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiedMap;

    public PublishingVehicleTracker(final Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiedMap = Collections.unmodifiableMap(locations);
    }

    public Map<String, SafePoint> getLocations() {
        return unmodifiedMap;
    }

    public SafePoint getLocation(final String id) {
        return locations.get(id);
    }

    public void setLocations(String id, int x, int y) {
        if (locations.containsKey(id)) {
            throw new IllegalStateException();
        }
        this.locations.get(id).set(x, y);
    }
}
