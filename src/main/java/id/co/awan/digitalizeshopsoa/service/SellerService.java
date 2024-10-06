package id.co.awan.digitalizeshopsoa.service;

import id.co.awan.digitalizeshopsoa.database.first.model.SellerModel;
import id.co.awan.digitalizeshopsoa.database.first.repo.SellerModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerModelRepo sellerModelRepo;

    public SellerModel saveSeller(SellerModel productModel) {
        return sellerModelRepo.save(productModel);
    }

    public SellerModel getSeller(String username) {

        return sellerModelRepo.findById(username)
                .orElse(null);
    }

    public Stream<SellerModel> getSellers(int pageNumber, int size) {

        PageRequest pageRequest = PageRequest.of(pageNumber, size);
        Page<SellerModel> productsPage = sellerModelRepo.findAll(pageRequest);
        return productsPage.get();

    }

    public void deleteSeller(String username) {
        sellerModelRepo.deleteById(username);
    }


}
