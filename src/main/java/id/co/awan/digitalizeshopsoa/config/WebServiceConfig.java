package id.co.awan.digitalizeshopsoa.config;

import id.co.awan.digitalizeshopsoa.endpoint.resource.ProductEndpoint;
import id.co.awan.digitalizeshopsoa.endpoint.resource.SellerEndpoint;
import id.co.awan.digitalizeshopsoa.endpoint.security.JWTEndpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

@EnableWs
@Configuration
@RequiredArgsConstructor
public class WebServiceConfig extends WsConfigurerAdapter {

    private final String LOCATION_URI = "/ws";

    // Schema Section
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "countries")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountriesPort");
        wsdl11Definition.setLocationUri(LOCATION_URI);
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Bean(name = "jwt")
    public DefaultWsdl11Definition getJWTWsdl11Definition(XsdSchema jwtSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(JWTEndpoint.PORT);
        wsdl11Definition.setLocationUri(LOCATION_URI);
        wsdl11Definition.setTargetNamespace(JWTEndpoint.NAMESPACE_URI);
        wsdl11Definition.setSchema(jwtSchema);
        return wsdl11Definition;
    }

    @Bean(name = "product")
    public DefaultWsdl11Definition productWsdl11Definition(XsdSchema productSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(ProductEndpoint.PORT);
        wsdl11Definition.setLocationUri(LOCATION_URI);
        wsdl11Definition.setTargetNamespace(ProductEndpoint.NAMESPACE_URI);
        wsdl11Definition.setSchema(productSchema);
        return wsdl11Definition;
    }

    @Bean(name = "seller")
    public DefaultWsdl11Definition sellerWsdl11Definition(XsdSchema sellerSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(SellerEndpoint.PORT);
        wsdl11Definition.setLocationUri(LOCATION_URI);
        wsdl11Definition.setTargetNamespace(SellerEndpoint.NAMESPACE_URI);
        wsdl11Definition.setSchema(sellerSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/countries.xsd"));
    }

    @Bean
    public XsdSchema jwtSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/jwt.xsd"));
    }

    @Bean
    public XsdSchema productSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/product.xsd"));
    }

    @Bean
    public XsdSchema sellerSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/seller.xsd"));
    }


    // Interceptor Section

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        super.addInterceptors(interceptors);
//        interceptors.add(new JWTEndpointInterceptor());
    }

}