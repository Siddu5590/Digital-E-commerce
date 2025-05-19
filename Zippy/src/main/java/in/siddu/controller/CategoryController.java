package in.siddu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.siddu.entity.Category;
import in.siddu.entity.CategoryForm;
import in.siddu.entity.User;
import in.siddu.service.AddCategory;

@Controller
@RequestMapping("/Category")
public class CategoryController {
	
	@Autowired
    private AddCategory categoryService;

    @GetMapping("/addcategory")
    public String showCategoryForm(@RequestParam(required = false) String message, Model model) {
    	
    	if(message!=null)
    		model.addAttribute("message", message);
    	
        model.addAttribute("categoryForm", new CategoryForm());
        return "addcategory";
    }

    @PostMapping("/addcategory")
    public String addCategory(@ModelAttribute CategoryForm categoryForm, RedirectAttributes model) {
        try {
            categoryService.addCategory(categoryForm);
            model.addAttribute("message", "Category added successfully!");
            System.out.println("Category added successfully...");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to add category. Please try again.");
        }
        return "redirect:/Category/addcategory";
    }
	
    @GetMapping("/delete")
    public void deleteCategory( Integer id)
    {
    	
    		categoryService.deleteCategory(id);
    	
    	
    }
    
    @GetMapping("/viewCategory")
	public String viewAllUsers(@RequestParam(required = false) String message, Model model) {
		List<Category> list= categoryService.getAllCategories();
		if (message != null) {
			model.addAttribute("message", message);
		}
		model.addAttribute("list", list);
		return "viewCategory";
		
	}

    @PostMapping("/edit")
    public String editCategory(@RequestParam("id") Integer id, Model model) {
        Category category = categoryService.findByID(id);
        
        if (category != null) {
            model.addAttribute("category", category);
            return "editCategory"; 
        } else {
            model.addAttribute("message", "User not found.");
            return "viewCategory"; 
        }
    }

}
