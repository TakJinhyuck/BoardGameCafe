package com.my.vo;

import java.util.ArrayList;
import java.util.List;

public class GameType {
	private String game_type;
	private List<BoardGame> stock;
	private int quantity;
	private int max_p;
	private int min_p;
	private float game_level;
	private int playtime;
	private String usage;
	private int use_cnt;
	public GameType() {
		stock = new ArrayList<>();
	}

	
	
	public GameType(String game_type, List<BoardGame> stock, int quantity, int use_cnt) {
		super();
		this.game_type = game_type;
		this.stock = stock;
		this.quantity = quantity;
		this.use_cnt = use_cnt;
	}



	public GameType(String game_type, List<BoardGame> stock, int quantity, int max_p, int min_p, float game_level,
			int playtime, String usage, int use_cnt) {
		super();
		this.game_type = game_type;
		this.stock = stock;
		this.quantity = quantity;
		this.max_p = max_p;
		this.min_p = min_p;
		this.game_level = game_level;
		this.playtime = playtime;
		this.usage = usage;
		this.use_cnt = use_cnt;
	}



	public String getGame_type() {
		return game_type;
	}


	public void setGame_type(String game_type) {
		this.game_type = game_type;
	}


	public List<BoardGame> getStock() {
		return stock;
	}


	public void setStock(List<BoardGame> stock) {
		this.stock = stock;
	}


	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public int getMax_p() {
		return max_p;
	}


	public void setMax_p(int max_p) {
		this.max_p = max_p;
	}


	public int getMin_p() {
		return min_p;
	}


	public void setMin_p(int min_p) {
		this.min_p = min_p;
	}


	public float getGame_level() {
		return game_level;
	}


	public void setGame_level(float game_level) {
		this.game_level = game_level;
	}


	public int getPlaytime() {
		return playtime;
	}


	public void setPlaytime(int playtime) {
		this.playtime = playtime;
	}


	public String getUsage() {
		return usage;
	}


	public void setUsage(String usage) {
		this.usage = usage;
	}


	public int getUse_cnt() {
		return use_cnt;
	}


	public void setUse_cnt(int use_cnt) {
		this.use_cnt = use_cnt;
	}


	@Override
	public String toString() {
		return "보드게임 [이름=" + game_type + ", 재고=" + stock.size()+"개" + ", 수량=" + quantity+"개" + ", 최대인원=" + max_p+"명"
				+ ", 최소인원=" + min_p+"명" + ", 난이도=" + game_level + ", 플레이시간=" + playtime+"분" + ", 방법=" + usage
				+ ", 이용횟수=" + use_cnt+"번" + "]";
	}
	
}
