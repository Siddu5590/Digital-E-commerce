package in.siddu.service.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.siddu.entity.Product;
import in.siddu.entity.User;
import in.siddu.repository.ProductRepo;
import in.siddu.repository.UserRepo;
import in.siddu.service.UserService;
import jakarta.servlet.http.HttpSession;
@Service 
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private ProductRepo prepo;
	
	@Override
	public Integer saveUser(User u) {
		// TODO Auto-generated method stub
		return urepo.save(u).getId();
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return urepo.findAll();
	}

	@Override
	public User getOneUser(int id) {
		// TODO Auto-generated method stub
		return urepo.findById(id).get();
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		urepo.deleteById(id);
	}

	@Override
	public void update(User u) {
		// TODO Auto-generated method stub
		urepo.save(u);
	}

	@Override
	public String loginUser(String email, String password, HttpSession session) {
		System.out.println(email + "this is in imple");
		String b;
		if ((urepo.findByEmailAndPassword(email).getEmail().equals(email))
				&& (urepo.findByEmailAndPassword(email).getPassword().equals(password))) {
			System.out.println("login Succesfull...........");
			
			session.setAttribute("user", urepo.findByEmailAndPassword(email));
			session.setAttribute("uid", urepo.findByEmailAndPassword(email).getId());
			session.setAttribute("umail", urepo.findByEmailAndPassword(email).getEmail());
			session.setAttribute("uname", urepo.findByEmailAndPassword(email).getName());
			session.setAttribute("uphone", urepo.findByEmailAndPassword(email).getPhone());
			b = "success";
		} else {
			System.out.println("Login unsuccess..");
			b = "fail";
		}
		return b;

	}

	@Override
	public boolean checkUser(String email) {
		// TODO Auto-generated method stub
		return urepo.existsByEmail(email);
	}

	

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return prepo.findAll();
	}

/*	@Override
	public String forgotPassword(String email, String password) {
		String status = "";
		boolean result = urepo.existsByEmail(email);
		if (result) {
			int i = urepo.updatePassword(email, password);
			if (i > 0) {
				status = "success";
				System.out.println(status);
			} else {
				status = "updatation failure";
				System.out.println(status);
			}
		} else {
			System.out.println("No such mail found");
			status = "failure";
			System.out.println(status);
		}
		return status;
	}
	
*/
}
