package com.my.vo;

public class BoardGame {
	/**
	 * game_no, game_status, game_name
	 */
	private String game_no;
	private int game_status;
	private String Board_GameType; //게임이름
	
	public BoardGame() {
		
	}

	
	
	public BoardGame(String game_no, int game_status, String board_GameType) {
		super();
		this.game_no = game_no;
		this.game_status = game_status;
		Board_GameType = board_GameType;
	}



	public String getGame_no() {
		return game_no;
	}

	public void setGame_no(String game_no) {
		this.game_no = game_no;
	}




	public int getGame_status() {
		return game_status;
	}




	public void setGame_status(int game_status) {
		this.game_status = game_status;
	}




	public String getBoard_GameType() {
		return Board_GameType;
	}




	public void setBoard_GameType(String board_GameType) {
		Board_GameType = board_GameType;
	}




	@Override
	public String toString() {
		return "보드게임 [번호=" + game_no + ", 상태=" + game_status + ", 이름="
				+ Board_GameType + "]";
	}
	
	
}
