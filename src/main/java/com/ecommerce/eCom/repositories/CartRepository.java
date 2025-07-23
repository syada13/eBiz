package com.ecommerce.eCom.repositories;

import com.ecommerce.eCom.model.Cart;
import com.ecommerce.eCom.payload.CartDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email =?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email=?1 AND c.id=?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);

}
