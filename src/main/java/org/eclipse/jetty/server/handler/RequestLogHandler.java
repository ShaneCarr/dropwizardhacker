//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.server.handler;

import com.google.common.annotations.VisibleForTesting;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.server.AsyncContextState;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.microsoft.shcarr.core.scrubber.PiiScrubber;
import org.microsoft.shcarr.core.scrubber.Url;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * Overwritten to remove of access token in request log
 * <p>
 * See removeAccessTokenFromUri and #PATCH tags
 */


/**
 * RequestLogHandler.
 * This handler can be used to wrap an individual context for context logging.
 *
 * @org.apache.xbean.XBean
 */
public class RequestLogHandler extends HandlerWrapper {
   // private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(org.eclipse.jetty.server.handler.RequestLogHandler.class);

    // FIXME: move the RequestLogHandler class into another package and instantiate in service startup instead of relying on Java magic to load it simply because it is in the same package as DropWizard expects it.
    private static final Logger LOG = Log.getLogger(RequestLogHandler.class);

    // At the same time, take the pii scrubber as an argument
    private final PiiScrubber piiScrubber = new Url();

    private RequestLog requestLog;
    private final AsyncListener listener = new AsyncListener() {

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {

        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
            event.getAsyncContext().addListener(this);
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {

        }

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
            AsyncContextState context = (AsyncContextState) event.getAsyncContext();
            Request request = context.getHttpChannelState().getBaseRequest();
            Response response = request.getResponse();
            LOG.info("does this get logged");
            requestLog.log(request, response);
        }
    };

    /* ------------------------------------------------------------ */
    /*
     * @see org.eclipse.jetty.server.server.Handler#handle(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, int)
     */
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        try {
            LOG.info("RequestLogHandler handle");
            super.handle(target, baseRequest, request, response);
        } finally {
            LOG.info("RequestLogHandler finally");
            if (requestLog != null && baseRequest.getDispatcherType().equals(DispatcherType.REQUEST)) {
                // not sure if we actually have async requesst.
                if (baseRequest.getHttpChannelState().isAsync()) {
                    if (baseRequest.getHttpChannelState().isInitial()) {
                        baseRequest.getAsyncContext().addListener(listener);
                        LOG.info("RequestLogHandler does this get logged-1");
                    }
                } else {
                    // #PATCH
                    // remove access token
                    LOG.info("RequestLogHandler does this get logged-2-1");
                    removeAccessTokenFromUri(baseRequest);
                    // #ENDPATCH
                    LOG.info("RequestLogHandler does this get logged-2-2");
                    // log request
                    requestLog.log(baseRequest, (Response) response);
                }
            }
        }
    }

    /* ------------------------------------------------------------ */
    public RequestLog getRequestLog() {
        return requestLog;
    }

    /* ------------------------------------------------------------ */
    public void setRequestLog(RequestLog requestLog) {
        updateBean(this.requestLog, requestLog);
        this.requestLog = requestLog;
    }

    /* ------------------------------------------------------------ */
    @Override
    protected void doStart() throws Exception {
        if (requestLog == null) {
            LOG.warn("RequestLogHandler !RequestLog");
            requestLog = new NullRequestLog();
        }
        super.doStart();
    }

    /* ------------------------------------------------------------ */
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        if (requestLog instanceof NullRequestLog) {
            requestLog = null;
        }
    }

    // #PATCH
    @VisibleForTesting
    public void removeAccessTokenFromUri(final Request baseRequest) {
        final String originalUriString = baseRequest.getHttpURI().toString();

        final String scrubbedUriString = scrubPii(originalUriString);

        final HttpURI newUri = new HttpURI(scrubbedUriString);
        baseRequest.setHttpURI(newUri);
    }

    private String scrubPii(final String realUrl) {
        try {
            return piiScrubber.scrubPii(realUrl);
        } catch (final Throwable ex) {
            LOG.warn("RequestLogHandler Failed to scrub url, setting to hashcode instead", ex);
            return "RequestLogHandler FAILED_TO_SCRUB/Url_hashcode=" + realUrl.hashCode();
        }
    }

    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    /* ------------------------------------------------------------ */
    private static class NullRequestLog extends AbstractLifeCycle implements RequestLog {
        @Override
        public void log(Request request, Response response) {
        }
    }
    // #ENDPATCH
}
