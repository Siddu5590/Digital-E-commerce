package in.siddu.service;

import java.util.List;

import in.siddu.entity.Category;
import in.siddu.entity.CategoryForm;

public interface AddCategory {
	
	public void addCategory(CategoryForm cat);
	
	public Category findByID(Integer id);

	public List<Category> getAllCategories();
	
	public void deleteCategory(Integer id);
	
	
	
}
