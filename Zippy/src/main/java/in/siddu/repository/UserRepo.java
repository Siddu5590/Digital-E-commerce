package in.siddu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.siddu.entity.Category;
import in.siddu.entity.Product;
import in.siddu.entity.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	
	
	@Query("select u from User u where u.email=:email")
	User findByEmailAndPassword(String email);

	//@Query("select u from User u where u.email=:email")
	boolean existsByEmail(String email);
	
	User findByEmail(String email);
	
	@Modifying
	@Query("DELETE FROM Order o WHERE o.user.id = :userId")
	void deleteByUserId(@Param("userId") Integer userId);

	

	@Query("SELECT p FROM Product p ORDER BY RAND() LIMIT :count")
	List<Product> getRandomProducts(@Param("count") int count);


	
	
}
