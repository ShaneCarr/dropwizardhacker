package org.microsoft.shcarr.resources;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.microsoft.shcarr.HelloWorldApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.codahale.metrics.annotation.Timed;

import org.microsoft.shcarr.api.Saying;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldApplication.class);

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        LOG.info("sayHello");
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }
}