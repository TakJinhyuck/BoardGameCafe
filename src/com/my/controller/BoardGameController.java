package com.my.controller;

import java.util.ArrayList;
import java.util.List;

import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.service.BoardGameService;
import com.my.service.GameTypeService;
import com.my.view.FailView;
import com.my.view.SuccessView;
import com.my.vo.*;
public class BoardGameController {
	private BoardGameService board_service = BoardGameService.getInstance();
	private GameTypeService type_service = GameTypeService.getInstance();
	
	private static BoardGameController instance = new BoardGameController();
	
	private BoardGameController() {
		
	}
	
	public static BoardGameController getInstance() {
		return instance;
	}
	
	public BoardGame findByNo(String id) {
		try {
			return board_service.findByGameNo(id);
		} catch (NotFoundException e) {
			FailView.errorMessage(e.getMessage());
		}
		return null;
	}
	
	
	public void findAllBoardGame() {
		try {
			List<BoardGame> list = board_service.selectAllBoardGame();
			for(BoardGame b : list) {
				System.out.println(b);
			}
		}catch(NotFoundException e) {
			e.printStackTrace();
			FailView.errorMessage(e.getMessage());
		}
	}
	
	public void addBoardGame(BoardGame b) {
		try {
			board_service.addBoardGame(b);
			//BoardTypeService bts = new BoardTypeService();
			//bts.plusStock(b.getBoard_GameType()); //재고수량 1증가
			//GameType type = type_service.findByGameName(b.getBoard_GameType()).get(0);
			
			//GameType temp = new GameType();
			//List<BoardGame> list = type.getStock();
			//희
			//if(list==null) {
			//	 list = new ArrayList<>();
			//}
			
			
			//temp.setStock(list);
			//temp.setQuantity(type.getQuantity()+1);
			
			//temp.setGame_type(type.getGame_type());
			
			//type_service.modifyType(temp);
			//재고 수량 증가시키기
			type_service.plusStockQuentity(b.getBoard_GameType());
			
			 SuccessView.printModify("추가 되었습니다.");
		}  catch (AddException e) {
			FailView.errorMessage("게임을 추가하지 못했습니다."+e.getMessage());
		}  catch (ModifyException e) {
			FailView.errorMessage("게임을 추가하지 못했습니다."+e.getMessage());
		}  catch (Exception e) {
			FailView.errorMessage("게임을 추가하지 못했습니다."+e.getMessage());
		}
	}

	public void deleteBoardGame(BoardGame b) {
		try {
			
			GameType type = type_service.findByGameName(b.getBoard_GameType()).get(0);
			type.setQuantity(type.getQuantity()-1);
			List<BoardGame> list = type.getStock();
			if(list.size() == 0) {
				FailView.errorMessage("삭제할 게임의 재고가 없습니다.");
			} else {
				list.remove(list.size()-1);
			}
			type_service.modifyType(type);
			board_service.deleteBoardGame(b);
			
			//type_service.minusStockQuentity(b.getBoard_GameType());
			
			SuccessView.printModify("보드게임이 삭제되었습니다.");
			
		}catch(NotFoundException e){
			FailView.errorMessage("삭제하려는 게임이 존재하지 않습니다.");
		}catch (ModifyException e) {
			FailView.errorMessage("이미 삭제된 게임입니다.");
		}catch (Exception e) {
			FailView.errorMessage("게임을 삭제하지 못했습니다."+e.getMessage());
		}

	}
}
