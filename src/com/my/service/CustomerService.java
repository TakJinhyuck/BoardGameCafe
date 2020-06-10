package com.my.service;
import java.util.List;
import com.my.dao.CustomerDAO;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.vo.Customer;
public class CustomerService {
	private static CustomerService service=new CustomerService();
	private CustomerDAO dao;
	private CustomerService() {  //----------->싱글톤패턴으로 만들기
		dao= new CustomerDAO();
	}
	public static CustomerService getInstance(){
		return service;
	}
	/**
	 * 가입
	 * @param c
	 * @throws AddException
	 */
	public void insert(Customer c) throws AddException{
		dao.insert(c);
	}
	/**
	 * 로그인
	 * @param id
	 * @param pwd
	 * @throws NotFoundException
	 */
	public void login(Customer c) throws NotFoundException{
		Customer customer= dao.login(c.getId(),c.getPwd());
		if(customer.getCustomer_status() !=1 ) {
			throw new NotFoundException("☆☆☆☆☆☆☆☆☆☆탈퇴한 회원입니다.☆☆☆☆☆☆☆☆☆☆");
			//System.out.println("☆☆☆☆☆☆☆☆☆☆탈퇴한 회원입니다.☆☆☆☆☆☆☆☆☆☆");
		}
	}
	/*
	 * 수정하기
	 */
	public void update(Customer c) throws ModifyException{
		dao.update(c);
	}
	/**
	 * 이름으로 회원조회
	 */
	public List<Customer> findByName(String name)throws NotFoundException {
		return dao.selectByName(name);
	}
	/**
	 * 아이디로 회원조회
	 */
	public Customer findById(String id) throws NotFoundException{
		return dao.selectById(id);
	}
	/**
	 * 전체 회원조회
	 */
	public List<Customer> findAll()throws NotFoundException{
		return dao.selectAll();
	}
	/**
	 * 탈퇴
	 * @param id
	 * @throws ModifyException
	 */
	public void deleteCustomer(String id) throws ModifyException {
		Customer c = new Customer();
		c.setId(id);
		c.setCustomer_status(2);
		dao.update(c);
		
	}
	
	
}