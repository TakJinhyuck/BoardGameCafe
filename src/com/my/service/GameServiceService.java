package com.my.service;

import java.util.List;

import com.my.dao.BoardGameDAO;
import com.my.dao.CustomerDAO;
import com.my.dao.GameServiceDAO;
import com.my.dao.GameTypeDAO;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.vo.BoardGame;
import com.my.vo.Customer;
import com.my.vo.GameService;
import com.my.vo.GameType;

public class GameServiceService {
	
	private static GameServiceService instance = new GameServiceService();
	private GameServiceDAO dao = new GameServiceDAO();
	private GameServiceService() {}
	private CustomerDAO c_dao = new CustomerDAO();
	private BoardGameDAO d_dao = new BoardGameDAO();
	private GameTypeDAO g_dao = new GameTypeDAO();
	
	
	public static GameServiceService getInstance() {
		return instance;
	}

	//서비스 하나를 시작하고, 세션에 게임을 반영한다.
	public void rentGame(GameService gameService) throws AddException {

		dao.insert(gameService);	
		
	}

	public void returnGame(GameService service) throws ModifyException {
		dao.update(service);
		
	}
	
	//날짜를 입력받아 해당 날짜에 해당되는 서비스들을 검색해서 반환한다.
	public List<GameService> findServiceByDate(String date) throws NotFoundException{
		List<GameService> list = dao.selectByDate(date);
		
		for(GameService g : list) {
			String id =g.getService_customer().getId();
			Customer customer = c_dao.selectById(id);
			g.setService_customer(customer);
			
			BoardGame boardGame = d_dao.selectByNo(g.getService_BoardGame().getGame_no());
			g.setService_BoardGame(boardGame);
		}
		
		return list;
	}
	

}
