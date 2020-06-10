package com.my.controller;
import java.util.List;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.service.CustomerService;
import com.my.session.Session;
import com.my.session.SessionSet;
import com.my.view.FailView;
import com.my.view.SuccessView;
import com.my.vo.Customer;
public class CustomerController {
	private static CustomerController controller=new CustomerController();
	private CustomerService service =CustomerService.getInstance();
	public static CustomerController getInstance(){
		return controller;
	}
	/**
	 * 로그인
	 * @param c
	 */
	public void login(String id, String pwd) {
			Customer c= new Customer();
			c.setId(id);
			c.setPwd(pwd);
		try {
			service.login(c);
			SuccessView.printLogin("☆☆☆☆☆☆☆☆☆☆로그인 성공☆☆☆☆☆☆☆☆☆☆");
			SessionSet ss= SessionSet.getInstance();
			Session session= new Session(id);
			ss.add(session);
		}catch(NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}
	/**
	 * 로그아웃
	 */
	public void logout(String id) {
		SessionSet ss= SessionSet.getInstance();
		Session session= new Session(id);
		ss.remove(session);
	}
	/**
	 * 가입
	 * @param c
	 */
	public void register(Customer c) {
		try {
			service.insert(c);
			SuccessView.printRegisterCustomer("가입 성공");
		} catch (AddException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}
	/**
	 * 수정하기
	 */
	public void modify(Customer c) {
		try {
			service.update(c);
			SuccessView.printModifyCustomer("☆☆☆☆☆☆☆수정 성공!☆☆☆☆☆☆☆");
		} catch (ModifyException e) {
			FailView.errorMessage("☆☆☆☆☆☆☆수정이 되지 않았습니다.☆☆☆☆☆☆☆");
		}
	}
	/**
	 * 이름으로 회원조회
	 */
	public  void searchByName(String name) {
		try {
			List<Customer> list=service.findByName(name);
			SuccessView.printByCustomerName(list);
			
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	/**
	 * 아이디로 회원조회
	 */
	public void searchById(String id) {
		try {
			Customer customer=service.findById(id);
			SuccessView.printById(customer);
		} catch (NotFoundException e) {
			
			FailView.errorMessage(e.getMessage());
		}
	}
	/**
	 * 전체 회원조회
	 */
	public void selectAll() {
		try {
			List<Customer> list=service.findAll();
			SuccessView.printAllCustomer(list);
		} catch (NotFoundException e) {
			
			FailView.errorMessage(e.getMessage());
		}
	}
	public void deleteCustomer(String id) {
		try {
			service.deleteCustomer(id);
			SuccessView.printModify("☆☆☆☆☆☆☆탈퇴되었습니다.☆☆☆☆☆☆☆");
		}catch(ModifyException e) {
			FailView.errorMessage(e.getMessage());
		}
		
	}
}