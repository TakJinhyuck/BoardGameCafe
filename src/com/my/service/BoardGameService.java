package com.my.service;

import java.util.List;

import com.my.dao.BoardGameDAO;
import com.my.dao.GameTypeDAO;
import com.my.vo.*;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;

public class BoardGameService {
	
	private static BoardGameService instance = new BoardGameService();
	
	private BoardGameService() {
		
	}
	
	public static BoardGameService getInstance() {
		return instance;
	}
	
	public BoardGameDAO dao = new BoardGameDAO();
	
	public BoardGame findByGameNo(String no) throws NotFoundException {
		return dao.selectByNo(no);
	}
	
	
	public void addBoardGame(BoardGame b) throws AddException {
		dao.insert(b);
	}

	
	public List<BoardGame> selectAllBoardGame() throws NotFoundException{
		return dao.selectAllBoardGame();
	}

	public GameTypeDAO gametypedao = new GameTypeDAO();
	
	public void deleteBoardGame(BoardGame b) throws NotFoundException, ModifyException{
		dao.deleteBoardGame(b);
		
	}
}
