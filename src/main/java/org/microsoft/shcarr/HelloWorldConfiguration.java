package org.microsoft.shcarr;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.microsoft.shcarr.core.logging.ServerFactory;

import javax.validation.constraints.NotEmpty;

public class HelloWorldConfiguration extends Configuration {
    HelloWorldConfiguration(){
        setServerFactory(new ServerFactory());
    }

    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
}
