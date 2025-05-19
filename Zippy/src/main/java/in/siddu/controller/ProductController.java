package in.siddu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.siddu.entity.Category;
import in.siddu.entity.Product;
import in.siddu.entity.SubCategory;
import in.siddu.repository.CategoryRepo;
import in.siddu.service.AddCategory;
import in.siddu.service.ProductService;
import in.siddu.service.SubCategoryService;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService pservice;

    @Autowired
    private AddCategory cservice;

    @Autowired
    private SubCategoryService subCategoryService;
    
    @Autowired
    private CategoryRepo categoryRepo;

    @GetMapping("/form")
    public String loadPage(Model model) {
        List<Category> categories = cservice.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());           
        return "productform";
    }

    @PostMapping("/form")
    public String saveProduct(@ModelAttribute Product p, Model model) {
        String message;

        if (p.getCategory() == null || p.getCategory().getId() == null ||
            p.getPsubCategory() == null || p.getPsubCategory().getId() == null) {
            message = "Please select both category and sub-category!";
        } else {
            Category category = cservice.findByID(p.getCategory().getId());
            SubCategory subCategory = subCategoryService.findById(p.getPsubCategory().getId());

            if (category == null || subCategory == null) {
                message = "Invalid category or sub-category selected.";
            } else {
                p.setCategory(category);
                p.setPsubCategory(subCategory);
                try {
                    Integer id = pservice.saveProduct(p);
                    message = "Product '" + id + "' added successfully!";
                } catch (Exception e) {
                    message = "Error while saving product: ";
                }
            }
        }

        model.addAttribute("product", new Product());
        model.addAttribute("categories", cservice.getAllCategories());
        model.addAttribute("message", message);

        return "productform";
    }



    // This will fetch subcategories dynamically based on the selected category
    @GetMapping("/getSubcategories")
    public String getSubcategories(@RequestParam("categoryId") Integer categoryId, Model model) {
        List<SubCategory> subcategories = subCategoryService.getSubcategoriesByCategoryId(categoryId);
        model.addAttribute("subcategories", subcategories);
        return "fragments :: subcategoryOptions";  // This is for Thymeleaf fragment.html class
    }
    
    //get products related to that id from homePage
    @GetMapping("/getproduct")
    public String getProductsByCategory(@RequestParam Integer categoryId, Model model) {
    	List<Product> products=pservice.getProductsByCategoryId(categoryId);
        List<SubCategory> subcategories = subCategoryService.getSubcategoriesByCategoryId(categoryId);

        model.addAttribute("subcat", subcategories);
    	model.addAttribute("products", products);
    	return "products";
    }
    

    
    @GetMapping("/filter")
    public String filterProducts(
            @RequestParam(required = false) Integer subCategoryId,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer id,
            Model model) {

        List<Product> filteredProducts = pservice.filterProducts(subCategoryId, sort, id);

        model.addAttribute("products", filteredProducts);
        model.addAttribute("subcat", subCategoryService.getSubcategoriesByCategoryId(id)); // For dropdown
        return "products";
    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam Integer id) {
    	return "productDetails";
    	
    }
  
    
}
