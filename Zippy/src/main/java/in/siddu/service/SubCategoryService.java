package in.siddu.service;

import java.util.List;

import in.siddu.entity.SubCategory;

public interface SubCategoryService {
	
	public SubCategory findById(Integer id);

	 public List<SubCategory> getSubcategoriesByCategoryId(Integer categoryId);
	 
	 public List<SubCategory> getAllSubCategories();
}
