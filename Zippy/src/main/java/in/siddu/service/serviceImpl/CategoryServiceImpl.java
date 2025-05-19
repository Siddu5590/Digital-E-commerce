package in.siddu.service.serviceImpl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.siddu.entity.Category;
import in.siddu.entity.CategoryForm;
import in.siddu.entity.SubCategory;
import in.siddu.repository.CategoryRepo;
import in.siddu.repository.SubCategoryRepo;
import in.siddu.service.AddCategory;

@Service
public class CategoryServiceImpl implements AddCategory {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private SubCategoryRepo subcategoryRepo;

    public void addCategory(CategoryForm cat) {
        System.out.println("Inside addCategoryWithSubcategory");
        System.out.println("categoryName = " + cat.getCategoryName());
        System.out.println("subcategoryName = " + cat.getSubcategoryName());

        Category category = categoryRepo.findByName(cat.getCategoryName())
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(cat.getCategoryName());
                    newCat.setImg(cat.getImage());
                    return categoryRepo.save(newCat);
                });

        SubCategory subcategory = new SubCategory();
        subcategory.setName(cat.getSubcategoryName());
        subcategory.setImg(cat.getSubimage());
        subcategory.setCategory(category);
        subcategoryRepo.save(subcategory);
    }


    
    public List<Category> getAllCategories() {
        return categoryRepo.findAllWithSubcategories();
    }



	@Override
	public Category findByID(Integer id) {
		// TODO Auto-generated method stub
		return categoryRepo.findById(id).orElse(null);
	}



	@Override
	public void deleteCategory(Integer id) {
		// TODO Auto-generated method stub
		categoryRepo.deleteById(id);
	}

}
