package com.my.controller;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.service.GameTypeService;
import com.my.view.FailView;
import com.my.view.SuccessView;
import com.my.vo.BoardGame;
import com.my.vo.GameType;
import com.my.vo.GameService;
public class GameTypeController {
	private static GameTypeController controller = new GameTypeController();
	public static GameTypeController getInstance() {
		return controller;
	}
	GameTypeService game_service = GameTypeService.getInstance();
	/**
	 * 게임전체조회
	 * 사용 랭킹순으로 정렬
	 */
	public void findAll() {
		try {
			
			List<GameType> gameList = new ArrayList<>();
					
			gameList = game_service.findAll();
			SuccessView.printAllGame(gameList);
		}catch(NotFoundException e) {
			FailView.errorMessage("조회할 수 없습니다.");
		}
	}
	/**
	 * 게임이름을 검색, 게임이름의 키워드로 검색하면 중복된 키워드가 있는 게임이 리스트로 리턴
	 * @param game_type
	 */
	public void searchByGameName(String game_type, String id) {
		try {
			List<GameType> gametype = game_service.findByGameName(game_type);
			SuccessView.printByGameName(gametype,id);
		}catch(NotFoundException e) {
			FailView.errorMessage("게임이름이 존재하지 않습니다.");
		}
	}
	/**
	 * 게임이름으로 검색, 게임타입으로 출
	 * @param name
	 */
	public void searchByGameName2(String name, String id) {
		try {
			
			List<GameType> gameList = new ArrayList<>();	
			gameList = game_service.selectByName(name);
			SuccessView.printGamesByName(gameList,id);
		}catch(NotFoundException e) {
			FailView.errorMessage("게임이름이 존재하지 않습니다.");
		}
	}
	/**
	 * 인원수로 검색
	 * @param person_no
	 */
	public void findByPlayPerson(int person_no, String id) {
		try {
			List<GameType> gameList = new ArrayList<>();
			gameList = game_service.findByPlayPerson(person_no);
			SuccessView.printGameByPlayPerson(gameList,id);
		}catch(NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
	}
	   /**
	    * 게임 난이도로 찾기
	    * @param game_level
	    */
	   public void findByLevel(float game_level, String id) {
	      List<GameType> gameList = new ArrayList<>() ;
	      try {
	    	 gameList = game_service.findByLevel(game_level);
	         SuccessView.printGamesByLevel(gameList,id);
	      } catch (NotFoundException e) {
	         FailView.errorMessage("해당하는 게임 난이도를 찾을 수 없습니다."+e.getMessage());
	      }
	}
	   /**
	    * 플레이 시간으로 찾기
	    * @param playtime
	    */
	   public void findByPlayTime(int playtime, String id) {
	      List<GameType> gameList = new ArrayList<>();
	      try {
	    	  
	    	 
	         gameList = game_service.findByPlayTime(playtime);
	         SuccessView.printgamesByPlayTime(gameList,id);
	      } catch (NotFoundException e) {
	         FailView.errorMessage("해당하는"+playtime+"시간의 게임을 찾을 수 없습니다."+e.getMessage());
	      }
	   }
	   
	   /**재고 수정하기
		 * 수량과 사용횟수는DAO에서 가져오기  
		 */
		public void modifyQuantity(GameType g) {
			try {
				game_service.modifyType(g);
				SuccessView.printModifyGameType("수정되었습니다.");
			}catch(ModifyException e) {
				e.printStackTrace();
				FailView.errorMessage(e.getMessage());
			}
		}
	//게임 추가 
		public void registar(GameType g) {
			try {
				game_service.registar(g);
				SuccessView.insertGameType("추가했습니다.");
			}catch(AddException e) {
				FailView.errorMessage(e.getMessage());
			}
		}
		//이름으로 대여
		public void getUsage(String game_name) {
			BufferedReader br = null;
			try {
				GameType type = game_service.findByGameName(game_name).get(0);
				String usage = type.getUsage();
				
				br = new BufferedReader(new FileReader("src/BoardGameUsage/"+usage));
				
				String result = "";
				int i = 0;
				int cnt = 0;
				while((i = br.read())!=-1) {
					result += (char)i;
					cnt++;
					if(cnt%60==0) {
						result += "\n";
					}
					
				}
				SuccessView.printUsage(result);
				
				
			} catch (NotFoundException e) {
				FailView.errorMessage(e.getMessage());
			} catch (FileNotFoundException e) {
				
				FailView.errorMessage(e.getMessage());
			} catch (IOException e) {
				FailView.errorMessage(e.getMessage());
			} finally {
				try {
					if(br!=null)br.close();
					
				} catch(IOException e) {
					FailView.errorMessage(e.getMessage());
				}
			}
			
		}
}