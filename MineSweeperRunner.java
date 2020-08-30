import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.*;
import java.util.Timer;
import java.util.TimerTask;
public class MineSweeperRunner extends JPanel implements ActionListener, MouseListener{

	JFrame frame;
	JToggleButton jtb;
	JMenuBar menuBar;
	JMenu difficultyMenu, controlMenu, iconMenu;
	JMenuItem easy, medium, hard, controlOne, controlTwo, standard, pirate, deck;
	JPanel menuPanel, gamePanel, scorePanel;
	JLabel flagsLabel, timeLabel;
	JButton resetButton;
	ImageIcon icon;
	int mineNumb = 10;
	int flags = mineNumb;
	int gridRow = 9;
	int gridCol = 9;
	int timePassed = -1;
	Tile[][] tiles;

	Timer timer;

	boolean firstClick = false;
	boolean flagged = false;
	boolean gameOver = false;

	int gameTheme = 1;

	public MineSweeperRunner(){
		frame = new JFrame();

		difficultyMenu = new JMenu("DIFFICULTY LEVEL");				//menu of difficulty levels

		easy = new JMenuItem("EASY");
		easy.addActionListener(this);

		medium = new JMenuItem("MEDIUM");
		medium.addActionListener(this);

		hard = new JMenuItem("HARD");
		hard.addActionListener(this);

		difficultyMenu.add(easy);
		difficultyMenu.add(medium);
		difficultyMenu.add(hard);

		controlMenu = new JMenu("Controls");
		controlOne = new JMenuItem("Left-Click an empty square to reveal it");
		controlTwo = new JMenuItem("Right-Click an empty square to flag it");
		controlMenu.add(controlOne);
		controlMenu.add(controlTwo);

		iconMenu = new JMenu("ICON");
		standard = new JMenuItem("standard");
		standard.addActionListener(this);
		pirate = new JMenuItem("pirate");
		pirate.addActionListener(this);
		deck = new JMenuItem("Deck of Cards");
		deck.addActionListener(this);
		iconMenu.add(standard);
		iconMenu.add(pirate);
		iconMenu.add(deck);

		menuBar = new JMenuBar();
		menuBar.add(difficultyMenu);
		menuBar.add(controlMenu);
		menuBar.add(iconMenu);
		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.add(menuBar, BorderLayout.NORTH);

		resetButton = new JButton();
		//scaledImage(new ImageIcon("./Sprites/Classic/smile.png"))
		resetButton.setIcon(new ImageIcon("smile.png"));
		resetButton.setPreferredSize(new Dimension(32, 32));
		resetButton.addActionListener(this);

		flagsLabel = new JLabel("  FLAGS:  "+flags+"  ");

		timeLabel =  new JLabel("  TIME:  ");


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.add(flagsLabel);
		buttonPanel.add(resetButton);
		buttonPanel.add(timeLabel);

		menuPanel.add(buttonPanel, BorderLayout.SOUTH);

		gamePanel = new JPanel();

		createGrid();	//first time creating grid

		frame.add(menuPanel, BorderLayout.NORTH);
		frame.add(gamePanel, BorderLayout.CENTER);
		frame.setSize(gridCol*32, gridRow*32);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public void setMine(int currRow, int currCol){
		int count = mineNumb;

		while(count>0)
		{
			int randomX = (int)(Math.random()*gridCol);
			int randomY = (int)(Math.random()*gridRow);
			int state = Integer.parseInt(""+tiles[randomY][randomX].getJtb().getClientProperty("state")); //0-9
			if(state == 0 && (Math.abs(randomY-currRow)>1 || Math.abs(randomX-currCol)>1)){
				tiles[randomY][randomX].getJtb().putClientProperty("state", 9);
				//tiles[randomY][randomX].getJtb().setIcon(new ImageIcon("mine.png"));
				count--;
				System.out.print(4);
			}
		}

		for(int r=0; r<gridRow; r++){
			for(int c=0; c<gridCol; c++){
				count =0;
				if(!tiles[r][c].isMine()){
					count = getNumber(r, c);
					tiles[r][c].getJtb().putClientProperty("state", count);
				}

			}
		}

		timer = new Timer();
		timer.schedule(new addToTimer(), 0, 1000);
	}


	public void createGrid(){
		tiles = new Tile[gridRow][gridCol];		//gridrow and gridcol r set globally as default easy mode
		gamePanel.removeAll();					//empty grid
		gamePanel.setLayout(new GridLayout(gridRow, gridCol));								//resets grid layout length based on difficulty lvl

		for(int y=0; y<gridRow; y++){															//creates actual grid
			for(int x=0; x<gridCol; x++){
				tiles[y][x] = new Tile(x, y, mineNumb, gameTheme);
				tiles[y][x].getJtb().putClientProperty("column", x);
				tiles[y][x].getJtb().putClientProperty("row", y);
				tiles[y][x].getJtb().putClientProperty("state", 0);
				tiles[y][x].getJtb().setFocusPainted(false);
				tiles[y][x].getJtb().addMouseListener(this);
				//tiles[y][x].getJtb().setBorder(BorderFactory.createBevelBorder(1));
				gamePanel.add(tiles[y][x].getJtb());
			}
		}
		frame.setSize(gridCol*32, gridRow*32);
		frame.repaint();
		frame.revalidate();
	}


	public void mouseClicked(MouseEvent e){
	}
	public void mouseEntered(MouseEvent e){
	}
	public void mouseExited(MouseEvent e){
	}
	public void mousePressed(MouseEvent e){
	}
	public void mouseReleased(MouseEvent e){

		int row = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("row"));
		int col = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("column"));
		if(!gameOver){
			if(SwingUtilities.isRightMouseButton(e)){

				if(tiles[row][col].isFlagged()){
					tiles[row][col].getJtb().setIcon(null);
					if(gameTheme == 1){
						icon = new ImageIcon("block.png");
					}
					if(gameTheme == 2){
						icon = new ImageIcon("./Pirate/block.png");
					}
					if(gameTheme == 3){
						icon = new ImageIcon("./cards/block.png");
					}
					tiles[row][col].getJtb().setIcon(icon);

					tiles[row][col].setFlagged();
					flags++;
					flagsLabel.setText("  FLAGS:  "+flags+"  ");
				}
				else if(!tiles[row][col].isFlagged() && !tiles[row][col].getJtb().isSelected()){ //not flagged and not selected
					if(gameTheme == 1){
						icon = new ImageIcon("flag.png");
					}
					if(gameTheme == 2){
						icon = new ImageIcon("./Pirate/flagged.png");
					}
					if(gameTheme == 3){
						icon = new ImageIcon("./cards/flag.png");
					}
					tiles[row][col].getJtb().setIcon(icon);
					tiles[row][col].setFlagged();
					flags--;
					flagsLabel.setText("  FLAGS:  "+flags+"  ");
				}
			}

			if(SwingUtilities.isLeftMouseButton(e)){
				if(!firstClick && !tiles[row][col].isFlagged()){
					setMine(row, col);

					firstClick=true;
				}
				int state = Integer.parseInt(""+((JToggleButton)e.getComponent()).getClientProperty("state"));
				if(!tiles[row][col].isFlagged() && state == 9){
					tiles[row][col].getJtb().setSelected(true);

					if(gameTheme == 1){
						icon = new ImageIcon("mine.png");
					}
					if(gameTheme == 2){
						icon = new ImageIcon("./Pirate/bomb.png");
					}
					if(gameTheme == 3){
						icon = new ImageIcon("./cards/bomb.png");
					}
					tiles[row][col].getJtb().setIcon(icon);
					gameOver = true;
					resetButton.setIcon(new ImageIcon("losesmile.png"));
					JOptionPane.showMessageDialog(null, "YOU LOSE SIR!");

				}
				else if(!tiles[row][col].isFlagged()){
					expand(row, col);
					checkWin();
				}
				else if(tiles[row][col].isFlagged()){
					tiles[row][col].getJtb().setSelected(false);
				}
			}
		}
	}

	public void checkWin(){
		int totalSpaces = gridRow * gridCol;
		int count=0;
		for(int r=0; r<gridRow; r++){
			for(int c=0; c<gridCol; c++){
				int state = tiles[r][c].getState();
				if(tiles[r][c].getJtb().isSelected() && state!=9){
					count++;
				}
			}
		}
		if(mineNumb == totalSpaces-count){
			JOptionPane.showMessageDialog(null, "You Win!");
		}

		//CHECK NUMBER OF FLAGS TOO

	}

	public int getNumber(int r, int c){
		int count=0;
		for (int k = -1; k <= 1 ; k++) {
			for (int l = -1; l <= 1; l++) {
				try {
					if (tiles[r+l][c+k].isMine()) {
						count++;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
				}

			}
		}
		return count;
	}

	public void writeText(int r, int c, int state){

		switch(state)
		{
			case 1:
			if(gameTheme == 1){
				icon = new ImageIcon("one.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/one.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/one.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 2:
			if(gameTheme == 1){
				icon = new ImageIcon("two.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/two.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/two.png");
			}

			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 3:
			if(gameTheme == 1){
				icon = new ImageIcon("three.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/three.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/three.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 4:
			if(gameTheme == 1){
				icon = new ImageIcon("four.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/four.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/four.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 5:
			if(gameTheme == 1){
				icon = new ImageIcon("five.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/five.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/five.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 6:
			if(gameTheme == 1){
				icon = new ImageIcon("six.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/six.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/six.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 7:
			if(gameTheme == 1){
				icon = new ImageIcon("seven.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/seven.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/seven.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 8:
			if(gameTheme == 1){
				icon = new ImageIcon("eight.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/eight.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/eight.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
			case 9:
			if(gameTheme == 1){
				icon = new ImageIcon("mine.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/bomb.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/bomb.png");
			}
			tiles[r][c].getJtb().setIcon(icon);
				break;
		}
	}

	public void expand(int row, int col){

		if(!tiles[row][col].getJtb().isSelected()){
			tiles[row][col].getJtb().setSelected(true);
		}

		int state = Integer.parseInt(""+tiles[row][col].getJtb().getClientProperty("state"));
		if(state>0){
			writeText(row,col,state);
		}
		else{
			if(gameTheme == 1){
				icon = new ImageIcon("empty.png");
			}
			if(gameTheme == 2){
				icon = new ImageIcon("./Pirate/empty.png");
			}
			if(gameTheme == 3){
				icon = new ImageIcon("./cards/empty.png");
			}
			tiles[row][col].getJtb().setIcon(icon);
			for(int r = row-1; r<= row+1; r++){
				for(int c = col-1; c<= col+1; c++){

					if(!(r==row && c == col)){
						try{
							if(!tiles[r][c].getJtb().isSelected() && !tiles[r][c].isFlagged()){
								expand(r,c);
							}

						}catch(ArrayIndexOutOfBoundsException e){

						}
					}
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource()== easy){
			resetButton.setIcon(new ImageIcon("smile.png"));
			gridRow = 9;
			gridCol = 9;
			mineNumb = 10;
			flags=mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			if(firstClick){
				timer.cancel();
				timer.purge();
				timeLabel.setText("  TIME:  ");
			}
			firstClick=false;
			createGrid();						//recreate grid according to row and column length
		}
		if(e.getSource()== medium){
			resetButton.setIcon(new ImageIcon("smile.png"));
			gridRow = 16;
			gridCol = 16;
			mineNumb = 40;
			flags=mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			if(firstClick){
				timer.cancel();
				timer.purge();
				timeLabel.setText("  TIME:  ");
			}
			firstClick=false;
			createGrid();						//recreate grid according to row and column length
		}
		if(e.getSource()== hard){
			resetButton.setIcon(new ImageIcon("smile.png"));
			gridRow = 16;
			gridCol = 30;
			mineNumb = 99;
			flags=mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			if(firstClick){
				timer.cancel();
				timer.purge();
				timeLabel.setText("  TIME:  ");
			}
			firstClick=false;
			createGrid();
		}
		if(e.getSource()==resetButton && !firstClick){
			resetButton.setIcon(new ImageIcon("smile.png"));
			for(int r = 0; r<gridRow; r++){
				for(int c=0; c<gridCol; c++){
					if(tiles[r][c].isFlagged()){
						tiles[r][c].getJtb().setIcon(null);
						if(gameTheme == 1){
							icon = new ImageIcon("block.png");
						}
						if(gameTheme == 2){
							icon = new ImageIcon("./Pirate/block.png");
						}
						if(gameTheme == 3){
							icon = new ImageIcon("./cards/block.png");
						}
						tiles[r][c].getJtb().setIcon(icon);
						tiles[r][c].setFlagged();
					}
				}
			}
			flags = mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
		}
		if(e.getSource()==resetButton && firstClick){
			resetButton.setIcon(new ImageIcon("smile.png"));
			firstClick = false;
			flags = mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			timer.cancel();
			timer.purge();
			System.out.println("HELLLOO");
			timeLabel.setText("  TIME:  ");

			createGrid();
		}
		if(e.getSource() == standard){
			gameTheme=1;
			resetButton.setIcon(new ImageIcon("smile.png"));
			flags = mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			if(firstClick){
				timer.cancel();
				timer.purge();
				timeLabel.setText("  TIME:  ");
			}
			firstClick = false;

			createGrid();
		}
		if(e.getSource() == pirate){
			gameTheme = 2;
			resetButton.setIcon(new ImageIcon("smile.png"));
			flags = mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			if(firstClick){
				System.out.println("HELLLO");
				timer.cancel();
				timer.purge();
				timeLabel.setText("  TIME:  ");
			}
			firstClick = false;

			createGrid();
		}
		if(e.getSource() == deck){
			gameTheme = 3;
			resetButton.setIcon(new ImageIcon("smile.png"));
			flags = mineNumb;
			flagsLabel.setText("  FLAGS:  "+flags+"  ");
			timePassed=-1;
			gameOver=false;
			if(firstClick){
				timer.cancel();
				timer.purge();
				timeLabel.setText("  TIME:  ");
			}
			firstClick = false;
			createGrid();
		}

	}
	public static void main(String args[]){
		MineSweeperRunner app = new MineSweeperRunner();
	}

	class addToTimer extends TimerTask{
		public void run(){
			if(!gameOver){
				timePassed++;
				timeLabel.setText("  TIME:  "+timePassed+" ");
			}
		}

	}
}

