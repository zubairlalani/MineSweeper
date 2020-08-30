import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
public class Tile{
	private boolean flagged;
	private int x;
	private int y;
	private JToggleButton jtb;
	private int mineNumb;
	private ImageIcon icon;
	int mineCount = 0;
	boolean firstClick = false;
	public Tile(int x, int y, int mineNumb, int gameTheme){
		this.x=x;
		this.y=y;
		this.mineNumb = mineNumb;
		//this.minePositions = minePositions;
		flagged = false;
		jtb=new JToggleButton();
		if(gameTheme == 1){
			icon = new ImageIcon("block.png");
		}
		else if(gameTheme==2){
			icon = new ImageIcon("./Pirate/block.png");
		}
		else if(gameTheme ==3){
			icon = new ImageIcon("./cards/block.png");
		}
		jtb.setIcon(icon);




	}
	public JToggleButton getJtb(){
		return jtb;
	}
	/*
	public void setMinePositions(MinePair[] arr){
		minePositions = arr;
	}
	*/
	public int getState(){
		return Integer.parseInt(""+getJtb().getClientProperty("state"));
	}
	public boolean isMine(){

		/*
		for(int j=0; j<minePositions.length; j++){
			if(minePositions[j].getX() == getX() && minePositions[j].getY() == getY())
				return true;
		}*/

		if(Integer.parseInt(""+getJtb().getClientProperty("state"))== 9)
			return true;

		return false;

	}
	public boolean isFlagged(){
		if(flagged == false){
			return false;
		}
		else
			return true;
	}
	public void setFlagged(){
		if(isFlagged())
			flagged = false;
		else
			flagged = true;
	}

	public boolean isFirstClicked(){
		return firstClick;
	}
	public void setNumber(int mCount){
		mineCount = mCount;
	}



	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}
}














/*
				if(getNumber()==1){
					getJtb().setIcon(new ImageIcon("one.png"));
				}
				if(getNumber()==2){
					getJtb().setIcon(new ImageIcon("two.png"));
				}
				if(getNumber()==3){
					getJtb().setIcon(new ImageIcon("three.png"));
				}
				if(getNumber()==4){
					getJtb().setIcon(new ImageIcon("four.png"));
				}
				if(getNumber()==5){
					getJtb().setIcon(new ImageIcon("five.png"));
				}
				if(getNumber()==6){
					getJtb().setIcon(new ImageIcon("six.png"));
				}
				if(getNumber()==7){
					getJtb().setIcon(new ImageIcon("seven.png"));
				}
				if(getNumber()==8){
					getJtb().setIcon(new ImageIcon("eight.png"));
				}
				*/