package com.my.dao;
import java.util.List;

import com.my.dao.*;
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.exception.NotFoundException;
import com.my.vo.BoardGame;
import com.my.vo.GameType;


public class DaoTest {

	public static void main(String[] args) throws Exception {
		GameTypeDAO gtDAO = new GameTypeDAO();
		
		GameType g = new GameType("윷놀이",null, 100, 120);
		g.setGame_type("윷놀이");
		
		gtDAO.update(g);
		
		List<GameType> list = gtDAO.selectAll();
		int i = 1; 
		for(GameType type : list) {
			System.out.print(i + " " );
			System.out.println(type);
			i++;
		}
		
		
		

	}

}
