package com.my.service;
import java.util.ArrayList;
import java.util.List;

import com.my.dao.BoardGameDAO;
import com.my.dao.GameTypeDAO;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.vo.BoardGame;
import com.my.vo.GameType;
public class GameTypeService {
	private static GameTypeService game_service = new GameTypeService();

	public static GameTypeService getInstance() {
		return game_service;
	}
	private GameTypeDAO dao;
	private BoardGameDAO gameDAO = new BoardGameDAO();
	private GameTypeService() {
		dao = new GameTypeDAO();
	}
	/**
	 * 전체조회 
	 * @return List<GameType>
	 * @throws NotFoundException
	 */
	public List<GameType> findAll()throws NotFoundException{
		List<GameType> Typelist = new ArrayList<>();

		Typelist = dao.selectAll();
		for(GameType g : Typelist) {
			List<BoardGame> gameList = new ArrayList<>();
			gameList = gameDAO.selectByName(g.getGame_type());
			g.setStock(gameList);
		}
		return Typelist;
	}
	/**
	 * 게임 이름으로 찾기 --게임명에 글자가 동일한 게임이 다수면 리스트 형태로 리턴하려고 만듬 
	 * @param game_type
	 * @return List<GameType>
	 * @throws NotFoundException
	 */
	public List<GameType> findByGameName(String game_type) throws NotFoundException{
		List<GameType> Typelist = new ArrayList<>();

		Typelist = dao.selectByName(game_type);
		for(GameType g : Typelist) {
			List<BoardGame> gameList = new ArrayList<>();
			gameList = gameDAO.selectByName(g.getGame_type());
			g.setStock(gameList);
		}
		return Typelist;
	}
	/**
	 * 게임 이름으로 찾기2 
	 * @param name
	 * @return GameType
	 * @throws NotFoundException
	 */
	public List<GameType> selectByName(String name) throws NotFoundException{
		List<GameType> Typelist = new ArrayList<>();

		Typelist = dao.selectByName(name);
		for(GameType g : Typelist) {
			List<BoardGame> gameList = new ArrayList<>();
			gameList = gameDAO.selectByName(g.getGame_type());
			g.setStock(gameList);
		}
		return Typelist;
	}
	/**
	 * 게임 참여인원수로 찾기 
	 * @param person_no
	 * @return List<GameType> 
	 * @throws NotFoundException
	 */
	public List<GameType> findByPlayPerson(int person_no)throws NotFoundException{
		List<GameType> Typelist = new ArrayList<>();

		Typelist = dao.selectByPlayPerson(person_no);
		for(GameType g : Typelist) {
			List<BoardGame> gameList = new ArrayList<>();
			gameList = gameDAO.selectByName(g.getGame_type());
			g.setStock(gameList);
		}
		return Typelist;
	}
	/**
	 * 게임 난이도로 찾기
	 * @param game_level
	 * @return
	 * @throws NotFoundException
	 */
	public List<GameType> findByLevel(float game_level) throws NotFoundException {

		List<GameType> Typelist = new ArrayList<>();

		Typelist = dao.selectByLevel(game_level);
		for(GameType g : Typelist) {
			List<BoardGame> gameList = new ArrayList<>();
			gameList = gameDAO.selectByName(g.getGame_type());
			g.setStock(gameList);
		}
		return Typelist;

	}

	/**
	 * 플레이 시간으로 찾기
	 * @param playtime
	 * @return
	 * @throws NotFoundException
	 */
	public List<GameType> findByPlayTime(int playtime) throws NotFoundException {
		List<GameType> Typelist = new ArrayList<>();

		Typelist = dao.selectByTime(playtime);
		for(GameType g : Typelist) {
			List<BoardGame> gameList = new ArrayList<>();
			gameList = gameDAO.selectByName(g.getGame_type());
			g.setStock(gameList);
		}
		return Typelist;
	}

	/**
	 * 재고, 수량 사용횟수 수정하기 
	 */
	public void modifyType(GameType g)throws ModifyException{
		dao.update(g);
	}
	/**
	 * 게임추가 
	 */
	public void registar(GameType g)throws AddException{
		dao.insert(g);
	}
	/**
	 * 제고/수량 증가
	 * @param board_GameType
	 * @throws ModifyException
	 */
	public void plusStockQuentity(String board_GameType) throws ModifyException{

		// TODO Auto-generated method stub
		dao.updateStockQuentity(board_GameType);
	}
	
	public void updatePlusStock(String board_GameType) throws ModifyException{
		dao.updatePlusStock(board_GameType);
	}
	public void minusStockQuentity(String board_GameType) throws Exception{
		dao.updateMinusStockQuentity(board_GameType);
		
	}

}