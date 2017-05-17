import java.io.*;
import java.util.Scanner;

// created by 2 other (anonymous) students and me
// please ignore the inconsistence in coding style

public class ConnectFour {

	String[][] Field;
	Scanner Input;
	Players turn;
	
	
	ConnectFour()
	{
		Field = new String [6][7];
		generateField();
		calculateTurn();
		GameOver = false;
		Input = new java.util.Scanner(System.in);
	}
	
	public void save() {
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream("ConnectFour.save"));
			out.writeObject(Field);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println(“Error”);
		} catch (IOException e) {
			System.out.println(“There was a problem, sry :(“);
		}
	}
	
	public void load() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("ConnectFour.save"));
	        Field = (String[][]) in.readObject();
	        in.close();
		} catch (IOException e) {
			System.out.println(“There is no save File.”);
		} catch (ClassNotFoundException e) {
			System.out.println(“Class not found.”);
		}
	}
	  
	
	public enum Players {
		PLAYER1, PLAYER2	//Player1: X Player2: O
	}
	
	public boolean isGameOver(){
		//check vertical
		String last;
		int count;
		int column;
		int row;
		for (column = Field[0].length-1; column >= 0; column--){
			last = Field[Field.length-1][column];
			count = 0;
			for (row = Field.length-1; row >= 0; row--){
				if (Field[row][column].equals(last)){
					count ++;
					if (count == 4 && !last.equals("_")){
						if (last.equals("X")) System.out.println("Player1 wins");
						else System.out.println("Player2 wins");
						return true;
					} 
				}
				else {
					count = 1;
					last = Field[row][column];
				}
			}
		}
		//check horizontal
		for (row = 0; row <= Field.length-1; row ++){
			count = 0;
			last = Field[row][0];
			for (column = 0; column <= Field[row].length-1; column ++){
				if (Field[row][column].equals(last)){
					count++;
					if (count == 4 && !last.equals("_")){
						if (last.equals("X")) System.out.println("Player1 wins");
						else System.out.println("Player2 wins");
						return true;
					} 
				}
				else {
					count = 1;
					last = Field[row][column];
				}
			}
		}
		
		//check diagonal upward
		row = 3;
		column = 0;
		while (column <=3){
			int newcolumn = column;
			count = 0;
			last = Field[row][column];
			for(int newrow = row; newrow >= 0 && newcolumn <= 6; newrow --, newcolumn++){
				if (Field[newrow][newcolumn].equals(last)){
					count++;
					if (count == 4 && !last.equals("_")){
						if (last.equals("X")) System.out.println("Player1 wins");
						else System.out.println("Player2 wins");
						return true;
					} 
				}
				else{
					count = 1;
					last = Field[newrow][newcolumn];
				}
			}
			if (row < 5){
				row ++; column = 0;
			}
			else{
				column++;
			}
			
		}
		//check diagonal downward
		
		row = 2;
		column = 0;
		while (column <=3){
			int newcolumn = column;
			count = 0;
			last = Field[row][column];
			for(int newrow = row; newrow <= 5 && newcolumn <= 6; newrow ++, newcolumn++){
				if (Field[newrow][newcolumn].equals(last)){
					count++;
					if (count == 4 && !last.equals("_")){
						if (last.equals("X")) System.out.println("Player1 wins");
						else System.out.println("Player2 wins");
						return true;
					} 
				}
				else{
					count = 1;
					last = Field[newrow][newcolumn];
				}
			}
			if (row > 0){
				row --;
			}
			else{
				column++;
			}
			
		}
		return false;
	}
	
	public void generateField() {
		for (int x = 0; x < Field.length; x++) {
			for (int y = 0; y < Field[x].length; y++) {
				Field[x][y] = "_";
			}
		}
	}
	
	
	public void showField() {
		for (int column = 1; column <= 7; column ++){
			System.out.print(" " + column);
		}
		System.out.println("");
	
		for (int x = 0; x < Field.length; x++) {
			for (int y = 0; y < Field[x].length; y++) {
					System.out.print(" " + Field[x][y]);
				}
			System.out.println();
		}
	}
	
	public void calculateTurn() {
		int p1turns = 0;
		int p2turns = 0;
		for (int x = 0; x < Field.length; x++) {
			for (int y = 0; y < Field[x].length; y++) {
				if(Field[x][y].equals("X"))
					p1turns++;
				if(Field[x][y].equals("O"))
					p2turns++;
			}
		}
		if(p1turns > p2turns)
			turn = Players.PLAYER2;
		else
			turn = Players.PLAYER1;
	}
	
	
	public void playerTurn(int column) {
		column = column-1;
		if (column < 0 || column > 6){
			System.out.println("Sry, column "+ (column+1) + " doesn't exist");
			return;
		}
		if (Field[0][column].equals("_")){
			int height = 0;
			while (height < Field.length && Field[height][column].equals("_"))
			{
				height++;
			}
			if(turn == Players.PLAYER1)
				Field[height-1][column] = "X";
			else
				Field[height-1][column] = "O";
		}
		else{
			System.out.println("Sorry, column "+ (column+1) +" is full, select another one");
		}
	}
	
	public boolean isFull() {
	for(String[] s1: Field) {
			for(String s2: s1) {
				if(s2.equals("_"))
					return false;
			}
		}
		return true;
	}
	
	
	
	public boolean isNumber(String string) {
		try {
		Long.parseLong(string);
		} catch (Exception e) {
		 		return false;
		  	}
		return true;
	}
	
	
	public static void main(String[] args) {
		String input;
		ConnectFour Test = new ConnectFour();
		Test.showField();
		while (!Test.isGameOver() && !Test.isFull()){
			input = Test.Input.next().trim();
			Test.calculateTurn();
			System.out.println(Test.turn + ", make your move!");
			if(input.equals("save"))
				Test.save();
			else if(input.equals("load"))
				Test.load();
			else if(input.equals("reset"))
				Test.generateField();
			else if(Test.isNumber(input))
				Test.playerTurn(Integer.parseInt(input));
			else {
				System.out.println("'" + input + "' is an invalid argument, try again");
			}
			Test.showField();
			
		}
		if(Test.isFull())
			System.out.println("Ist schon voll :(");
	}

}
