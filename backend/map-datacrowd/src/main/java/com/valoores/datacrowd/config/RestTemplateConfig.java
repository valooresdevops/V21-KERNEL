//package com.valoores.datacrowd.config;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.security.KeyStore;
//
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//@Configuration
//public class RestTemplateConfig {
//
//    @Bean
//    public RestTemplate restTemplate() throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("JKS");
//        try (FileInputStream inputStream = new FileInputStream("classpath:keystore.jks")) {
//            keyStore.load(inputStream, "valoores".toCharArray());
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load keystore", e);
//        }
//        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        ((HttpComponentsClientHttpRequestFactory) requestFactory).setHttpClient(
//                HttpClientBuilder.create()
//                        .setSSLContext(SSLContextBuilder.create()
//                                .loadKeyMaterial(keyStore, "valoores".toCharArray())
//                                .build())
//                        .build()
//        );
//        return new RestTemplate(requestFactory);
//    }
//}