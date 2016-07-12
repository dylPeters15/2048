import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class DataStructureFor2048 {

	private JLabel[][] labels;
	private DataStructureFor2048Delegate delegate;
	private boolean shouldStopAt2048;
	private boolean shouldContinue;

	DataStructureFor2048(){
		this(4);
	}

	DataStructureFor2048(int numRows){
		this(numRows,numRows);
	}

	DataStructureFor2048(int numRows, int numCols){
		this(numRows,numCols,true);
	}

	DataStructureFor2048(int numRows, int numCols, boolean newShouldStopAt2048){
		shouldContinue = true;
		shouldStopAt2048 = newShouldStopAt2048;
		labels = new JLabel[numRows][numCols];
		for (int r = 0; r < numRows; r++){
			for (int c = 0; c < numCols; c++){
				labels[r][c] = new JLabel("",SwingConstants.CENTER);
				labels[r][c].setBackground(new Color((float)0.0, (float)1.0, (float)1.0, (float)1.0));
				labels[r][c].setOpaque(true);
				labels[r][c].setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}
		}
		makeRandom();
		checkBoard();
	}

	public void setShouldStopAt2048(boolean newShouldStopAt2048){
		shouldStopAt2048 = newShouldStopAt2048;
	}

	public boolean getShouldStopAt2048(){
		return shouldStopAt2048;
	}

	public JLabel[][] getJLabels(){
		return labels;
	}

	public void setDelegate(DataStructureFor2048Delegate newDelegate){
		delegate = newDelegate;
	}

	public DataStructureFor2048Delegate getDelegate(){
		return delegate;
	}

	public int labelVal(int row, int col){
		if (labels[row][col].getText().isEmpty()){
			return -1;
		}
		return Integer.valueOf(labels[row][col].getText());
	}

	public void setLabel(int row, int col, int val){
		labels[row][col].setText(String.valueOf(val));
		if (val == -1){
			labels[row][col].setText("");
			labels[row][col].setBackground(new Color((float)0.0,(float)1.0,(float)1.0,(float)1.0));
		} else if (val > 2048){
			labels[row][col].setBackground(new Color((float)0.5,(float)0.5,(float)0.5,(float)1.0));
		} else {
			labels[row][col].setBackground(new Color((float)val/2048/2,(float)0.5,(float)0.5,(float)1.0));
		}
	}

	public void doubleLabel(int row, int col){
		setLabel(row,col,labelVal(row,col)*2);
	}

	public void makeRandom(){
		if (shouldContinue){
			ArrayList<Integer[]> available = new ArrayList<Integer[]>();

			for (int r = 0; r < labels.length; r++){
				for (int c = 0; c < labels[r].length; c++){
					if (labelVal(r, c) == -1){
						Integer[] toAdd = new Integer[2];
						toAdd[0] = r;
						toAdd[1] = c;
						available.add(toAdd);
					}
				}
			}

			if (available.size() > 0){
				int rand = (int)(Math.random()*available.size()); //pick a random empty square
				int row = available.get(rand)[0];
				int col = available.get(rand)[1];
				rand = ((int)(Math.random()*2+1))*2; //fill that square with either a 2 or 4
				setLabel(row, col, rand);
				//
				for (int r = 0; r < labels.length; r++){
					for (int c = 0; c < labels[r].length; c++){
						setLabel(r,c,labelVal(r,c));
					}
				}
				labels[row][col].setBackground(new Color((float)1.0,(float)0.0,(float)0.0));
				//
			}
			checkBoard();
		}
	}

	private void checkBoard(){
		boolean emptySpacesExist = false;
		boolean didWin = false;
		for (int r = 0; r < labels.length; r++){
			for (int c = 0; c < labels[r].length; c++){
				if (labelVal(r, c) == 2048){
					didWin = true;
				}
				if (labelVal(r, c) == -1){
					emptySpacesExist = true;
				}
			}
		}
		if (didWin){
			didWin();
			return;
		}
		if (!emptySpacesExist){
			for (int r = 0; r < labels.length; r++){
				for (int c = 0; c < labels[r].length; c++){
					if (r != 0){
						if (labelVal(r-1, c) == labelVal(r,c)){
							return;
						}
					}
					if (c != 0){
						if (labelVal(r,c-1) == labelVal(r,c)){
							return;
						}
					}
				}
			}
			didLose();
		}
	}

	private void didWin(){
		try {
			if (delegate != null){
				delegate.didWin();
			}
		} catch (NullPointerException e){

		}
		if (shouldStopAt2048){
			shouldContinue = false;
		}
		System.out.println("DidWin");
	}

	private void didLose(){
		try {
			if (delegate != null){
				delegate.didLose();
			}
		} catch (NullPointerException e){

		}
		shouldContinue = false;
		System.out.println("DidLose");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean canSwipeDown(){
		for (int c = 0; c < labels[0].length; c++){
			if (canSwipeDown(c)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeDown(int c){
		for (int r = labels.length-1; r > 0; r--){
			if (canSwipeDown(r,c)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeDown(int r, int c){
		if ((labelVal(r,c) == -1 
				&& labelVal(r-1,c)!=-1) 
				|| (labelVal(r,c) != -1 
				&& labelVal(r,c) == labelVal(r-1,c))){
			return true;
		}
		return false;
	}
	public void swipeDown(){
		if (shouldContinue && canSwipeDown()){
			System.out.println("Down");
			for (int c = 0; c < labels[0].length; c++){
				moveAllDown(c);
				mergeAllDown(c);
			}
			didMakeMove();
		}
	}
	private void moveAllDown(int c){
		for (int r = labels.length-1; r >= 0; r--){
			if (labelVal(r,c)!=-1){
				moveDown(r,c);
			}
		}
	}
	private void moveDown(int r,int c){
		if (r != labels.length-1 && labelVal(r+1,c) == -1){
			setLabel(r+1,c,labelVal(r,c));
			setLabel(r,c,-1);
			moveDown(r+1,c);
		}
	}
	private void mergeAllDown(int c){
		for (int r = labels.length-1; r > 0; r--){
			if (labelVal(r,c) != -1 && labelVal(r,c) == labelVal(r-1,c)){
				mergeDown(r,c);
			}
		}
	}
	private void mergeDown(int r, int c) {
		doubleLabel(r,c);
		setLabel(r-1,c,-1);
		moveAllDown(c);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean canSwipeUp(){
		for (int c = 0; c < labels[0].length; c++){
			if (canSwipeUp(c)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeUp(int c){
		for (int r = 0; r < labels.length-1; r++){
			if (canSwipeUp(r,c)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeUp(int r, int c){
		if ((labelVal(r,c) == -1 && labelVal(r+1,c)!=-1) 
				|| (labelVal(r,c) != -1 
				&& labelVal(r,c) == labelVal(r+1,c))){
			return true;
		}
		return false;
	}
	public void swipeUp(){
		if (shouldContinue && canSwipeUp()){
			System.out.println("Up");
			for (int c = 0; c < labels[0].length; c++){
				moveAllUp(c);
				mergeAllUp(c);
			}
			didMakeMove();
		}
	}
	private void moveAllUp(int c){
		for (int r = 0; r < labels.length; r++){
			if (labelVal(r,c)!=-1){
				moveUp(r,c);
			}
		}
	}
	private void moveUp(int r,int c){
		if (r != 0 && labelVal(r-1,c) == -1){
			setLabel(r-1,c,labelVal(r,c));
			setLabel(r,c,-1);
			moveUp(r-1,c);
		}
	}
	private void mergeAllUp(int c){
		for (int r = 0; r < labels.length-1; r++){
			if (labelVal(r,c) != -1 && labelVal(r,c) == labelVal(r+1,c)){
				mergeUp(r,c);
			}
		}
	}
	private void mergeUp(int r, int c) {
		doubleLabel(r,c);
		setLabel(r+1,c,-1);
		moveAllUp(c);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean canSwipeRight(){
		for (int r = 0; r < labels.length; r++){
			if (canSwipeRight(r)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeRight(int r){
		for (int c = labels[r].length-1; c > 0; c--){
			if (canSwipeRight(r,c)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeRight(int r, int c){
		if ((labelVal(r,c) == -1 
				&& labelVal(r,c-1)!=-1) 
				|| (labelVal(r,c) != -1 
				&& labelVal(r,c) == labelVal(r,c-1))){
			return true;
		}
		return false;
	}
	public void swipeRight(){
		if (shouldContinue && canSwipeRight()){
			System.out.println("Right");
			for (int r = 0; r < labels.length; r++){
				moveAllRight(r);
				mergeAllRight(r);
			}
			didMakeMove();
		}
	}
	private void moveAllRight(int r){
		for (int c = labels[r].length-1; c >= 0; c--){
			if (labelVal(r,c)!=-1){
				moveRight(r,c);
			}
		}
	}
	private void moveRight(int r,int c){
		if (c != labels[r].length-1 && labelVal(r,c+1) == -1){
			setLabel(r,c+1,labelVal(r,c));
			setLabel(r,c,-1);
			moveRight(r,c+1);
		}
	}
	private void mergeAllRight(int r){
		for (int c = labels[r].length-1; c > 0; c--){
			if (labelVal(r,c) != -1 && labelVal(r,c) == labelVal(r,c-1)){
				mergeRight(r,c);
			}
		}
	}
	private void mergeRight(int r, int c) {
		doubleLabel(r,c);
		setLabel(r,c-1,-1);
		moveAllRight(r);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private boolean canSwipeLeft(){
		for (int r = 0; r < labels.length; r++){
			if (canSwipeLeft(r)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeLeft(int r){
		for (int c = 0; c < labels[r].length-1; c++){
			if (canSwipeLeft(r,c)){
				return true;
			}
		}
		return false;
	}
	private boolean canSwipeLeft(int r, int c){
		if ((labelVal(r,c) == -1 
				&& labelVal(r,c+1)!=-1) 
				|| (labelVal(r,c) != -1 
				&& labelVal(r,c) == labelVal(r,c+1))){
			return true;
		}
		return false;
	}
	public void swipeLeft(){
		if (shouldContinue && canSwipeLeft()){
			System.out.println("Left");
			for (int r = 0; r < labels.length; r++){
				moveAllLeft(r);
				mergeAllLeft(r);
			}
			didMakeMove();
		}
	}
	private void moveAllLeft(int r){
		for (int c = 0; c < labels[r].length; c++){
			if (labelVal(r,c)!=-1){
				moveLeft(r,c);
			}
		}
	}
	private void moveLeft(int r,int c){
		if (c != 0 && labelVal(r,c-1) == -1){
			setLabel(r,c-1,labelVal(r,c));
			setLabel(r,c,-1);
			moveLeft(r,c-1);
		}
	}
	private void mergeAllLeft(int r){
		for (int c = 0; c < labels[r].length-1; c++){
			if (labelVal(r,c) != -1 && labelVal(r,c) == labelVal(r,c+1)){
				mergeLeft(r,c);
			}
		}
	}
	private void mergeLeft(int r, int c) {
		doubleLabel(r,c);
		setLabel(r,c+1,-1);
		moveAllLeft(r);
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void didMakeMove(){
		checkBoard();
		makeRandom();
	}





}
