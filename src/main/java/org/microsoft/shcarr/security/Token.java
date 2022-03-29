package org.microsoft.shcarr.security;
/*
org.glassfish.hk2.api.Factory - Creates injectable objects/services
org.glassfish.hk2.api.InjectionResolver - Used to create injection points for your own annotations.
org.glassfish.jersey.server.spi.internal.ValueFactoryProvider - To provide parameter value injections.

 */
// let's say this is a fake token, the injectible object that match up with Auth.
public class Token {
    private final String token;
    public Token(String token) { this.token = token; }
    public String getToken() { return token; }
}