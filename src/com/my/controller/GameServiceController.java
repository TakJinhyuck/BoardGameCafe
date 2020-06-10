package com.my.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.service.CustomerService;
import com.my.service.GameServiceService;
import com.my.service.GameTypeService;
import com.my.session.Session;
import com.my.session.SessionSet;
import com.my.view.FailView;
import com.my.view.SuccessView;
import com.my.vo.BoardGame;
import com.my.vo.Customer;
import com.my.vo.GameService;
import com.my.vo.GameType;

public class GameServiceController {
	private static  GameServiceController instance = new GameServiceController();
	private GameServiceService gameService = GameServiceService.getInstance();
	private GameTypeService typeService = GameTypeService.getInstance();
	private CustomerService customerService = CustomerService.getInstance();
	private GameTypeService gameTypeService = GameTypeService.getInstance();

	private GameServiceController() {

	}
	public static GameServiceController getInstance() {
		return instance;
	}



	//서비스에서는 Session을 쓰지 않는 것이 관례이다.
	//id와 game_type을 받아서 서비스하나를 가져온다.
	public void rentGame(Session session, String game_type) {
		try {
			SessionSet ss = SessionSet.getInstance();			
			GameService currService = (GameService)session.getAttribute("currService");

			if(currService==null) {
				Customer customer =  customerService.findById(session.getSessionId()); // findCustomer 메소드 호출
				BoardGame game = useGame(game_type);
				GameService service = new GameService(customer, game);
				
				Calendar cal=Calendar.getInstance();
				Date start_time = cal.getTime();
				service.setStart_time(start_time);
				session.setAttribute("currService", service);
				service.setService_no((int)service.getStart_time().getTime());
				
				gameService.rentGame(service);
				ss.add(session);
			} else {
				throw new AddException("☆☆☆☆☆☆☆이미 이용 중인 게임이 있습니다.☆☆☆☆☆☆");
			}
			
		} catch (AddException e) {
			FailView.errorMessage(e.getMessage());
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		} catch (ModifyException e) {
			FailView.errorMessage(e.getMessage());
		}
	}

	public BoardGame useGame(String game_type) throws NotFoundException, ModifyException {

		GameType type;
		type = typeService.findByGameName(game_type).get(0);
		BoardGame game = null;

		List<BoardGame> list = type.getStock();
		if(list.size()==0) {
			throw new NotFoundException("☆☆☆☆☆☆☆재고가 없습니다☆☆☆☆☆☆");
		}
		game = list.remove(list.size()-1);
		type.setUse_cnt(type.getUse_cnt()+1);
		gameTypeService.modifyType(type);
		return game;
	}

	//게임 반납
	public void returnGame(Session session) {
		try {
			SessionSet ss = SessionSet.getInstance();
			//			Session session = ss.get(id);
			GameService service = (GameService)session.getAttribute("currService");
			if(service == null) {
				throw new ModifyException("☆☆☆☆☆☆☆이용 중인 게임이 없습니다☆☆☆☆☆☆");
			}
			Calendar cal=Calendar.getInstance();
			Date end_time = cal.getTime();
			service.setEnd_time(end_time);
			long start = service.getStart_time().getTime();
			long end = end_time.getTime();
			long mills= end - start;
			long min = mills/60000;
			int charge = (int)(min*100+500); 
			service.setCharge(charge);


			gameTypeService.updatePlusStock(service.getService_BoardGame().getBoard_GameType());
			gameService.returnGame(service);

			SuccessView.printReturn(charge, min);
			ss.remove(session);
		} catch (ModifyException e) {
			FailView.errorMessage(e.getMessage());
		} catch (Exception e) {
			FailView.errorMessage(e.getMessage());
		}
	}


	//날짜를 입력받아 해당 날짜의 서비스를 받아온다.
	public void findServiceByDate(String date) {
		try {
			List<GameService> list = new ArrayList<>();
			list = gameService.findServiceByDate(date);
			int totalCharge = 0;
			for(GameService g : list) {
				if(g.getCharge() != 0) {
				totalCharge += g.getCharge();
				} 
			}
			SuccessView.printServiceFindByDate(list,totalCharge);
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	public void showService(Session session) {
		
		SessionSet ss = SessionSet.getInstance();			
		GameService currService = (GameService)session.getAttribute("currService");

		if(currService==null) {
			FailView.errorMessage("*** 현재 이용 중인 서비스가 없습니다. ***");
		} else {
			Calendar cal=Calendar.getInstance();
			Date end_time = cal.getTime();
			
			long start = currService.getStart_time().getTime();
			long end = end_time.getTime();
			long mills= end - start;
			long min = mills/60000;
			int charge = (int)(min*100+500); 
			
			String game_name = currService.getService_BoardGame().getBoard_GameType();
			SuccessView.printService(currService.getStart_time(), min, charge, game_name);
		}
	}

//
//	//현재 이용 중인 모든 고객을 보여준다.
//	public void showAllSession(String id) {
//		try {
//			SessionSet ss = SessionSet.getInstance();
//			Session session = ss.get(id); 
//			HashSet<Session> set=(HashSet)session.getAttribute("currService");
//			if(set ==null) {
//				throw new NotFoundException("현재 이용중인 고객이 없습니다.");
//			}
//
//			SuccessView.printAllSession(set);
//		} catch (NotFoundException e) {
//			FailView.errorMessage(e.getMessage());
//		}
//
//	}
}
