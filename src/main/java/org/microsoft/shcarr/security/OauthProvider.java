// this is jersery v1
//package org.microsoft.shcarr.security;
//import com.sun.jersey.core.spi.component.ComponentContext;
//import com.sun.jersey.core.spi.component.ComponentScope;
//import com.sun.jersey.spi.inject.Injectable;
//import com.sun.jersey.spi.inject.InjectableProvider;
//import com.sun.jersey.api.model.Parameter;
//
//public class OauthProvider implements InjectableProvider<Auth, Parameter> {
//
//    @Override
//    public ComponentScope getScope() {
//        return null;
//    }
//
//    @Override
//    public Injectable<?> getInjectable(ComponentContext componentContext, Auth auth, Parameter parameter) {
//        return new OauthInjectable(auth.required());
//    }
//}
