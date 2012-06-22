package com.bsbnb.creditregistry.client;

// BEWARE: Do not import anything from or.eclispe.swt in your main class!!!

import com.bsbnb.creditregistry.client.language.LanguageResources;
import com.bsbnb.creditregistry.client.util.LocaleUtil;
import com.bsbnb.creditregistry.client.util.MimeTypesImpl;
import com.bsbnb.creditregistry.client.util.MimeTypesUtil;
import org.apache.log4j.BasicConfigurator;



public class CreditRegistryClient {

    public static void main(final String[] args) {
        // add the correct swt jar to the classpath
        final MultiPlatformSwtHelper multiPlatformSwtHelper = new MultiPlatformSwtHelper();
        multiPlatformSwtHelper.addSwtPlatformDependentJarURLToSystemClassLoader();

        // run your own main or whatever you want
        MimeTypesUtil.setMimeTypes(new MimeTypesImpl());
        
        LocaleUtil.setDefault("ru", "RU", "");
        LanguageResources.setConfig("content.Language");
        
        BasicConfigurator.configure();
        
        Bootstrap.load();
    }
}
