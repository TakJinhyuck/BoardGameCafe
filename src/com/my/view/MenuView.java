package com.my.view;

public class MenuView {
	   
	   /**
	    * 로그인성공시 메뉴
	    */
	   public static void printLoginMenu() {
	      System.out.println("=================메뉴================");
	      System.out.println("1.게임 찾기, 2.반납 , 3.로그아웃, 4.내정보보기 5.이용 시간 및 요금 확인");
	   }
	   
	   /**
	    * 게임찾기 메뉴
	    */
	   public static void printGameSearchMenu() {
	      System.out.println("=========================게임검색=============================");
	      System.out.println("1.이름검색, 2.난이도검색, 3.인원검색, 4.소요시간검색, 5.순위표기, 9.나가기");   
	   }
	   
	   /**
	    * 대여 메뉴
	    */
	   public static void printRentalMenu() {
	      System.out.println("===대여하기====");
	      System.out.println("1.대여, 9.나가기");
	   }
	   
	   /**
	    * 정보보기 메뉴
	    */
	   public static void printUserMenu() {
	      System.out.println("=========내정보 보기=======");
	      System.out.println("1. 정보수정, 2.탈퇴, 9.나가기");
	   }
	   
	   /**
	    * 관리자 메뉴
	    */
	   public static void printAdminMenu() {
	      System.out.println("=======================관리자 메뉴======================");
	      System.out.println("1.회원관리, 2.보드게임 관리, 3.정산, 9.나가기");
	   }
	   
	   /**
	    * 회원관리 메뉴
	    */
	   public static void printMemberManagement() {
	      System.out.println("============회원관리=============");
	      System.out.println("1.이름검색, 2.아이디검색 , 3.전체 검색");   
	   }
	   
	   /**
	    * 보드게임 관리 메뉴
	    */
	   public static void printGameManagement() {
	      System.out.println("=====================보드게임 관리=====================");
	      System.out.println("1.보드게임 추가, 2.게임 정보 추가, 3.보드게임 전체검색, 4.보드게임 삭제");
	   }
	   
//	   public static void printGameCharge() {
//		   System.out.println("***보드게임 끝***");
//		   System.out.println("1. 계산하기, 2.나가기");
//	   }
	}