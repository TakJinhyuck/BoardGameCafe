package com.my.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.sql.MyConnection;
import com.my.vo.Customer;
import com.my.vo.GameType;
public class CustomerDAO {
	/**
	 * 고객 id와 비밀번호(pwd)를 등록해준다_회원가입
	 */
	public void insert(Customer c) throws AddException{
		//1.DB연결
		Connection con=null;
		PreparedStatement pstmt=null;
			try {
		con=MyConnection.getConnection();
		String insertSQL="INSERT INTO customer(id,pwd,name ) "+
		" VALUES(?,?,?)";	
		try {
			pstmt=con.prepareStatement(insertSQL);
			pstmt.setString(1, c.getId());
			pstmt.setString(2, c.getPwd());
			pstmt.setString(3, c.getName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			if(e.getErrorCode()==1)
				throw new AddException("이미 존재하는 아이디 입니다.");
			throw new AddException(e.getMessage());
		}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, null);
			}
	}
	/**
	 *
	 * @param id
	 * @param password
	 * @return
	 * @throws NotFoundException
	 */
	public Customer login(String id,String password) throws NotFoundException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = MyConnection.getConnection();
			//2)SQL송신
			String selectByIdSQL =  "SELECT * FROM customer WHERE id=? AND pwd = ?";
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String name = rs.getString("name");
				int status = rs.getInt("customer_status");
				return new Customer(id,password,status,name);
			}else {
				throw new NotFoundException("아이디 또는 비밀번호가 틀렸습니다.");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new NotFoundException("ID로 검색 오류 : " + e.getMessage());
		}finally {
			//3) DB연결해제
			MyConnection.close(con, pstmt, null);
		}
	}
	/**
	 *  회원정보를 수정한다.
	 * @param c
	 *
	 */
	public void update(Customer c) throws ModifyException{
		Connection con=null;
		Statement stmt=null;
		try {
			con=MyConnection.getConnection();
			String updateSQL1="UPDATE customer SET ";
			String updateSQL2=" WHERE id='"+c.getId()+"'";
			boolean flag=false;
			if(c.getPwd()!=null) {  //null이아니면 수정한다는 뜻~
				updateSQL1 +="pwd='" +c.getPwd()+"'";
				flag=true;
			}
			if(c.getName()!=null) {
				if(flag) {
					updateSQL1 += ",";
				}
				updateSQL1 +="name='" +c.getName()+"'";
				flag=true;
			}
			if(c.getCustomer_status()!=0) {
				if(flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "customer_status="+c.getCustomer_status();
				flag=true;
			}
			if(flag) {//flag가 true라면 하나라도 있다면 한개라도 값을 수정해야함
				stmt=con.createStatement();
				stmt.executeUpdate(updateSQL1+updateSQL2);
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}finally {
			MyConnection.close(con, stmt, null);
		}
	}
	/**
	 * 이름으로 회원 검색
	 * @param name
	 * @return
	 * @throws NotFoundException
	 */
	public List<Customer> selectByName(String name) throws NotFoundException{
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		List<Customer> list= new ArrayList<>();
		try {
			con=MyConnection.getConnection();
			String selectByNameSQL=" SELECT * FROM CUSTOMER WHERE name = ?";
			pstmt=con.prepareStatement(selectByNameSQL);
			pstmt.setString(1,name);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				String id= rs.getString("id");
				String pwd= rs.getString("pwd");
				int customer_status= rs.getInt("customer_status");
				list.add(new Customer(id,pwd,customer_status,name));
				{if(customer_status==2) {
					throw new Exception(name+"님은 탈퇴한 회원입니다.");
						}
				}
			}if(list.size()==0){
			throw new NotFoundException(name+"에 해당하는 이름은 없습니다.");					
					
			}return list;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	/**
	 * 아이디로 검색
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	public Customer selectById(String id) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Customer customer = new Customer();
		try {
			con = MyConnection.getConnection();
			String selectByIdSQL = "SELECT * FROM customer WHERE id = ?  AND customer_status = 1";
			pstmt = con.prepareStatement(selectByIdSQL);
			pstmt.setString(1,id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				String pwd = rs.getString(2);
				String name = rs.getString(3);
				int status = rs.getInt(4);
				customer = new Customer(id,pwd,status,name);
				return customer;
			} else if(rs.next()) {
				throw new NotFoundException(id+"에 대한 정보가 없습니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
		return customer;
	}
	/**
	 * 전체 검색
	 * @return
	 * @throws NotFoundException
	 */
	public List<Customer> selectAll()throws NotFoundException {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		List<Customer> list= new ArrayList<>();
		try {
			con=MyConnection.getConnection();
			String selectAllSQL=" SELECT * FROM CUSTOMER WHERE customer_status = 1 ";
			pstmt=con.prepareStatement(selectAllSQL);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				String id= rs.getString("id");
				String pwd= rs.getString("pwd");
				int customer_status= rs.getInt("customer_status");
				String name=rs.getString("name");
				list.add(new Customer(id,pwd,customer_status,name ));
			}
		return list;
	}catch(Exception e) {
		e.printStackTrace();
		throw new NotFoundException("전체검색 오류:" +e.getMessage());
	}finally {
		MyConnection.close(con, pstmt, rs);
	}
	}
}