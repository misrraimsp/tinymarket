package misrraimsp.tinymarket.data;

import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.util.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p.id FROM Product p WHERE p.price >= :lowLimit AND p.price < :highLimit")
    Set<Long> findIdByPrice(@Param("lowLimit") BigDecimal lowLimit, @Param("highLimit") BigDecimal highLimit);

    @Query("SELECT p.id FROM Product p WHERE p.category = :category")
    Set<Long> findIdByCategory(@Param("category") Category category);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findByIds(@Param("ids") Set<Long> ids);

    @Query("SELECT p.id FROM Product p WHERE p.title LIKE :q")
    Set<Long> findIdByTitleLike(@Param("q") String q);

    @Query("SELECT p.id FROM Product p WHERE p.description LIKE :q")
    Set<Long> findIdByDescriptionLike(@Param("q") String q);
}
