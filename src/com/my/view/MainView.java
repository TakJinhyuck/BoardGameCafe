package com.my.view;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.my.controller.BoardGameController;
import com.my.controller.CustomerController;
import com.my.controller.GameServiceController;
import com.my.controller.GameTypeController;
import com.my.dao.CustomerDAO;
import com.my.exception.NotFoundException;
import com.my.session.Session;
import com.my.session.SessionSet;
import com.my.vo.BoardGame;
import com.my.vo.Customer;
import com.my.vo.GameType;
public class MainView {
	static Scanner sc = new Scanner(System.in);
	//메인 메뉴를 화면에 출력

	/**
	 * 초기 메뉴
	 */
	public static void printMenu() {
		System.out.println("=====================KOSTA Board Game Cafe==================");
		System.out.println("============================================================");
		System.out.println("                   1.로그인  2.가입  9.종료");
		System.out.println("============================================================");
		
		
	}

	//로그인 작업
	public static void login() {
		System.out.println("==========로그인==========");
		System.out.print("ID:");
		sc.nextLine();
		String id = sc.nextLine();
		System.out.print("비밀번호:");
		String pwd = sc.nextLine();
		CustomerController customerController = CustomerController.getInstance();
		customerController.login(id, pwd);
		SessionSet ss = SessionSet.getInstance();
		Session session = ss.get(id);
		if(session != null) { //로그인성공상태
			if("admin".equals(id)) {
				//관리자 모드
				String job;
				do {
					MenuView.printAdminMenu();
					job = sc.nextLine();
					switch(job) {
					case "1":
						System.out.println("============회원관리============");
						System.out.println("1.이름검색, 2.아이디검색 , 3.전체 검색");
						String menu;
						menu = sc.nextLine();
						switch (menu) {
						case "1": findByCustomerName(); //이름으로 검색

						break;

						case "2": findByCustomerId(); //아이디로 검색
						break;

						case "3" : findByAllCustomer(); //전체검색
						
						break;
						}
						sc.nextLine();
						break;
					case "2"://보드게임관리 
						MenuView.printGameManagement();
						//1.게임 종류추가, 2.게임 추가, 3.게임 검색, 4.게임 수정, 5.게임삭제
						String board = sc.nextLine();
						switch(board) {
						case "1" : //보드게임 추가(BoardGame) 
							addBoardGame();
							break;
						case "2" ://게임정보 추가(GameType) 
							addGameType();
							break;
						case "3" ://게임 검색 (BoardGame), 관리자가 전체게임 조회 
							findByAllBoardGame();
							break;
						case "4" ://게임 삭제(BoardGame)
							deleteBoardGame();
							break;
						case "5" : findAll();
						}
						break;
						case "3": //정산
							adjustment();
							break;
//						case "4":showUsers(id);
//						break;
						case "9"://관리자모드 나가기
							customerController.logout(id);
							break;
						}
					}while(!job.equals("9"));
				}else {
					//사용자 모드
					String menu;
					do {//반복
						
						MenuView.printLoginMenu();
						menu = sc.nextLine();
						switch(menu) {
						case "1"://게임찾기
							MenuView.printGameSearchMenu();
							String job = sc.nextLine();
							if("1".equals(job)) {
								searchByGameName(id);//이름으로검색
							}else if("2".equals(job)) {
								findByLevel(id);//난이도검색
							}else if("3".equals(job)) {
								searchByPlayPerson(id);//인원검색
							}else if("4".equals(job)) {
								findByPlayTime(id);//플레이 시간검색
							}else if("5".equals(job)) {
								findAll();//순위표기
							}else if("9".equals(job)) {
							}
							sc.nextLine();
							break;
						case "2"://반납
							returnGame(id);
							sc.nextLine();
							return;
						case "3": //로그아웃
							
							returnGame(id);
							customerController.logout(id);
							
							break;
						case "4"://내정보보기
							System.out.println("===================="+id+"고객님 정보=================");
							customerController.searchById(id);//내정보보기
							System.out.println("※ 회원 = 1, 비회원(탈퇴) = 2");
							String menu2 = null;
							do {
								MenuView.printUserMenu();
								menu2 = sc.nextLine();
								switch (menu2) {
								case "1" : update(id); //정보수정
								System.out.println("=================수정된 정보================");
								customerController.searchById(id);//내정보보기
								System.out.println("※ 회원 = 1, 비회원(탈퇴) = 2");
								break;

								case "2" : deleteCustomer(id); //탈퇴
								customerController.logout(id);
								return;
								case "9" : //나가기
									break;
								}
							}while(!"9".equals(menu2));
							break;
						case "5" : //이용 중인 서비스 및 요금 보기
							showService(session);
							break;
						}
					}while(!"3".equals(menu));
				}
			}
		}
	
		public static void showService(Session session) {
			System.out.println("=================서비스 정보================");
			
			GameServiceController controller = GameServiceController.getInstance();
			controller.showService(session);
			System.out.println("=========================================");
		}
	
		//4.보드게임 수량 수정 
		public static void modifyQuantity(String game_type) {

			System.out.println("수정할 수량 : ");
			int quantity = sc.nextInt();

			GameType g = new GameType();
			g.setGame_type(game_type);
			g.setQuantity(quantity);

			GameTypeController controller = GameTypeController.getInstance();
			controller.modifyQuantity(g);

		}


		//보드게임 전체보기 
		public static void findByAllBoardGame() {
			BoardGameController controller = BoardGameController.getInstance();
			controller.findAllBoardGame();
		}

		//게임타입 기본정보 추가, stock는 처음에 수량을 받지말고, 게임이름이 같은 boardgame이 insert되면수량이 늘도록 설정 
		public static void addGameType() {
			GameTypeController controller = GameTypeController.getInstance();
			//게임 [종류=" + game_type + ", 재고=" + stock.size() + ", 수량=" + quantity + ", 최대인원=" + max_p
			//+ ", 최소인원=" + min_p + ", 난이도=" + game_level + ", 플레이시간=" + playtime + ", 방법=" + usage
			//+ ", use_cnt=" + use_cnt + "]"
			System.out.println("게임이름 : ");
			String game_type = sc.next();
			System.out.println("최대인원 : ");
			int max_p = sc.nextInt();
			System.out.println("최소인원 : ");
			int min_p = sc.nextInt();
			System.out.println("난이도 : ");
			float game_level = sc.nextFloat();
			System.out.println("플레이시간 : ");
			int playtime = sc.nextInt();
			GameType g = new GameType();
			g.setGame_type(game_type);
			g.setMax_p(max_p);
			g.setMin_p(min_p);
			g.setGame_level(game_level);
			g.setPlaytime(playtime);
			controller.registar(g);

			//		   int stock = 5;
			//		   List<GameType> list = new ArrayList<>();
			//		   for(int i=0; i<stock; i++)
			//		   {
			//			   list.add(new BoardGame());
			//		   }
			//		   GameType g = new GameType();
			//		   g.setStock(list);
			//		   controller.registar(g);
		}


		//보드게임 추가
		public static void addBoardGame() {

			System.out.println("게임이름 : ");
			String Board_GameType = sc.next();
			System.out.println("게임상태 : ");
			int game_status = sc.nextInt();
			System.out.println("게임 관리번호 : (ex.루미01)");
			String game_no = sc.next();

			BoardGame b = new BoardGame();
			//GameType g = new GameType();
			b.setBoard_GameType(Board_GameType);
			b.setGame_status(game_status);
			b.setGame_no(game_no);


			BoardGameController bc = BoardGameController.getInstance();
			bc.addBoardGame(b);
		}
		
		public static void deleteBoardGame() {
			System.out.println("수정할 게임 번호 : ");
			String game_no = sc.next();
			BoardGameController controller = BoardGameController.getInstance();
			
			BoardGame b = controller.findByNo(game_no);
			controller.deleteBoardGame(b);
		}

		private static void update(String id) {
			System.out.println("***수정작업***");
			System.out.print("비밀번호:");
			String pwd =sc.nextLine();
			System.out.print("이름:");
			String name =sc.nextLine();
			Customer c = new Customer();
			c.setId(id);
			if(!"".equals(pwd)) {
				c.setPwd(pwd);
			}
			if(!"".equals(name)) {
				c.setName(name);
			}
			CustomerController controller = CustomerController.getInstance();
			controller.modify(c);
		}
		/**
		 * 게임이름 검색 작업
		 */
		private static void searchByGameName(String id) {
			System.out.println("***게임이름 검색***");
			System.out.print("게임이름:");
			String name = sc.nextLine();
			GameType g = new GameType();
			g.setGame_type(name);
			GameTypeController gameTypeController = GameTypeController.getInstance();
			gameTypeController.searchByGameName(name,id);
			//gameTypeController.getUsage(name);
			// rentGame();

		}
		/**
		 * 난이도 검색 작업
		 *
		 */
		private static void findByLevel( String id) {
			System.out.println("***난이도별 게임 검색***");
			System.out.println("난이도:");
			int level = sc.nextInt();
			GameType g = new GameType();
			g.setGame_level(level);
			GameTypeController gameTypeController = GameTypeController.getInstance();
			gameTypeController.findByLevel(level,id);

		}

		/**
		 * 인원 검색 작업
		 */
		private static void searchByPlayPerson( String id) {
			System.out.println("***인원으로 게임 검색***");
			System.out.println("플레이인원:");
			int person = sc.nextInt();
			GameType g = new GameType();
			g.setMax_p(person);g.setMin_p(person);
			GameTypeController gameTypeController = GameTypeController.getInstance();
			gameTypeController.findByPlayPerson(person,id);


		}

		/**
		 * 소요 시간으로 검색 작업
		 */
		private static void findByPlayTime( String id) {
			System.out.println("***소요시간으로 게임 검색***");
			System.out.println("소요시간:");
			int time = sc.nextInt();
			GameType g = new GameType();
			g.setPlaytime(time);
			GameTypeController gameTypeController = GameTypeController.getInstance();
			gameTypeController.findByPlayTime(time,id);

		}

		/**
		 * 사용 랭킹순으로 검색 작업
		 */
		private static void findAll() {
			//System.out.println("***게임 인기 랭킹***");
			GameTypeController gameTypeController = GameTypeController.getInstance();
			gameTypeController.findAll();
		}
		//회원 가입 작업
		public static void register() {
			System.out.println("==가입작업==");
			System.out.print("ID:");
			sc.nextLine();
			String id = sc.nextLine();
			System.out.print("이름:");
			String name = sc.nextLine();
			System.out.print("비밀번호:");
			String pwd = sc.nextLine();
			Customer c = new Customer();
			c.setId(id); c.setName(name); c.setPwd(pwd);
			CustomerController customerController = CustomerController.getInstance();
			customerController.register(c);
		}
		/*게임 찾기 과정
		 * 모든 게임 보기
		 * 이름으로 찾기
		 * 난이도로 찾기
		 * 최대 소요시간으로 찾기
		 * 순위표에서 고르기
		 * 회원 탈퇴
		 */

		//대여
		public static void rentGame(String id) {
			int number = sc.nextInt();
			switch(number) {
			case 1:
				System.out.print("대여하실 게임이름을 입력하시오 : ");
				String name = sc.next();
				SessionSet ss= SessionSet.getInstance();
				Session session= new Session();
				session= ss.get(id);
				GameServiceController test = GameServiceController.getInstance();
				test.rentGame(session, name);
				GameTypeController gameTypeController = GameTypeController.getInstance();
				gameTypeController.getUsage(name);
				break;
			case 2:
				break;
			}
		}



		//반납
		public static void returnGame(String id) {
			SessionSet ss= SessionSet.getInstance();
			Session session= new Session();
			session= ss.get(id);

			GameServiceController test = GameServiceController.getInstance();
			test.returnGame(session);

		}


		/*
		 * 
		 * 관리자 메뉴
		 * 회원 관리, 보드게임 관리, 정산
		 */
		//회원 관리
		public static void findByAllCustomer() {
			System.out.println("======전체검색=====");
			CustomerController controller= CustomerController.getInstance();
			controller.selectAll();
			System.out.println("※ 회원 = 1, 비회원(탈퇴) = 2");
		}
		//회원관리
		public static void findByCustomerId() {
			System.out.println("***ID로 검색 작업***");
			System.out.println("ID:");
			String id1 = sc.nextLine();
			CustomerController customerController = CustomerController.getInstance();
			customerController.searchById(id1);
		}
		//이름으로 검색
		public static void findByCustomerName() {
			System.out.println("***이름으로 검색 작업***");
			System.out.print("이름 입력 : ");
			String name = sc.next();
			CustomerController customerController = CustomerController.getInstance();
			customerController.searchByName(name);
		}


		//회원삭제
		public static void deleteCustomer(String id) {
			System.out.println("====회원 탈퇴=====");
			CustomerController controller = CustomerController.getInstance();
			controller.deleteCustomer(id);
		}
//
//		//유저보기
//		public static void showUsers(String id) {
//			System.out.println("====현재 이용중인 고객 List=======");
//			GameServiceController controller = GameServiceController.getInstance();
//			controller.showAllSession(id);
//
//
//		}
		//보드게임 관리
		public static void modifyGame(BoardGame game) {
		}
		public static void deleteGame(int gameNumber) {
		}


		//정산
		public static void adjustment() {//정산
			System.out.println("============정산하기============");
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date currentTime = new Date();
//			String date = sdf.format(currentTime);
//			System.out.println(date);
			System.out.print("조회하고 싶은 날짜를 입력해주세요(yyyy-MM-dd)>");
			String date = sc.nextLine();
			GameServiceController controller = GameServiceController.getInstance();
			controller.findServiceByDate(date);
		}
		public static void main(String[] args) {
			boolean run = true;
			while(run) {
				printMenu();
				switch (sc.nextInt()) {
				case 1:
					login();
					break;
				case 2:
					register();
					break;
				case 9:
					run= false;
					System.out.println("시스템을 종료합니다.");
					break;
				default: System.out.println("잘못된 입력입니다.");
				}
			}
		}
	}