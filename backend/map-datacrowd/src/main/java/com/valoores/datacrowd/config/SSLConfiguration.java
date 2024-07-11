package com.valoores.datacrowd.config;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSLConfiguration {

//    private final Resource trustStore; // the trust store file
//    private final String trustStorePassword; // the trust store password
//
//    public SSLConfiguration(@Value("${server.ssl.trust-store}") Resource trustStore,
//                           @Value("${server.ssl.trust-store-password}") String trustStorePassword) {
//        this.trustStore = trustStore;
//        this.trustStorePassword = trustStorePassword;
//    }
//
//    @Bean
//    public RestTemplate restTemplate() throws Exception {
//    	
//    	  TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//
//          SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
//                  .loadTrustMaterial(null, acceptingTrustStrategy)
//                  .build();
//
//          SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
//
//          CloseableHttpClient httpClient = HttpClients.custom()
//                  .setSSLSocketFactory(csf)
//                  .build();
//
//          HttpComponentsClientHttpRequestFactory requestFactory =
//                  new HttpComponentsClientHttpRequestFactory();
//
//          requestFactory.setHttpClient(httpClient);
//          RestTemplate restTemplate = new RestTemplate(requestFactory);
//          return restTemplate;
//    	
////        SSLContext sslContext = new SSLContextBuilder()
////                .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
////                .build();
////        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
////        HttpClient httpClient = HttpClients.custom()
////                .setSSLSocketFactory(socketFactory)
////                .build();
////        HttpComponentsClientHttpRequestFactory factory =
////                new HttpComponentsClientHttpRequestFactory(httpClient);
////        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public RestTemplateBuilder restTemplateBuilder() {
//        return new RestTemplateBuilder().requestFactory(this::clientHttpRequestFactory);
//    }
//
//    private ClientHttpRequestFactory clientHttpRequestFactory() {
//        try {
//            SSLContext sslContext = new SSLContextBuilder()
//                    .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
//                    .build();
//            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
//            HttpClient httpClient = HttpClients.custom()
//                    .setSSLSocketFactory(socketFactory)
//                    .build();
//            HttpComponentsClientHttpRequestFactory factory =
//                    new HttpComponentsClientHttpRequestFactory(httpClient);
//            return factory;
//        } catch (Exception e) {
//            return new SimpleClientHttpRequestFactory();
//        }
//    }
    
	
//	  @Bean
//	  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	    return http
//	      .requiresChannel(channel -> 
//	          channel.anyRequest().requiresSecure())
//	      .authorizeRequests(authorize ->
//	          authorize.anyRequest().permitAll())
//	      .build();
//	    }
    
    
//	  @Bean
//	  public ServletWebServerFactory servletContainer() {
//	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//	      @Override
//	      protected void postProcessContext(Context context) {
//	        var securityConstraint = new SecurityConstraint();
//	        securityConstraint.setUserConstraint("CONFIDENTIAL");
//	        var collection = new SecurityCollection();
//	        collection.addPattern("/*");
//	        securityConstraint.addCollection(collection);
//	        context.addConstraint(securityConstraint);
//	      }
//	    };
//	    tomcat.addAdditionalTomcatConnectors(getHttpConnector());
//	    return tomcat;
//	  }
//
//	  private Connector getHttpConnector() {
//	    var connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//	    connector.setScheme("http");
//	    connector.setPort(8111);
//	    connector.setSecure(false);
//	    connector.setRedirectPort(8088);
//	    return connector;
//	  }

}
