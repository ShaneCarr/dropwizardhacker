//package org.microsoft.shcarr.security;
//
//import javax.inject.Inject;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.Response;
//import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
//
//public class TokenFactory extends AbstractContainerRequestValueFactory<Token> {
//
//    private final TokenAuthenticator tokenAuthenticator;
//
//    @Inject
//    public TokenFactory(TokenAuthenticator tokenAuthenticator) {
//        this.tokenAuthenticator = tokenAuthenticator;
//    }
//
//    @Override
//    public Token provide() {
//        String auth = getContainerRequest().getHeaderString(HttpHeaders.AUTHORIZATION);
//        try {
//            if (tokenAuthenticator.authenticate(auth).get() == null) {
//                throw new WebApplicationException(Response.Status.FORBIDDEN);
//            }
//        } catch (AuthenticationException ex) {
//            Logger.getLogger(TokenFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return new Token("New Token");
//    }
//}