package com.example.ecommerce.repository;

import com.example.ecommerce.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerUnsafeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // DEMO ONLY – Detta är inte en del av inlämningen och kommer stanna i min branch,
    // endast för att jag vill testa och lära mig något nytt
    public Optional<Customer> findByEmailUnsafe(String email) {

        String sql =
                "SELECT * FROM customers WHERE email = '" + email + "'";

        List<Customer> result = entityManager
                .createNativeQuery(sql, Customer.class)
                .getResultList();

        return result.stream().findFirst();
    }
}