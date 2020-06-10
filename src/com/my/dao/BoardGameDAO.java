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
import com.my.vo.BoardGame;

public class BoardGameDAO {
	
	
	//보드게임 번호로 검색
	public BoardGame selectByNo(String id) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = MyConnection.getConnection();
			String sql = "SELECT * FROM boardgame WHERE game_no = ? AND game_status = 1 ORDER BY game_no";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(!rs.next()) {
				throw new NotFoundException(id +"에 해당하는 게임을 찾을 수 없습니다.");
			}
			String game_no = rs.getString("game_no"); 
			int game_status = rs.getInt("game_status");
			String board_GameType = rs.getString("game_name"); 
			BoardGame game = new BoardGame(game_no, game_status, board_GameType);
			return game;
		} catch (Exception e) {
			throw new NotFoundException(id +"에 해당하는 게임을 찾을 수 없습니다.");
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	
	
	
	//보드게임 전체 검색 
		public List<BoardGame> selectAllBoardGame() throws NotFoundException{
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<BoardGame> boardgame = new ArrayList<>();
			try {
				con=MyConnection.getConnection();
				String selectAllBoardGameSQL = "SELECT * FROM boardgame ORDER BY game_no";
				pstmt=con.prepareStatement(selectAllBoardGameSQL);
				rs=pstmt.executeQuery();
				while(rs.next()) {
					String game_no = rs.getString("game_no");
					int game_status = rs.getInt("game_status");
					String Board_GameType = rs.getString("game_name");				
					boardgame.add(new BoardGame(game_no, game_status, Board_GameType));
				}
				if(boardgame.size()==0) {
					throw new NotFoundException("보드게임이 존재하지 않습니다.");
				}
			}catch(Exception e) {
				e.printStackTrace();
				throw new NotFoundException(e.getMessage());
			}finally {
				MyConnection.close(con, pstmt, rs);
			}
			return boardgame;
		}
	
	
	//보드게임 이름으로 검색 
	public List<BoardGame> selectByName(String name) throws NotFoundException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardGame> list = new ArrayList<>();
		try {
			con = MyConnection.getConnection();
			String selectByNameSQL = "SELECT * FROM boardgame WHERE game_name = ? AND game_status = 1 ORDER BY game_no";
			pstmt = con.prepareStatement(selectByNameSQL);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String game_no = rs.getString("game_no"); 
				int game_status = rs.getInt("game_status");
				String board_GameType = rs.getString("game_name"); 
				BoardGame game = new BoardGame(game_no, game_status, board_GameType);
				list.add(game);
			}
//			if(list.size()==0) {
//				throw new NotFoundException("데이터가 없습니다.");
//			}
			return list;
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		} finally {
			MyConnection.close(con, pstmt, rs);
		}
	}
	
	//보드게임 관리 추가 
	public void insert(BoardGame b) throws AddException{
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con=MyConnection.getConnection();
			
			String insertSQL=
					"INSERT INTO boardgame(game_name, game_no, game_status) VALUES (?,?,?)";
			try {
				pstmt=con.prepareStatement(insertSQL);
				pstmt.setString(1, b.getBoard_GameType());
				pstmt.setString(2, b.getGame_no());
				pstmt.setInt(3, b.getGame_status());
				pstmt.executeUpdate();
				
			}catch(SQLException e) {
				if(e.getErrorCode()==1)///1번이면 pk중복임!!
					throw new AddException("이미 존재하는 게임번호입니다.");
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
	//보드게임 삭제 
	public void delete(String no)throws ModifyException{
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con=MyConnection.getConnection();
			String deleteSQL1=" UPDATE boardgame SET game_status=2";
			String deleteSQL2=" WHERE game_no ='"+no+"'";
			
			String deleteSQL = "UPDATE boardgame SET game_status = 2 WHERE game_no = ?";
			pstmt = con.prepareStatement(deleteSQL);
			pstmt.setString(1, no);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModifyException(e.getMessage());
		}finally {
			MyConnection.close(con,pstmt,null);
		}
	}


	public void deleteBoardGame(BoardGame b) throws NotFoundException, ModifyException{
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			con=MyConnection.getConnection();
			
			String selectSQL = "SELECT game_status FROM boardgame WHERE game_no =?";
			pstmt2 = con.prepareStatement(selectSQL);
			pstmt2.setString(1, b.getGame_no());
			rs = pstmt2.executeQuery();
			
			if(!rs.next()) {
				throw new NotFoundException();
			} else {
				int status = rs.getInt(1);
				
				if(status != 1) {
					throw new ModifyException();
				}
			}
			pstmt2.close();
			
			String deleteSQL = "UPDATE boardgame SET game_status = 2 WHERE game_no = ?";
			pstmt = con.prepareStatement(deleteSQL);
			pstmt.setString(1, b.getGame_no());
			pstmt.executeUpdate();
				
			
			
		}
		catch (ModifyException e) {//이미 status가 2라서 삭제가 안되는 경우 예외처리 
			throw new ModifyException("이미 삭제된 게임입니다.");
		}catch (NotFoundException e) {
			throw new NotFoundException("게임번호가 존재하지 않아 삭제 불가합니다.");
		}catch (Exception e) {
			
		}finally {
			MyConnection.close(con,pstmt,null);
		}		
	}
}
