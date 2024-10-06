package id.co.awan.digitalizeshopsoa.endpoint.resource;

import https.soa_digitalizeshop_id.ws.product.ProductEntity;
import https.soa_digitalizeshop_id.ws.product.ReadProductsRequest;
import https.soa_digitalizeshop_id.ws.product.ReadProductsResponse;
import id.co.awan.digitalizeshopsoa.database.first.model.ProductModel;
import id.co.awan.digitalizeshopsoa.database.first.repo.ProductModelRepo;
import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import id.co.awan.digitalizeshopsoa.service.ProductService;
import id.co.awan.digitalizeshopsoa.util.MessageContextUtil;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.namespace.QName;
import java.util.stream.Stream;

@Endpoint
@RequiredArgsConstructor
public class ProductEndpoint {


    // Never Edit Global
    public static final String NAMESPACE_URI = "https://soa.digitalizeshop.id/ws/product";
    public static final String PORT = "ProductPort";

    // Never Edit Per Service
    public static final String LOCAL_PART_CREATE = "createProductRequest";
    public static final String LOCAL_PART_READ = "readProductRequest";
    public static final String LOCAL_PART_READS = "readProductsRequest";
    public static final String LOCAL_PART_UPDATE = "updateProductRequest";
    public static final String LOCAL_PART_DELETE = "deleteProductRequest";

    // CDI
    private final ProductService productService;

    /* Create */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_CREATE)
    public JAXBElement<ProductEntity> createProductRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<ProductEntity> request
    ) throws UnauthorizedSoapException {

        ProductEntity requestObj = request.getValue();
        String sellerUsername = MessageContextUtil.getPrincipalFromMessageContext(messageContext);
        requestObj.setSeller(sellerUsername);

        // Create Entity
        ProductModel product = new ProductModel();
//        product.setId(requestObj.getId());
        product.setSeller(requestObj.getSeller());
        product.setName(requestObj.getName());
        product.setPrice(requestObj.getPrice());
        product.setDescription(requestObj.getDescription());
        product.setImageUri(requestObj.getImageUri());
        product.setDiscount(requestObj.getDiscount());
        product.setCategory(requestObj.getCategory());
        product.setAvailable(requestObj.isAvailable());

        // Save Entity
        ProductModel savedPrduct = productService.saveProduct(product);

        requestObj.setId(savedPrduct.getId());

        return request;
    }


    private final ProductModelRepo productModelRepo;

    /* Read One */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_READ)
    public JAXBElement<ProductEntity> readProductRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<Integer> request
    ) throws UnauthorizedSoapException {


        String sellerUsername = MessageContextUtil.getPrincipalFromMessageContext(messageContext);
        Integer id = request.getValue();
        ProductModel product = productService.getSellerProduct(id, sellerUsername);

        // Finalize Response
        ProductEntity response = new ProductEntity();

        if (product == null) {
            return new JAXBElement<>(
                    new QName(NAMESPACE_URI, "readProductResponse", "prod"),
                    ProductEntity.class,
                    null);
        }

        // Finalize Response
        response.setId(product.getId());
        response.setSeller(product.getSeller());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setImageUri(product.getImageUri());
        response.setDiscount(product.getDiscount());
        response.setCategory(product.getCategory());
        response.setAvailable(product.isAvailable());

        return new JAXBElement<>(
                new QName(NAMESPACE_URI, "readProductResponse", "prod"),
                ProductEntity.class,
                response);
    }


    /* Read All */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_READS)
    public ReadProductsResponse readProductsRequest(
            MessageContext messageContext,
            @RequestPayload ReadProductsRequest request
    ) throws UnauthorizedSoapException {


        String sellerUsername = MessageContextUtil.getPrincipalFromMessageContext(messageContext);
        int page = request.getPage();
        int size = request.getSize();
        Stream<ProductModel> products = productService.getSellerProducts(page, size, sellerUsername);

        ReadProductsResponse response = new ReadProductsResponse();
        response.getResponse().addAll(
                products.map(product -> {
                    ProductEntity productEntity = new ProductEntity();
                    productEntity.setId(product.getId());
                    productEntity.setSeller(product.getSeller());
                    productEntity.setName(product.getName());
                    productEntity.setPrice(product.getPrice());
                    productEntity.setDescription(product.getDescription());
                    productEntity.setImageUri(product.getImageUri());
                    productEntity.setDiscount(product.getDiscount());
                    productEntity.setCategory(product.getCategory());
                    productEntity.setAvailable(product.isAvailable());
                    return productEntity;
                }).toList()
        );

        return response;
    }

    /* Update */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_UPDATE)
    public JAXBElement<ProductEntity> updateProductRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<ProductEntity> request
    ) throws UnauthorizedSoapException {


        ProductEntity requestObj = request.getValue();
        String sellerUsername = MessageContextUtil.getPrincipalFromMessageContext(messageContext);
        requestObj.setSeller(sellerUsername);

        int id = requestObj.getId();

        ProductModel product = productService.getSellerProduct(id, sellerUsername);
        product.setId(requestObj.getId());
        // product.setSeller(requestObj.getSeller()); Seller is not updatable
        product.setName(requestObj.getName());
        product.setPrice(requestObj.getPrice());
        product.setDescription(requestObj.getDescription());
        product.setImageUri(requestObj.getImageUri());
        product.setDiscount(requestObj.getDiscount());
        product.setCategory(requestObj.getCategory());
        product.setAvailable(requestObj.isAvailable());

        productService.saveProduct(product);
        return request;
    }


    /* Delete */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_DELETE)
    public JAXBElement<Boolean> deleteProductRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<Integer> request
    ) throws UnauthorizedSoapException {

        String sellerUsername = MessageContextUtil.getPrincipalFromMessageContext(messageContext);

        productService.deleteSellerProduct(request.getValue(), sellerUsername);
        return new JAXBElement<>(
                new QName(NAMESPACE_URI, "deleteProductResponse", "prod"),
                Boolean.class,
                Boolean.TRUE
        );
    }

}
