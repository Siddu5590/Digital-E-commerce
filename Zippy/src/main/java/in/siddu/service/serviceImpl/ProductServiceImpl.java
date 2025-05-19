package in.siddu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.siddu.entity.Product;
import in.siddu.repository.ProductRepo;
import in.siddu.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductRepo prepo;
	@Override
	public Integer saveProduct(Product p) {
		// TODO Auto-generated method stub
		return prepo.save(p).getId();
	}
	@Override
	public List<Product> getProductsByCategoryId(Integer categoryId) {
		// TODO Auto-generated method stub
		return prepo.findByCategoryId(categoryId);
		
	}
	@Override
	public Product getProduct(Integer id) {
		// TODO Auto-generated method stub
		return prepo.findById(id).get();
	}
	
	@Override
	public List<Product> filterProducts(Integer subCategoryId, String sort, Integer id) {
	    if (subCategoryId == null && (sort == null || sort.isEmpty())) {
	        return prepo.findByCategoryId(id);
	    }

	    if (sort != null && sort.equals("lowToHigh")) {
	        return prepo.findByFiltersOrderByPriceAsc(subCategoryId, id);
	    } else if (sort != null && sort.equals("highToLow")) {
	        return prepo.findByFiltersOrderByPriceDesc(subCategoryId, id);
	    } else {
	        return prepo.findByFilters(subCategoryId, id); 
	    }
	}



}
