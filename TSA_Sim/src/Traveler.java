/**
 * This class simulates a traveler 
 * at TSA. 
 * @author jasmine bilir 
 * @version March 12, 2019
 *
 */
public class Traveler {
	int id;  //id # in order  of arrival 
	int arrivalTime; //time of arrival 
	int waitTime;//time waiting in line
	int length; //length of inspection
	final int MAX_LENGTH, MIN_LENGTH; //max time and min time for inspection
	boolean isPre; //whether the traveler is pre-checked or not

	/**
	 * Constructs a new Traveler
	 * @param idVal - # in order of arrival
	 * @param time - time of arrival
	 * @param max - max length of inspection time
	 * @param min - min length of inspection time
	 */
	public Traveler(int idVal, int time, int max, int min) {
		id = idVal; 
		arrivalTime = time;
		MAX_LENGTH = max;
		MIN_LENGTH = min;
		waitTime = 0;
		length = (int)(Math.random()*MAX_LENGTH + MIN_LENGTH);
		isPre = (10== ((int)(Math.random()*10+1))); //one in 10 chance of being pre-checked
	}

	/**
	 * Constructs a new traveler 
	 * (min is 2 min, max is 10 min)
	 * @param idVal - # in order of arrival
	 * @param time - time of arrival
	 */
	public Traveler(int idVal, int time) {
		this(idVal, time, 10, 2);
	}

	/**
	 * returns traveler's id
	 * @return - id num
	 */
	public int getId() {
		return id;
	}


	/**
	 * returns the traveler's arrival time
	 * @return - arrival time
	 */
	public int getArrivalTime() {
		return arrivalTime;
	}


	/**
	 * returns the length of inspection
	 * @return - inspection length
	 */
	public int getLength() {
		return length;
	}


	/**
	 * sets the wait time of the traveler
	 * @param i - time waited
	 */
	public void setWaitTime(int i) {
		waitTime = i;	
	}

	/**
	 * returns the amount of time in min 
	 * which the traveler waited in line
	 * @return time waited in line
	 */
	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * returns whether the traveler is pre-checked or not
	 * @return true if pre-checked
	 */
	public boolean getIsPre() {
		return isPre;
	}
}
