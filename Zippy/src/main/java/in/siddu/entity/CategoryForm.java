package in.siddu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryForm {

	    private String categoryName;
	    private String image;
	    private String subcategoryName;
	    private String subimage;

}
