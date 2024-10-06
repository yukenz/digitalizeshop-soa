package id.co.awan.digitalizeshopsoa.database.first.repo;

import id.co.awan.digitalizeshopsoa.database.first.model.ProductModel;
import id.co.awan.digitalizeshopsoa.util.MessageContextUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ws.context.MessageContext;

import java.util.Optional;

public interface ProductModelRepo extends JpaRepository<ProductModel, Integer> {

    /**
     * Search single product by id and seller's username
     *
     * @param id     product id
     * @param seller seller's username, must get from {@link MessageContextUtil#getPrincipalFromMessageContext(MessageContext)}
     * @return Nullable product POJO
     */
    Optional<ProductModel> findByIdAndSeller(Integer id, String seller);

    /**
     * Search all products by seller's username with paginated result
     *
     * @param seller      seller's username, must get from {@link MessageContextUtil#getPrincipalFromMessageContext(MessageContext)}
     * @param pageRequest PageRequest object
     * @return products POJO wrap with Page structure
     */
    Page<ProductModel> findAllBySeller(String seller, PageRequest pageRequest);

    /**
     * Delete product that owned by seller, identified by seller's username and product id
     *
     * @param id     product id
     * @param seller seller's username, must get from {@link MessageContextUtil#getPrincipalFromMessageContext(MessageContext)}
     */
    void deleteByIdAndSeller(Integer id, String seller);

}
