package in.siddu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.siddu.entity.Category;
import jakarta.transaction.Transactional;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

	Optional<Category> findByName(String name);
	
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.subcategories")
	List<Category> findAllWithSubcategories();
	
}
