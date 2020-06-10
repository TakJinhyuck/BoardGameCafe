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
import com.my.vo.GameType;
public class GameTypeDAO {


	public void updatePlusStock(String board_GameType) throws ModifyException{
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		//ResultSet rs = null;
		try{
			con=MyConnection.getConnection();
			String updateStockQuentitySQL =
				"UPDATE gametype SET stock=stock+1 WHERE game_name=?";
			pstmt =con.prepareStatement(updateStockQuentitySQL);
			pstmt.setString(1, board_GameType);
			int rowcnt = pstmt.executeUpdate();
			if(rowcnt == 0) {
				throw new ModifyException("보드게임이름이 없습니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			MyConnection.close(con, pstmt, null);
		}
	}
	
	
	//보드게임 추가
	public void insert(GameType g) throws AddException{
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con=MyConnection.getConnection();
			String insertSQL=
					"INSERT INTO gametype(game_name, stock, quantity, max_p, min_p, game_level, playtime, usage, use_cnt)"+
							" VALUES (?,?,?,?,?,?,?,?,?)";
			try {
				int stock = 0;
				if(g.getStock()!=null) {
					stock = g.getStock().size();
				}
				pstmt=con.prepareStatement(insertSQL);
				pstmt.setString(1, g.getGame_type());
				pstmt.setInt(2, stock);
				pstmt.setInt(3, g.getQuantity());
				pstmt.setInt(4, g.getMax_p());
				pstmt.setInt(5, g.getMin_p());
				pstmt.setFloat(6, g.getGame_level());
				pstmt.setInt(7, g.getPlaytime());
				pstmt.setString(8, g.getUsage());
				pstmt.setInt(9, g.getUse_cnt());
				pstmt.executeQuery();
			}catch(SQLException e) {
				if(e.getErrorCode()==1)///1번이면 pk중복임!!
					throw new AddException("이미 존재하는 아이디 입니다.");
				throw new AddException(e.getMessage());
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new AddException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, null);
		}
	}




	/**
	 * 재고(stock), 수량(quantity) 사용횟수(use_cnt)수정하기
	 * @param g
	 * @throws ModifyException
	 */
	public void update(GameType g) throws ModifyException{
		Connection con = null;
		Statement stmt = null;
		try {

			con=MyConnection.getConnection();
			String updateSQL1="UPDATE gametype SET ";
			String updateSQL2=" WHERE game_name='"+g.getGame_type()+"'";
			boolean flag=false;
			if(g.getStock()!=null) {  //null이아니면 수정한다는 뜻~
				updateSQL1 +="stock='" +g.getStock().size() + "'";
				flag=true;
			}
			if(g.getQuantity()!= 0) {
				if(flag) {
					updateSQL1 += ",";
				}
				updateSQL1 +="quantity='" +g.getQuantity()+"'";
				flag=true;
			}
			if(g.getUse_cnt()!=0) {
				if(flag) {
					updateSQL1 += ",";
				}
				updateSQL1 += "use_cnt="+g.getUse_cnt();
				flag=true;
			}
			if(flag) {//flag가 true라면 하나라도 있다면 한개라도 값을 수정해야함
				stmt=con.createStatement();
				stmt.executeUpdate(updateSQL1+updateSQL2);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}finally {
			MyConnection.close(con, stmt, null);
		}
	}



	///////////////////////////선호 지희/////////////////////

	/**
	 * 이름으로 게임찾기
	 * @param game
	 * @return GameType
	 */
	
	
	public List<GameType> selectByName(String gametype) throws NotFoundException {
		List<GameType> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con=MyConnection.getConnection();
			String findByGameNameSQL =
					"SELECT * FROM gametype WHERE game_name LIKE ?";
			pstmt = con.prepareStatement(findByGameNameSQL);
			pstmt.setString(1, "%"+gametype+"%");
			rs=pstmt.executeQuery();
			while(rs.next()==true) {
				String game_type = rs.getString("game_name");
				//List<BoardGame> stock = //rs.getInt("stock");
				int quantity = rs.getInt("quantity");
				int max_p = rs.getInt("max_p");
				int min_p = rs.getInt("min_p");
				float game_level = rs.getFloat("game_level");
				int playtime = rs.getInt("playtime");
				String usage = rs.getString("usage");
				int use_cnt = rs.getInt("use_cnt");
				list.add(new GameType(game_type, null, quantity, max_p, min_p, game_level, playtime, usage, use_cnt));
			}
			
			if(list.size()==0) {
				throw new NotFoundException(gametype+"에 해당하는 게임은 없습니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("검색하는 게임이 존재하지 않습니다.");
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
		return list;
	}
	/**
	 * 시간으로 찾기
	 * @param play_time
	 * @return GameType
	 */
	public List<GameType> selectByTime(int play_time) throws NotFoundException{
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		List<GameType> list= new ArrayList<>();
		try {
			con=MyConnection.getConnection();
			String searchByTimeSQL="SELECT* FROM GameType WHERE playtime <= ? ";
			pstmt=con.prepareStatement(searchByTimeSQL);
			pstmt.setInt(1,play_time);
			rs=pstmt.executeQuery();
			if(rs.next()==false) {
				throw new NotFoundException("찾는 시간에 맞는 게임이 없습니다.");
			}
			while(rs.next()) {
				String game_name=rs.getString("game_name");
				int stock=rs.getInt("stock");
				int quantity =rs.getInt("quantity");
				int max_p=rs.getInt("max_p");
				int min_p=rs.getInt("min_p");
				int game_level=rs.getInt("game_level");
				int playtime=rs.getInt("playtime");
				String usage=rs.getString("usage");
				int use_cnt=rs.getInt("use_cnt");
				list.add( new GameType(game_name,null,quantity,max_p,min_p,game_level,playtime
						, usage,use_cnt));
			}return list;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	/**
	 * 난이도로 찾기
	 * @param gamelevel
	 * @return GameType
	 * @throws NotFoundException
	 */
	public List<GameType> selectByLevel(float gamelevel) throws NotFoundException {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		List<GameType> list= new ArrayList<>();
		try {
			con=MyConnection.getConnection();
			String searchByLevelSQL="SELECT* FROM GameType WHERE game_level = ? ";
			pstmt=con.prepareStatement(searchByLevelSQL);
			pstmt.setFloat(1,gamelevel);
			rs=pstmt.executeQuery();
			if(rs.next()==false) {
				throw new NotFoundException(gamelevel+"난이도의 게임이 없습니다.");
			}
			while(rs.next()) {
				String game_name=rs.getString("game_name");
				int stock=rs.getInt("stock");
				int quantity =rs.getInt("quantity");
				int max_p=rs.getInt("max_p");
				int min_p=rs.getInt("min_p");
				float game_level=rs.getInt("game_level");
				int playtime=rs.getInt("playtime");
				String usage=rs.getString("usage");
				int use_cnt=rs.getInt("use_cnt");
				list.add(new GameType(game_name,null,quantity,max_p,min_p,game_level,playtime
						, usage,use_cnt));

			}return list;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	/**
	 * 인원수로 찾기
	 * @param game_playperson
	 * @return GameType
	 */
	public List<GameType> selectByPlayPerson(int game_playperson)throws NotFoundException {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		List<GameType> list= new ArrayList<>();
		try {
			con = MyConnection.getConnection();
			String searchByPlayPersonSQL = "SELECT * FROM gametype WHERE max_p >= ? AND min_p <= ?";
			pstmt = con.prepareStatement(searchByPlayPersonSQL);
			pstmt.setInt(1, game_playperson);
			pstmt.setInt(2, game_playperson);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String game_name = rs.getString("game_name");
				int stock = rs.getInt("stock");
				int quantity = rs.getInt("quantity");
				int max_p = rs.getInt("max_p");
				int min_p = rs.getInt("min_p");
				int game_level = rs.getInt("game_level");
				int playtime = rs.getInt("playtime");
				String usage = rs.getString("usage");
				int use_cnt = rs.getInt("use_cnt");
				list.add(new GameType(game_name,null,quantity,max_p,min_p,game_level,playtime
						, usage,use_cnt));
			}if(list.size()==0) {
				throw new NotFoundException(game_playperson+"에 맞는 인원수의 게임이 없습니다.");
			}return list;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException(e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	/**
	 * 전체검색
	 * @return List<GameType>
	 */
	public List<GameType> selectAll()throws NotFoundException {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		List<GameType> list= new ArrayList<>();
		try {
			con=MyConnection.getConnection();
			String selectAllSQL="SELECT * FROM gametype ORDER BY use_cnt DESC";
			pstmt=con.prepareStatement(selectAllSQL);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				String game_name=rs.getString("game_name");
				int stock=rs.getInt("stock");
				int quantity =rs.getInt("quantity");
				int max_p=rs.getInt("max_p");
				int min_p=rs.getInt("min_p");
				int game_level=rs.getInt("game_level");
				int playtime=rs.getInt("playtime");
				String usage=rs.getString("usage");
				int use_cnt=rs.getInt("use_cnt");
				list.add(new GameType(game_name,null,quantity,max_p,min_p,game_level,playtime
						, usage,use_cnt));
			}
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			throw new NotFoundException("전체검색 오류"+e.getMessage());
		}finally {
			MyConnection.close(con, pstmt, rs);
		}
	}




	public void updateStockQuentity(String board_GameType) throws ModifyException{
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		//ResultSet rs = null;
		try{ 
			con=MyConnection.getConnection();
			String updateStockQuentitySQL =
				"UPDATE gametype SET stock=stock+1, quantity=quantity+1 WHERE game_name=?";
			
			pstmt =con.prepareStatement(updateStockQuentitySQL);
			pstmt.setString(1, board_GameType);

			int rowcnt = pstmt.executeUpdate();
			if(rowcnt == 0) {
				throw new ModifyException("보드게임이름이 없습니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			MyConnection.close(con, pstmt, null);
		}
	}


	public void updateMinusStockQuentity(String board_GameType) throws NotFoundException{
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		//ResultSet rs = null;
		try{
			con=MyConnection.getConnection();
			String updateMinusStockQuentitySQL =
				"UPDATE gametype SET stock=stock-1, quantity=quantity-1 WHERE game_name=?";
			
			pstmt =con.prepareStatement(updateMinusStockQuentitySQL);
			pstmt.setString(1, board_GameType);
			pstmt.executeUpdate();
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			MyConnection.close(con, pstmt, null);
		}
	}
}