// this is jersery v1
//package org.microsoft.shcarr.security;
//
//import org.glassfish.jersey.internal.inject.ContextInjectionResolver;
//
//// from this ContextInjectionResolver -< AbstractHttpContextInjectable
//public class OauthInjectable extends ContextInjectionResolver<String> {
//
//    private final boolean required;
//
//    public OauthInjectable(boolean required) {
//        this.required = required;
//    }
//
//    //https://stackoverflow.com/questions/34607608/where-is-this-class-httpcontext-exist-in-glassfish-jersey-2-9?msclkid=b1fb27b5aeeb11ec850e199bcb0ccba5
//    @Override
//    public String getValue() {
//        return "OauthInjectable::getValue: " + this.required;
//    }
//}
