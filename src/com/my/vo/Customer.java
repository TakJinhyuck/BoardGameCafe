package com.my.vo;

public class Customer {
	/**
	 * id, pwd customer_status
	 */
	private String id;
	private String name;
	private String pwd;
	private int customer_status;
	
	public Customer() {
	}
	
	
	
	public Customer(String id, String pwd, int customer_status, String name) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.customer_status = customer_status;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getCustomer_status() {
		return customer_status;
	}

	public void setCustomer_status(int customer_status) {
		this.customer_status = customer_status;
	}

	@Override
	public String toString() {
		return name+"고객님 [id=" + id + ", 이름=" + name + ", 비밀번호=" + pwd + ", 회원/비회원=" + customer_status + "]";
	}

	
	
	
}
