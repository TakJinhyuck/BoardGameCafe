package com.my.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.my.controller.GameServiceController;
import com.my.session.Session;
import com.my.vo.Customer;
import com.my.vo.GameService;
import com.my.vo.GameType;

public class SuccessView {

	
	
	public static void printByGameName(List<GameType> gametype, String id) {
	      int i = 1;
	      for(GameType g : gametype) {
	         System.out.println("[" + i + "] " + g);
	         i++;
	      }
	      	MenuView.printRentalMenu();
			 MainView.rentGame(id);
	   }

	public static void printGamesByName(List<GameType> gameList, String id) {
		int i = 1;
		for(GameType g : gameList) {
			System.out.println("[" + i + "] " + g);
			i++;
		}		
		MenuView.printRentalMenu();
		 MainView.rentGame(id);
	}

	public static void printGameByPlayPerson(List<GameType> gameList, String id) {
		int i = 1;
		for(GameType g : gameList) {
			System.out.println("[" + i + "] " + g);
			i++;
		}
		MenuView.printRentalMenu();
		MainView.rentGame(id);
		
	}

	public static void printGamesByLevel(List<GameType> gameList, String id) {
		int i = 1;
		for(GameType g : gameList) {
			System.out.println("[" + i + "] " + g);
			i++;
		}
		MenuView.printRentalMenu(); //대여하기 메뉴뷰
		 MainView.rentGame(id);//대여
		
	}

	public static void printgamesByPlayTime(List<GameType> gameList, String id) {
		int i = 1;
		for(GameType g : gameList) {
			System.out.println("[" + i + "] " + g);
			i++;
		}
		MenuView.printRentalMenu();
		MainView.rentGame(id);
		
		
	}

	public static void printAllGame(List<GameType> gameList) {
		int i = 1;
		for(GameType g : gameList) {
			System.out.println("[" + i + "] " + g);
			i++;
		}
		
				
	}

	public static void printLogin(String msg) {
		System.out.println(msg);
		
	}

	public static void printRegisterCustomer(String msg) {
		System.out.println(msg);
	}

	public static void printModifyCustomer(String msg) {
		System.out.println(msg);
	}

	public static void printByCustomerName(List<Customer> list) {
		for(Customer c : list) {
			System.out.println(c);
			System.out.println("※ 회원 = 1, 비회원(탈퇴) = 2");
		}
	}

	public static void printById(Customer customer) {
		System.out.println(customer);
		System.out.println("※ 회원 = 1, 비회원(탈퇴) = 2");
	}

	

	public static void printAllCustomer(List<Customer> list) {
		for(Customer c : list) {
			System.out.println(c);
		}
		
	}

	public static void printServiceFindByDate(List<GameService> list, int totalCharge) {
		int i = 1;
		for(GameService s : list) {
			System.out.println("[" + i + "] " + s);
			i++;
		}		
		System.out.println("전체 수익 : " + totalCharge);
	}

	public static void printAllSession(HashSet<Session> set) {
		
		System.out.println("현재 이용중인 고객과 서비스");
		Iterator<Session> iter = set.iterator();
		while(iter.hasNext()) {
			Session s = iter.next();
			GameService service = (GameService)s.getAttribute("currService");
			String game_name = service.getService_BoardGame().getBoard_GameType();
			String game_no = service.getService_BoardGame().getGame_no();
			System.out.println("고객 ID : " + s.getSessionId() + "게임이름 : " + game_name + "게임 번호" + game_no );
		}
		
	}

	public static void printModify(String msg) {
		System.out.println(msg);		
	}

	public static void printModifyGameType(String msg) {
		System.out.println(msg);
	}

	public static void insertGameType(String msg) {
		System.out.println(msg);
		
	}

	public static void printUsage(String usage) {
		System.out.println("************게임 방법**********");
		System.out.println(usage);
	}
	
	public static void printReturn(int charge, long min) {
		System.out.println("--------------------------");
		System.out.println("반납성공!");
		System.out.println("--------------------------");
		System.out.println("카운터에서 직원에게 계산해주세요.");
		System.out.println("--------------------------");
		System.out.print("이용시간: ");
		System.out.println(min+"분");
		System.out.println();
		System.out.print("요금: ");
		System.out.println(charge);
		System.out.println("--------------------------");
		
			
	}

	public static void printService(Date start, long min, int charge, String game_name) {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd / hh시 mm분 ss초");
		System.out.println("시작 시간: " + sdf.format(start));
		System.out.println("이용 시간: " + min + "분");
		System.out.println("이용 중인 게임 " + game_name);
		System.out.println("현재 요금 :" + charge);
		
	}

}
