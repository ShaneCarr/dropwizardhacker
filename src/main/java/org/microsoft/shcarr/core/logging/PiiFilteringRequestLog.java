package org.microsoft.shcarr.core.logging;

import io.dropwizard.server.DefaultServerFactory;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.microsoft.shcarr.core.scrubber.PiiScrubber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PiiFilteringRequestLog implements RequestLog {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultServerFactory.class);
    private final RequestLog defaultRequestLog;
    private final PiiScrubber piiScrubber;

    public PiiFilteringRequestLog(final RequestLog defaultRequestLog, final PiiScrubber piiScrubber) {
        this.defaultRequestLog = defaultRequestLog;
        this.piiScrubber = piiScrubber;
    }

    @Override
    public void log(final Request request, final Response response) {
        LOG.info("PiiFilteringRequestLog" + request.getRequestURI().toString());
        scrubPiiFromRequest(request);
        defaultRequestLog.log(request, response);
    }

    private void scrubPiiFromRequest(final Request request) {
        final HttpURI uri = request.getHttpURI();
        final String rawUri = uri.toString();

        final String scrubbedRawUri = piiScrubber.scrubPii(rawUri);
        final HttpURI scrubbedUri = new HttpURI(scrubbedRawUri);

        request.setHttpURI(scrubbedUri);
    }

}
