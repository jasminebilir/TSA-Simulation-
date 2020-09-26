import java.awt.Font;

import javax.swing.JOptionPane;

/**
 * This class simulates airport security. 
 * 
 * @author jasmine bilir 
 * @version March 12, 2019
 *
 */
public class AirportSecurity {
	final int NUM_MINUTES; //minutes in a day
	final int ARRIVAL_PROB; //probability of a traveler arrival
	Agent [] agents; //agents and lines available
	int currentTime; //current minute in sim
	int travelerNum; //number of travelers that have been through security
	int waitTime; //sum of all wait times
	int maxWait; //longest time which a traveler waited in a line
	boolean isEmpty;//true if all lines are empty 
	int numAgents; //number of agents currently working
	double spacing; //space between agents on graphics screen


	/**
	 * @param dayLength - length of simulation in min
	 * @param probability - probability of traveler showing up per min
	 * @param numAgents - number of agents inspecting travelers
	 */
	public AirportSecurity(int dayLength, int probability, int agentCount) {
		NUM_MINUTES = dayLength;
		ARRIVAL_PROB = probability;
		numAgents = agentCount;
		agents = new Agent [numAgents];
		currentTime = 0;
		travelerNum = 0;
		isEmpty= true;
		waitTime = 0;
		spacing = 1000/(double)numAgents;
		for (int i =0; i< agents.length; i++) {
			agents[i] = new Agent();
		}
	}

	/**
	 * finds the shortest line in the airport security 
	 * @return index of agent with shortest line
	 */
	public int findShortestLine() {
		int min = agents[1].getLineSize();
		int shortest = 1;

		for (int i = 2; i< agents.length; i++) {
			int current = agents[i].getLineSize();
			if (current < min) {
				min = current; //resets shortest line num
				shortest = i; //resets index
			}
		}

		return shortest;
	}

	/**
	 * adds a traveler to the shortest line
	 */
	public void addTraveler() {
		Traveler t = new Traveler(travelerNum, currentTime);
		if (t.getIsPre()) {
			agents[0].addTraveler(t);
		}
		else {
			agents[findShortestLine()].addTraveler(t);
		}
		System.out.println(currentTime +": Traveler "+ t.getId() + 
				" arrives (transaction length = " + t.getLength() + ")");		
	}

	/**
	 * check if all lines are empty 
	 * and resets isEmpty var
	 */
	public void checkEmpty() {
		for (Agent a: agents) {
			if (a.getLineSize() > 0 || !a.isAvailable) {
				isEmpty = false;
				return;
			}
		}
		isEmpty = true;
	}

	/**
	 * moves clock forward in simulation
	 * and conducts the necessary changes to 
	 * the security lines
	 */
	public void moveClock() {
		currentTime++; //move time forward	
		refreshGraphics();

		// if still in operation, have the possibility of adding a traveler
		if (currentTime < NUM_MINUTES) {
			double rand = (Math.random()*100 + 1);
			if (rand <= ARRIVAL_PROB) {
				travelerNum++;
				addTraveler();
				isEmpty = false;
			}
		}

		for (int i = 0; i< agents.length; i++) {
			Agent a = agents[i];
			a.moveClock(); //move time forward for all the agents

			updateLines(i);
			// if an agent has a line and has become available, inspect next traveler
			if (a.getAvailability() && a.getLineSize()> 0) { 
				Traveler t = a.inspectTraveler();	
				System.out.println(currentTime +": Traveler "+ t.getId() + 
						" inspected by Agent " + i + " ( Agent " + i + " Busy Until " + a.getBusyUntil() + " )");	
				//collect wait time data
				int wait = t.getWaitTime();
				waitTime += wait;
				if (wait > maxWait) {
					maxWait = wait;
				}
			}

		}

		StdDraw.show(); 
		StdDraw.pause(120);

	}


	/**
	 * Sets up the graphical 
	 * representation of the simulation
	 */
	public void setUpGraphics(){
		StdDraw.setXscale(0,1000);
		StdDraw.setYscale(0,1000);
		StdDraw.enableDoubleBuffering();

		refreshGraphics();
	}

	public void refreshGraphics() {
		StdDraw.picture(0, 0, "DEN.jpeg");

		Font title = new Font ("Alright Sans Light", Font.PLAIN, 25);
		Font text = new Font ("Arial", Font.BOLD, 15);
		StdDraw.setFont(text);

		//Pre-Check Line
		StdDraw.setPenColor(0, 200,0);
		StdDraw.text(spacing/2, 800, "Pre-Check");
		StdDraw.picture(spacing/2, 600, "TSA_Agent.png", 75, 75);
		StdDraw.setPenColor(0,0,0);
		StdDraw.line(spacing, 800, spacing, 0);


		for (int i = 1; i < numAgents; i++) {
			double xPos = spacing/2 + i*spacing;
			StdDraw.setPenColor(0,0,0);
			StdDraw.text(xPos, 800, "Line " + i);
			StdDraw.picture(xPos, 600, "TSA_Agent.png", 75, 75);
			StdDraw.line(xPos + spacing/2, 800, xPos + spacing/2, 0);
		}
		StdDraw.setPenColor(0,0, 255);
		StdDraw.text(100, 875, "TIME: "+ currentTime);
		StdDraw.setPenColor(100, 0, 155);
		StdDraw.setFont(title);
		StdDraw.text(500, 950, "Wecome to Denver International Airport!");

	}

	/**
	 * updates the graphics of a line in TSA
	 * @param index - line number to be altered
	 */
	public void updateLines(int index) {
		Agent a = agents[index];
		if (!a.getAvailability()) {
			StdDraw.picture(index*spacing + spacing/4, 600, "Traveler.png", 50, 50);
		}

		Font label = new Font("Arial", Font.PLAIN, 90/numAgents);

		for (int i = 0; i< a.getLineSize(); i++) {
			StdDraw.setPenColor(200, 0, 0);
			StdDraw.setFont(label);
			StdDraw.text(index*spacing + spacing/2, 700, a.getLineSize() + " Travelers");
			StdDraw.picture(index*spacing + spacing/2, 500 - i*50, "Traveler.png", 50, 50);
		}
	}


	/**
	 * runs the simulation
	 *  using move clock  
	 */
	public void simulate() {
		setUpGraphics();
		for (int i = 0; i < NUM_MINUTES; i++) {
			moveClock();
		}
		checkEmpty();
		System.out.println("------END OF DAY-----");
		while (!isEmpty) {
			moveClock();
			checkEmpty();
		}
		
		int average = 0;
		if (travelerNum >  0) {
			average = waitTime/travelerNum;
		}
			
		String summary = "\nSummary: \n" + "Travelers Served = " + travelerNum + "\nAverage Wait Time = " + average + "\nMax Wait Time =  " + maxWait;
		JOptionPane.showMessageDialog(StdDraw.frame, summary ,"END OF SIMULTATION", JOptionPane.INFORMATION_MESSAGE);
		System.out.print(summary);


		/*System.out.println("\n Summary: \n");
		System.out.println("Travelers Served = " + travelerNum);
		System.out.println("Average Wait Time = " + waitTime/travelerNum);
		System.out.println("Max Wait Time = " + maxWait);*/

	}


	public static void main (String [] args) {
		AirportSecurity DIA = new AirportSecurity (60, 30, 6);
		DIA.simulate();
	}
}