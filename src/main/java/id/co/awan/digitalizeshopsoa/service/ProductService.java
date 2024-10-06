package id.co.awan.digitalizeshopsoa.service;

import id.co.awan.digitalizeshopsoa.database.first.model.ProductModel;
import id.co.awan.digitalizeshopsoa.database.first.repo.ProductModelRepo;
import id.co.awan.digitalizeshopsoa.util.MessageContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.context.MessageContext;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductModelRepo productModelRepo;

    /**
     * Save or Update product
     *
     * @param productModel Product POJO
     * @return Product POJO
     */
    @Transactional(transactionManager = "firstTransactionManager")
    public ProductModel saveProduct(ProductModel productModel) {
        return productModelRepo.save(productModel);
    }

    @Deprecated
    public ProductModel getProduct(Integer id) {

        return productModelRepo.findById(id)
                .orElse(null);
    }


    /**
     * Search all products by seller's username with paginated result
     *
     * @param pageNumber Number of page
     * @param size       Size of page
     * @param seller     seller's username, must get from {@link MessageContextUtil#getPrincipalFromMessageContext(MessageContext)}
     * @return Stream of products POJO for mapping to SOAP DTO Entity
     */
    public Stream<ProductModel> getSellerProducts(int pageNumber, int size, String seller) {
        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Page<ProductModel> productsPage = productModelRepo.findAllBySeller(seller, pageRequest);
        return productsPage.get();

    }

    /**
     * Search single product by id and seller's username
     *
     * @param id     product id
     * @param seller seller's username, must get from {@link MessageContextUtil#getPrincipalFromMessageContext(MessageContext)}
     * @return Nullable product POJO
     */
    @Nullable
    public ProductModel getSellerProduct(Integer id, String seller) {
        return productModelRepo.findByIdAndSeller(id, seller)
                .orElse(null);
    }

    @Deprecated
    public Stream<ProductModel> getProducts(int pageNumber, int size) {

        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Page<ProductModel> productsPage = productModelRepo.findAll(pageRequest);
        return productsPage.get();

    }

    @Transactional(transactionManager = "firstTransactionManager")
    @Deprecated
    public void deleteProduct(int id) {
        productModelRepo.deleteById(id);
    }

    /**
     * Delete product that owned by seller, identified by seller's username and product id
     *
     * @param id     product id
     * @param seller seller's username, must get from {@link MessageContextUtil#getPrincipalFromMessageContext(MessageContext)}
     */
    @Transactional(transactionManager = "firstTransactionManager")
    public void deleteSellerProduct(int id, String seller) {
        productModelRepo.deleteByIdAndSeller(id, seller);
    }


}
