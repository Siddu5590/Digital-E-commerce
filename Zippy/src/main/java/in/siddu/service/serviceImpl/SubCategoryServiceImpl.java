package in.siddu.service.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.siddu.entity.SubCategory;
import in.siddu.repository.SubCategoryRepo;
import in.siddu.service.SubCategoryService;

@Service
public class SubCategoryServiceImpl implements SubCategoryService{

    @Autowired
    private SubCategoryRepo subCategoryRepo;

    public List<SubCategory> getSubcategoriesByCategoryId(Integer categoryId) {
        return subCategoryRepo.findByCategoryId(categoryId);
    }

	@Override
	public SubCategory findById(Integer id) {
		// TODO Auto-generated method stub
		return subCategoryRepo.findById(id).orElse(null);
	}

	@Override
	public List<SubCategory> getAllSubCategories() {
		
		return subCategoryRepo.findAll();
	}
}
