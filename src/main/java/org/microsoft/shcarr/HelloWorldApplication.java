package org.microsoft.shcarr;

import io.dropwizard.ConfiguredBundle;
import org.microsoft.shcarr.core.logging.ServerFactory;
import org.microsoft.shcarr.core.scrubber.Url;
import org.microsoft.shcarr.resources.HelloWorldResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldApplication.class);
    public static void main(final String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "HelloWorld";
    }

    @Override
    public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addBundle(new ConfiguredBundle<HelloWorldConfiguration>() {

            @Override
            public void initialize(Bootstrap<?> bootstrap) {
                // NOP
            }

            @Override
            public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
                //try{
                final Url urlPiiScrubber = new Url();
                LOG.info("HelloWorldConfiguration.run.start");
                final io.dropwizard.server.ServerFactory serverFactory = configuration.getServerFactory();
                if (serverFactory instanceof ServerFactory) {
                    LOG.info("HelloWorldConfiguration.setPiiScrubberconfigured");
                    org.microsoft.shcarr.core.logging.ServerFactory yammerServerFactory = (ServerFactory) serverFactory;
                    yammerServerFactory.setPiiScrubber(urlPiiScrubber);
                }

                 LOG.info("HelloWorldConfiguration.run.done");
            //    registerLogging(environment);
//                }catch(Exception e){
//                    LOG.error(e.toString());
//                }
            }
        });
    }

    @Override
    public void run(final HelloWorldConfiguration configuration,
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        environment.jersey().register(resource);
    }

}
