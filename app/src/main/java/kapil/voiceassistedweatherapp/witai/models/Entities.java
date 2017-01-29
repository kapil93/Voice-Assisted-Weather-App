
package kapil.voiceassistedweatherapp.witai.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "intent",
    "location"
})
public class Entities {

    @JsonProperty("intent")
    private List<Intent> intent = null;
    @JsonProperty("location")
    private List<Location> location = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("intent")
    public List<Intent> getIntent() {
        return intent;
    }

    @JsonProperty("intent")
    public void setIntent(List<Intent> intent) {
        this.intent = intent;
    }

    @JsonProperty("location")
    public List<Location> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Location> location) {
        this.location = location;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
