package com.my.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.sql.MyConnection;
import com.my.vo.BoardGame;
import com.my.vo.Customer;
import com.my.vo.GameService;
import com.my.vo.GameType;

public class GameServiceDAO {
	
	//게임 대여하면 서비스가 하나 만들어지고 테이블에 반영된다.	
	public void insert(GameService gameService) throws AddException{
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		// to_date('1111-11-11 21:11:22', 'yyyy-mm-dd hh24:mi:ss')
		try {
			con = MyConnection.getConnection();
			con.setAutoCommit(false);
			String insertServiceSQL = "INSERT INTO service (service_no,id,game_no,start_time)"
					+ " VALUES(?,?,?,to_date(?, 'yyyy-mm-dd hh:mi:ss'))";
			pstmt = con.prepareStatement(insertServiceSQL);

			long no = gameService.getService_no();
			String id = gameService.getService_customer().getId();
			String game_no = gameService.getService_BoardGame().getGame_no();
			java.sql.Date start_time = new java.sql.Date(gameService.getStart_time().getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String st = sdf.format(start_time);
			System.out.println("대여시작 : "+st);
			System.out.println("==========================================");
			System.out.println("");
			pstmt.setLong(1,no);
			pstmt.setString(2,id);
			pstmt.setString(3,game_no);
			pstmt.setString(4,st);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new AddException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, null);
		}
	}
	
	//반납을 하면 요금과 끝나느 시간을 검색해서 현재 서비스를 업데이트한다.
	
	public void update(GameService service) throws ModifyException {
		Connection con = null;
		PreparedStatement pstmt = null;
//		Statement stmt = null;
//		GameType g = new GameType();
		
		try {
			con = MyConnection.getConnection();
			String updateServiceSQL = "UPDATE service SET charge = ? , end_time = to_date(?, 'yyyy-mm-dd hh24:mi:ss') WHERE service_no = ? ";
//			String updateServiceSQL2 = "UPDATE gametype SET ";
//			
//			
//			String updateServiceSQL3=" WHERE game_name='"+g.getGame_type()+"'";
//			boolean flag=false;
//			if(g.getStock()!=null) {  //null이아니면 수정한다는 뜻~
//				updateServiceSQL2 +="stock='" +g.getStock()+ "1" + "'";
//				flag=true;
//			}
//			if(flag) {//flag가 true라면 하나라도 있다면 한개라도 값을 수정해야함
//				stmt=con.createStatement();
//				stmt.executeUpdate(updateServiceSQL2+updateServiceSQL3);
//				con.commit();
//			}
			pstmt = con.prepareStatement(updateServiceSQL);
			
			
			int charge = service.getCharge();
			java.sql.Date end_time = new java.sql.Date(service.getEnd_time().getTime()); 
			int service_no = service.getService_no();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String et = sdf.format(end_time);
			System.out.println("반납 시간 : "+et);
			pstmt.setInt(1, service.getCharge());
			pstmt.setString(2, et);
			pstmt.setInt(3, service.getService_no());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new ModifyException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, null);
		}
		
	}
	
	//날짜를 선택해서 해당되는 날의 요금 정보를 출력한다.
	public List<GameService> selectByDate(String date) throws NotFoundException {
		
		List<GameService> list = new ArrayList<>();
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
			String selectBtDateSQL = 
					"SELECT * FROM service WHERE usedate BETWEEN ? AND ? ";
			stmt = con.createStatement();
			
			java.sql.Date start = null;
			java.sql.Date end = null;
			String test = "to_char(start_time, 'yyyy-MM-dd hh24:mi:ss')" + ",to_char(end_time, 'yyyy-MM-dd hh24:mi:ss')";
			//String sql = "SELECT * FROM service WHERE use_date between TO_DATE('" + date + " 00:00:00','YYYY-MM-DD HH24:MI:SS') and TO_DATE('"+ date +" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')";
			String sql = "SELECT id,charge,service_no,use_date,game_no,start_time,end_time,"+ test +"FROM service WHERE use_date between TO_DATE('" + date + " 00:00:00','yyyy-MM-dd HH24:MI:SS') and TO_DATE('"+ date +" 23:59:59', 'yyyy-MM-dd HH24:MI:SS')";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				
				String id = rs.getString("id");
				int charge = rs.getInt("charge");
				int service_no = rs.getInt("service_no");
				/////////////////////////////
				
				String st = rs.getString(8);
				String et = rs.getString(9);
				//String ud = rs.getString("to_char(use_date, 'yyyy-mm-dd hh24:mi:ss')");
				
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
				java.util.Date start_time= sdf.parse(st);
				java.util.Date end_time= sdf.parse(et);
				java.util.Date use_date= (Date)rs.getDate("use_date");
				String game_no = rs.getString("game_no");
				 
				
				Customer customer = new Customer();
				customer.setId(id);
				BoardGame game = new BoardGame();
				game.setGame_no(game_no);
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH시 mm분 ss");
				GameService gameService = new GameService();
				gameService.setService_customer(customer);
				gameService.setStart_time(start_time);
				gameService.setEnd_time(end_time);
				gameService.setUse_date(use_date);
				gameService.setCharge(charge);
				gameService.setService_BoardGame(game);
				gameService.setService_no(service_no);
				list.add(gameService);
			}
			if(list.size()==0) {
				throw new NotFoundException("해당 날짜에 해당되는 데이터가 없습니다.");
			}
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new NotFoundException(e.getMessage());
		} finally {
			MyConnection.close(con, stmt, rs);
		}
		
		
	}
}
