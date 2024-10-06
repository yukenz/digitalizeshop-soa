package id.co.awan.digitalizeshopsoa.endpoint.resource;

import https.soa_digitalizeshop_id.ws.seller.ReadSellersRequest;
import https.soa_digitalizeshop_id.ws.seller.ReadSellersResponse;
import https.soa_digitalizeshop_id.ws.seller.SellerEntity;
import id.co.awan.digitalizeshopsoa.config.CachingConfig;
import id.co.awan.digitalizeshopsoa.database.first.model.SellerModel;
import id.co.awan.digitalizeshopsoa.exception.UnauthorizedSoapException;
import id.co.awan.digitalizeshopsoa.service.SellerService;
import id.co.awan.digitalizeshopsoa.util.CryptoUtil;
import id.co.awan.digitalizeshopsoa.util.MessageContextUtil;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.namespace.QName;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@Endpoint
@RequiredArgsConstructor
@Slf4j
public class SellerEndpoint {


    // Never Edit Global
    public static final String NAMESPACE_URI = "https://soa.digitalizeshop.id/ws/seller";
    public static final String PORT = "SellerPort";

    // Never Edit Per Service
    public static final String LOCAL_PART_CREATE = "createSellerRequest";
    public static final String LOCAL_PART_READ = "readSellerRequest";
    public static final String LOCAL_PART_READS = "readSellersRequest";
    public static final String LOCAL_PART_UPDATE = "updateSellerRequest";
    public static final String LOCAL_PART_DELETE = "deleteSellerRequest";

    // CDI
    private final SellerService sellerService;

    /* Create */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_CREATE)
    public JAXBElement<SellerEntity> createSellerRequest(
//            MessageContext messageContext,
            @RequestPayload JAXBElement<SellerEntity> request
    ) throws UnauthorizedSoapException, NoSuchAlgorithmException {

        SellerEntity requestObj = request.getValue();

        // Hashing Password
        String password = requestObj.getPassword();
        requestObj.setPassword(CryptoUtil.sha256ToHexFormat(password));

        SellerModel seller = new SellerModel();
        seller.setUsername(requestObj.getUsername());
        seller.setPassword(requestObj.getPassword());
        seller.setName(requestObj.getName());
        seller.setOwnerName(requestObj.getOwnerName());
        seller.setAddress1(requestObj.getAddress1());
        seller.setAddress2(requestObj.getAddress2());
        seller.setStatus(requestObj.isStatus());
        seller.setImageURI(requestObj.getImageURI());
        seller.setRegistrationDate(requestObj.getRegistrationDate().toGregorianCalendar().getTime());
        seller.setLastLogin(requestObj.getLastLogin().toGregorianCalendar().getTime());

        SellerModel sellerSaved = sellerService.saveSeller(seller);
        requestObj.setUsername(sellerSaved.getUsername());

        return request;
    }


    /* Read One */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_READ)
    public JAXBElement<SellerEntity> readSellerRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<String> request
    ) throws UnauthorizedSoapException, DatatypeConfigurationException {


        String username = request.getValue();
        SellerModel seller = sellerService.getSeller(username);

        if (seller == null) {
            return new JAXBElement<>(
                    new QName(NAMESPACE_URI, "readSellerResponse", "seller"),
                    SellerEntity.class,
                    null);
        }

//        GregorianCalendar.

        SellerEntity response = new SellerEntity();
        response.setUsername(seller.getUsername());
        response.setPassword(seller.getPassword());
        response.setName(seller.getName());
        response.setOwnerName(seller.getOwnerName());
        response.setAddress1(seller.getAddress1());
        response.setAddress2(seller.getAddress2());
        response.setStatus(seller.getStatus());
        response.setImageURI(seller.getImageURI());

        Date registrationDate = seller.getRegistrationDate();
        String registrationDateString = new SimpleDateFormat("yyyy-MM-dd")
                .format(registrationDate);
        response.setRegistrationDate(
                javax.xml.datatype.DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(registrationDateString)
        );

        Date lastLoginDateTime = seller.getRegistrationDate();
        String lastLoginDateTimeString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .format(lastLoginDateTime);
        response.setLastLogin(
                javax.xml.datatype.DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(lastLoginDateTimeString)
        );

        return new JAXBElement<>(
                new QName(NAMESPACE_URI, "readSellerResponse", "seller"),
                SellerEntity.class,
                response);
    }


    /* Read All */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_READS)
    public ReadSellersResponse readSellersRequest(
            MessageContext messageContext,
            @RequestPayload ReadSellersRequest request
    ) throws UnauthorizedSoapException {


        int page = request.getPage();
        int size = request.getSize();
        Stream<SellerModel> sellers = sellerService.getSellers(page, size);

        ReadSellersResponse response = new ReadSellersResponse();
        response.getResponse().addAll(
                sellers.map(seller -> {
                    SellerEntity sellerEntity = new SellerEntity();
                    sellerEntity.setUsername(seller.getUsername());
                    sellerEntity.setPassword(seller.getPassword());
                    sellerEntity.setName(seller.getName());
                    sellerEntity.setOwnerName(seller.getOwnerName());
                    sellerEntity.setAddress1(seller.getAddress1());
                    sellerEntity.setAddress2(seller.getAddress2());
                    sellerEntity.setStatus(seller.getStatus());
                    sellerEntity.setImageURI(seller.getImageURI());

                    try {
                        Date registrationDate = seller.getRegistrationDate();
                        String registrationDateString = new SimpleDateFormat("yyyy-MM-dd")
                                .format(registrationDate);
                        sellerEntity.setRegistrationDate(
                                javax.xml.datatype.DatatypeFactory.newInstance()
                                        .newXMLGregorianCalendar(registrationDateString)
                        );

                        Date lastLoginDateTime = seller.getRegistrationDate();
                        String lastLoginDateTimeString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                .format(lastLoginDateTime);
                        sellerEntity.setLastLogin(
                                javax.xml.datatype.DatatypeFactory.newInstance()
                                        .newXMLGregorianCalendar(lastLoginDateTimeString)
                        );
                    } catch (DatatypeConfigurationException e) {
                        log.error("Error on converting date to XMLGregorianCalendar", e);
                    }

                    return sellerEntity;
                }).toList()
        );
        return response;
    }

    /* Update */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_UPDATE)
    public JAXBElement<SellerEntity> updateSellerRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<SellerEntity> request
    ) throws UnauthorizedSoapException {


        SellerEntity requestObj = request.getValue();

        // Get seller by username
        String username = MessageContextUtil.getPrincipalFromMessageContext(messageContext);
        SellerModel seller = sellerService.getSeller(username);

        // Hashing Password
        String password = requestObj.getPassword();
        requestObj.setPassword(CryptoUtil.sha256ToHexFormat(password));

        seller.setPassword(requestObj.getPassword());
        seller.setName(requestObj.getName());
        seller.setOwnerName(requestObj.getOwnerName());
        seller.setAddress1(requestObj.getAddress1());
        seller.setAddress2(requestObj.getAddress2());
        seller.setStatus(requestObj.isStatus());
        seller.setImageURI(requestObj.getImageURI());
        seller.setRegistrationDate(requestObj.getRegistrationDate().toGregorianCalendar().getTime());
        seller.setLastLogin(requestObj.getLastLogin().toGregorianCalendar().getTime());

        sellerService.saveSeller(seller);

        return request;
    }


    private final CachingConfig cachingConfig;

    /* Delete */
    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = LOCAL_PART_DELETE)
    public JAXBElement<Boolean> deleteSellerRequest(
            MessageContext messageContext,
            @RequestPayload JAXBElement<String> username
    ) throws UnauthorizedSoapException {

        // Clear Cache every delete
        sellerService.deleteSeller(username.getValue());
        cachingConfig.backendEntityCacheEvict();
        return new JAXBElement<>(
                new QName(NAMESPACE_URI, "deleteSellerResponse", "seller"),
                Boolean.class,
                Boolean.TRUE
        );
    }

}
