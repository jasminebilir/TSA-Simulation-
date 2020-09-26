import java.util.Queue;
import java.util.LinkedList;
/**
 * This class simulates 
 * a TSA agent and his/her line 
 * @author Jasmine Bilir 
 * @version March 12, 2019
 *
 */
public class Agent {
	int travelersServed; //# of travelers served
	Queue <Traveler> line; //travelers in line for Agent 
	int lineSize; //number of travelers in line 
	int currentTime; //currentTime in simulation
	int busyUntil;//minute when agent will finish current inspection
	boolean isAvailable; //whether agent is currently inspecting or not
	Traveler currentTrav; // current traveler being inspected 

	/**
	 * constructs an agent with a line
	 */
	public Agent () {
		line = new LinkedList<Traveler>();
		lineSize = 0;
		travelersServed = 0;
		currentTime = 0; 
		busyUntil = currentTime;
		isAvailable= true;
	}

	/**
	 * adds a traveler to the agent's line
	 * @param t - Traveler
	 */
	public void addTraveler(Traveler t) {
		line.add(t);
		lineSize++;
	}

	/**
	 * inspects traveler for given amount of time 
	 * then removes traveler from line
	 * @return traveler served
	 */
	public Traveler inspectTraveler() {
		if (!line.isEmpty() && isAvailable) {
			isAvailable = false;
			currentTrav = line.remove();
			travelersServed++;
			lineSize--;
			busyUntil = currentTime + currentTrav.getLength();
			currentTrav.setWaitTime(currentTime - currentTrav.getArrivalTime());
			return currentTrav;
		}
		else if (!isAvailable) {
			throw new IllegalStateException("Agent is still inspecting another traveler");
		}
		else {
			throw new IllegalStateException("Line is empty!");
		}
	}

	/**
	 * returns the number of travelers which the agent has served
	 * @return number of travelers served
	 */
	public int getTravelersServed() {
		return travelersServed;
	}

	/**
	 * returns number of travelers in agent's line
	 * @return number of travelers in line
	 */
	public int getLineSize() {
		return lineSize;
	}

	/**
	 * moves clock forward
	 */
	public void moveClock() {
		currentTime++; 
		if (currentTime == busyUntil) {
			isAvailable = true;
			System.out.println(currentTime + ": Traveler " + currentTrav.getId() +" Departs");
		}
	}

	/**
	 * returns the agent's availability 
	 * @return true if the agent is not currently inspecting a traveler
	 */
	public boolean getAvailability() {
		return isAvailable;
	}

	/**
	 * returns the time when Agent will be available 
	 * @return time when teller becomes available
	 */
	public int getBusyUntil() {
		return busyUntil;
	}
}
