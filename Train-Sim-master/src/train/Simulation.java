package train;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * @Author Bradley Rawson
 * Simulation actions / objectives
 *  - prompt for number of stations, trains, length of route, rate of passenger arrival
 *  - keep track of ticks, execute actions at correct times.
 */
public class Simulation extends Pane{
	
	private static int numberOfStations;
	private static int numberOfTrains;
	private static int distanceBetweenStations;
	private static int numberOfTicks;
	private static int pplPerTick;
	private static boolean useGUI;
	private static PrintWriter log;
	private Station[] stations;
	private TrainRoute route;
	
	/**
	 * Sets up all the option variables and then sets the simulation in motion
	 * 
	 * @param args
	 */
	
	
	/**
	 * Initializes all the stations and trains as well as the route.
	 * The trains are assigned to stations in an orderly fashion, half are set to go
	 * forward and the other half backwards.
	 * 
	 * @param log
	 */
	public Simulation() {
		// Creates the log file if it does not exist
		File options = new File("Options.txt");
		if(!options.exists()) {
			try(Scanner sc = new Scanner(System.in)) {
				System.out.println(options.createNewFile());
				PrintWriter pr = new PrintWriter(options);
				pr.println("Stations: 8");
				pr.println("Trains: 4");
				pr.println("Distance: 5");
				pr.println("Ticks: 50");
				pr.println("People/Tick: 5");
				pr.println("Use_GUI?: true");
				pr.close();
			} catch(Exception ex) {
				System.out.println("Failed to create Options.txt");
				ex.printStackTrace();
			}
		}
				
		// Reads the log file to set option variables
		try(Scanner sc = new Scanner(options)) {
			sc.next();
			numberOfStations = sc.nextInt();
			sc.next();
			numberOfTrains = sc.nextInt();
			sc.next();
			distanceBetweenStations = sc.nextInt();
			sc.next();
			numberOfTicks = sc.nextInt();
			sc.next();
			pplPerTick = sc.nextInt();
			sc.next();
			useGUI = sc.nextBoolean();
		} catch(Exception ex) {
			System.out.println("Failed to read Options.txt");
			ex.printStackTrace();
		}
				
		// Creates the log file if it does not exist
		File simLog = new File("log.txt");
		if(!simLog.exists()) {
			try {
				simLog.createNewFile();
			} catch(Exception ex) {
				System.out.println("Failed to create log.txt");
				ex.printStackTrace();
			}
		}
		
		try {
			// Creates the log that stores simulation data
			log = new PrintWriter(simLog);
			
			// Puts a minimum of two on the number of trains
			if(numberOfTrains < 2) {
				numberOfTrains = 2;
				log.println("Must have at least one inbound and one outbound train. Number of trains set to two.");
			}
			
			// If the number of trains is greater than the capacity of the stations than the number of trains is set to the capacity
			if(numberOfTrains >= numberOfStations*2) {
				numberOfTrains = numberOfStations*2;
				log.println("Train limit exceeded, setting to " + numberOfTrains);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		// Prints specifications of simulation to the log
		log.println("Stations: "+numberOfStations);
		log.println("Trains: "+numberOfTrains);
		log.println("Distance: "+distanceBetweenStations);
		log.println("Ticks: "+numberOfTicks);
		log.println("People/Tick: "+pplPerTick);
		log.println();
		log.println();
		
		// GUI stuff
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		vBox.getChildren().addAll(hBox,makeButton());
		getChildren().add(vBox);
		
		// Initializes the station array
		stations = new Station[numberOfStations];
		
		// Populates the stations array
		for(int i = 0 ; i < numberOfStations ; i++) {
			stations[i] = new Station(i,log);
			hBox.getChildren().add(stations[i]);
		}
		
		log.println("Trains initializing passengers not present.");
		// Populates the stations with trains
		for(int i = 0 ; i < numberOfTrains ; i++) {
			// Makes the train
			Train train = new Train(i,i%numberOfStations,log,numberOfStations,i<numberOfTrains/2);
			
			// Assign train to a station
			stations[i%numberOfStations].acceptTrain(train);
		}
		
		// Skips two lines in the log
		log.println();
		log.println();
		
		// Initializes the train route
		route = new TrainRoute(stations,distanceBetweenStations,log);
		vBox.getChildren().add(new Label("Train Route"));
		vBox.getChildren().add(route);
		
		try {
			if(!useGUI) {
				start();
				log.close();
				System.exit(0);
			}
			
			log.close();
		} catch(Exception ex) {
			System.out.println("The simulation has crashed.");
			ex.printStackTrace();
		}
	}
	
	private Button makeButton() {
		Button button = new Button("Next Tick");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(int i = 0 ; i < pplPerTick ; i++) {
					// Randomly assigns the passenger to a station
					stations[(int)(Math.random()*numberOfStations)].addPassenger(new Passenger((int)(Math.random()*numberOfStations)));
				}
					
				// Performs the operations of one tick
				route.nextTick();
					
				// Skips two lines in the log
				log.println();
				log.println();
			}
		});
		return button;
	}

	/**
	 * Runs the simulation, this is where all the ticks are processed.
	 */
	private void start() {
		for(int i = 0 ; i < numberOfTicks ; i++) {
			// Prints the current tick number to the log
			log.println("Tick number " + i + " out of " + numberOfTicks + ".");
			log.println("Adding " + pplPerTick + " passengers to the simulation.");
			for(int j = 0 ; j < pplPerTick ; j++) {
				// Randomly assigns the passenger to a station
				stations[(int)(Math.random()*numberOfStations)].addPassenger(new Passenger((int)(Math.random()*numberOfStations)));
			}
			
			// Performs the operations of one tick
			route.nextTick();
			
			// Skips two lines in the log
			log.println();
			log.println();
		}
	}
	
}