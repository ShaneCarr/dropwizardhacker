package org.microsoft.shcarr.core.logging;

import com.fasterxml.jackson.annotation.JsonTypeName;


import io.dropwizard.request.logging.RequestLogFactory;
import io.dropwizard.server.DefaultServerFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import java.util.Optional;

import org.microsoft.shcarr.core.scrubber.PiiScrubber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonTypeName("yammer_dropwizard_server_factory")
public class ServerFactory extends DefaultServerFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ServerFactory.class);
    private Optional<PiiScrubber> piiScrubber = Optional.empty();

    public void setPiiScrubber(final PiiScrubber piiScrubber) {
        LOG.info("ServerFactory.setPiiScrubber");
        this.piiScrubber = Optional.ofNullable(piiScrubber);
    }

    @Override
    protected Handler addRequestLog(final Server server, final Handler handler, final String name) {
        LOG.info("ServerFactory.addRequestLog_test");
        if (!piiScrubber.isPresent()) {
            LOG.info("!ServerFactory.piiScrubber.isPresent()");
            return super.addRequestLog(server, handler, name);
        }

        // Use a scrubbing log handler.
        // The logic is taken from DefaultServerFactory and amended to use a custom
        final RequestLogFactory<?> requestLogFactory = super.getRequestLogFactory();

        final RequestLogHandler requestLogHandler = new RequestLogHandler();

        final RequestLog requestLog = buildRequestLog(name, requestLogFactory, piiScrubber.get());
        LOG.info("!ServerFactory.requestLogHandler.setRequestLog");
        requestLogHandler.setRequestLog(requestLog);
        // server should own the request log's lifecycle since it's already started,
        // the handler might not become managed in case of an error which would leave
        // the request log stranded
        server.addBean(requestLogHandler.getRequestLog(), true);
        requestLogHandler.setHandler(handler);
        LOG.info("!ServerFactory.requestLogHandler.return()");
        return requestLogHandler;
    }

    private RequestLog buildRequestLog(final String name, final RequestLogFactory requestLogFactory, final PiiScrubber piiScrubber) {
        LOG.info("!ServerFactory.buildRequestLog");
        final RequestLog defaultRequestLog = requestLogFactory.build(name);
        return new PiiFilteringRequestLog(defaultRequestLog, piiScrubber);
    }
}
