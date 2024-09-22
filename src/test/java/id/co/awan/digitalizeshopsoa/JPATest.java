package id.co.awan.digitalizeshopsoa;

import id.co.awan.digitalizeshopsoa.database.first.domain.SellerFirst;
import id.co.awan.digitalizeshopsoa.database.first.repo.SellerFirstRepo;
import id.co.awan.digitalizeshopsoa.database.second.domain.SellerSecond;
import id.co.awan.digitalizeshopsoa.database.second.repo.SellerSecondRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class JPATest {

    @Autowired
    private SellerFirstRepo sellerFirstRepo;

    @Autowired
    @Qualifier("firstEntityManagerFactory")
    private EntityManager entityManager1;
//
//    @Autowired
//    @Qualifier("secondEntityManagerFactory")
//    private EntityManager entityManager2;

//    @Autowired
//    private SellerSecondRepo sellerSecondRepo;

    @Test
    @Transactional("firstTransactionManager")
    public void first() {

        List<SellerFirst> all = sellerFirstRepo.findAll();



        StoredProcedureQuery query = entityManager1.createStoredProcedureQuery("authSeller");
        query.registerStoredProcedureParameter("usernameIn", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("passwordIn", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("isValid", Boolean.class, ParameterMode.OUT);
        query.setParameter("usernameIn", "yukenz");
        query.setParameter("passwordIn", "awan123");

        query.execute();
        Object isValid1 = query.getOutputParameterValue("isValid");
        System.out.println(isValid1);

        Boolean b = sellerFirstRepo.authSeller("yukenz", "awan123");
        System.out.print(b);


//        StoredProcedureQuery query2 = entityManager2.createStoredProcedureQuery("allSeller");
//
//
//        query.execute();
//        Object singleResult = query2.getSingleResult();
//        System.out.println(singleResult);

    }

//    @Test
//    @Transactional("secondTransactionManager")
//    public void second() {
//
//        List<SellerSecond> all = sellerSecondRepo.findAll();
//        System.out.println(all.stream().findFirst().get().name);
//
//    }

}
