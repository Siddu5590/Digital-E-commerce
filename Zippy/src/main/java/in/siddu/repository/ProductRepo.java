package in.siddu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.siddu.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
	List<Product> findByCategoryId(Integer categoryId);

	@Query("SELECT p FROM Product p WHERE " + "(:subCategoryId IS NULL OR p.psubCategory.id = :subCategoryId) AND "
			+ "p.category.id = :id " + "ORDER BY (p.pprice - p.pdiscount) ASC")
	
	List<Product> findByFiltersOrderByPriceAsc(@Param("subCategoryId") Integer subCategoryId, @Param("id") Integer id);

	
	@Query("SELECT p FROM Product p WHERE " + "(:subCategoryId IS NULL OR p.psubCategory.id = :subCategoryId) AND "
			+ "p.category.id = :id " + "ORDER BY (p.pprice - p.pdiscount) DESC")
	
	List<Product> findByFiltersOrderByPriceDesc(@Param("subCategoryId") Integer subCategoryId, @Param("id") Integer id);

	@Query("SELECT p FROM Product p WHERE " + "(:subCategoryId IS NULL OR p.psubCategory.id = :subCategoryId) AND "
			+ "p.category.id = :id")
	
	List<Product> findByFilters(@Param("subCategoryId") Integer subCategoryId, @Param("id") Integer id);

}
