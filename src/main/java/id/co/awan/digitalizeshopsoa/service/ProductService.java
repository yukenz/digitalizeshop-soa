package id.co.awan.digitalizeshopsoa.service;

import id.co.awan.digitalizeshopsoa.database.first.model.ProductModel;
import id.co.awan.digitalizeshopsoa.database.first.repo.ProductModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductModelRepo productModelRepo;

    public ProductModel saveProduct(ProductModel productModel) {
        return productModelRepo.save(productModel);
    }

    public ProductModel getProduct(Integer id) {

        return productModelRepo.findById(id)
                .orElse(null);
    }

    public Stream<ProductModel> getProducts(int pageNumber, int size) {

        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Page<ProductModel> productsPage = productModelRepo.findAll(pageRequest);
        return productsPage.get();

    }

    public void deleteProduct(int id) {
        productModelRepo.deleteById(id);
    }


}
