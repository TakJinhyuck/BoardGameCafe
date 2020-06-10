package com.my.vo;

import java.util.Date;

public class GameService {
	private int service_no;
	private Date use_date;
	private Customer service_customer; //손님 id
	private BoardGame service_BoardGame; //보드게임 번호
	private int charge; //비용
	private Date start_time;
	private Date end_time;
	
	public GameService() {
	
	}

	
	public GameService(int service_no, Date use_date, Customer service_customer, BoardGame service_BoardGame,
			int charge, Date start_time, Date end_time) {
		super();
		this.service_no = service_no;
		this.use_date = use_date;
		this.service_customer = service_customer;
		this.service_BoardGame = service_BoardGame;
		this.charge = charge;
		this.start_time = start_time;
		this.end_time = end_time;
	}



	public GameService(Customer customer, BoardGame game) {
		service_customer = customer;
		service_BoardGame = game;
	}


	public int getService_no() {
		return service_no;
	}



	public void setService_no(int service_no) {
		this.service_no = service_no;
	}



	public Date getUse_date() {
		return use_date;
	}



	public void setUse_date(Date use_date) {
		this.use_date = use_date;
	}



	public Customer getService_customer() {
		return service_customer;
	}



	public void setService_customer(Customer service_customer) {
		this.service_customer = service_customer;
	}



	public BoardGame getService_BoardGame() {
		return service_BoardGame;
	}



	public void setService_BoardGame(BoardGame service_BoardGame) {
		this.service_BoardGame = service_BoardGame;
	}



	public int getCharge() {
		return charge;
	}



	public void setCharge(int charge) {
		this.charge = charge;
	}



	public Date getStart_time() {
		return start_time;
	}



	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}



	public Date getEnd_time() {
		return end_time;
	}



	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}



	@Override
	public String toString() {
		return "서비스 [번호=" + service_no + ", 사용날짜=" + use_date + ", 손님id="
				+ service_customer + ", 게임번호=" + service_BoardGame + ", 비용=" + charge
				+ ", 시작시간=" + start_time + ", 끝나는시간=" + end_time + "]";
	}
	
	
}
