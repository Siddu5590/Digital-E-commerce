package in.siddu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String pname;
	
	@Column(length = 1000)
	private String pimg;
	
	@Column(length = 1000)
	private String pimg2;
	
	@Column(length = 1000)
	private String pimg3;
	private Double pprice;
	private String quantity;
	private String pdescription;
	private Double pdiscount;
	
	private String stock;
	
	@ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
	
	@ManyToOne
	@JoinColumn(name = "subcategory_id")
	private SubCategory psubCategory;
	
	
}
