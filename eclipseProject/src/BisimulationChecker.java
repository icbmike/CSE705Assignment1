import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BisimulationChecker {

	private LabeledTransitionSystem p;
	private LabeledTransitionSystem q;
	private Set<String> actions;

	public BisimulationChecker() {
		actions = new HashSet<>();
	}

	public void readInput(String fileP, String fileQ) {
		try {
			p = createLTS(Files.readAllLines(
					FileSystems.getDefault().getPath(fileP),
					StandardCharsets.UTF_8));
			q = createLTS(Files.readAllLines(
					FileSystems.getDefault().getPath(fileQ),
					StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private LabeledTransitionSystem createLTS(List<String> lines) {
		LabeledTransitionSystem lts = new LabeledTransitionSystem();

		for (String line : lines) {
			if (line.startsWith("!"))
				break;
			String[] split = line.split("[,:]");

			// Add all states
			lts.addState(Integer.parseInt(split[0]));

		}

		for (String line : lines) {
			if (line.startsWith("!"))
				break;
			String[] split = line.split("[,:]");
			// Add all transitions
			actions.add(split[1]);
			lts.addSuccesor(Integer.parseInt(split[0]), split[1],
					Integer.parseInt(split[2]));
		}
		return lts;
	}

	public void performBisimulation() {

		// Create rho and waiting set of sets
		Set<Set<State>> rho = new HashSet<Set<State>>();
		Set<Set<State>> waiting = new HashSet<Set<State>>();

		// Create the initial partition,
		Set<State> initialPartition = new HashSet<State>();
		initialPartition.addAll(p.getStates());
		initialPartition.addAll(q.getStates());

		// Add the initial partition to both rho and waiting
		rho.add(initialPartition);
		waiting.add(initialPartition);

		while (true) {
			if (waiting.isEmpty())
				break; // Termination condition

			// Pop waiting
			Set<State> pPrime = waiting.iterator().next();
			waiting.remove(pPrime);

			for (String action : actions) {
				Set<Set<State>> matchP = new HashSet<>();

				for (Set<State> partitionP : rho) {
					Set<State> tap = tap(action, partitionP, pPrime);
					if (!tap.isEmpty() && !tap.equals(partitionP)) {
						matchP.add(partitionP);
					}
				}
				for (Set<State> partitionP : matchP) {
					Set<Set<State>> splitP = split(partitionP, pPrime, action);
					rho.remove(partitionP);
					rho.addAll(splitP);
					waiting.remove(partitionP);
					waiting.addAll(splitP);
				}

			}

		}
		System.out.println("Whooo" + rho);

	}

	private Set<State> tap(String action, Set<State> partition,
			Set<State> pPrime) {
		HashSet<State> accumulator = new HashSet<State>();
		
		for(State s : partition){
			State successor = s.getSuccessor(action);
			if(successor != null){
				if(pPrime.contains(successor)){
					accumulator.add(s);
				}
			}
		}
		return accumulator;
		
		

	}

	private Set<Set<State>> split(Set<State> partition, Set<State> pPrime,
			String action) {
		
		HashSet<Set<State>> split = new HashSet<Set<State>>();
		Set<State> tap = tap(action, partition, pPrime);
		split.add(tap);
		Set<State> pup =  new HashSet<>();
		pup.addAll(partition);
		pup.addAll(pPrime);
		pup.removeAll(tap);
		split.add(pup);
		
		return split;

	}

	public void writeOutput(String filename) {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BisimulationChecker bc = new BisimulationChecker();
		String testDir = "/Users/michaellittle/Repos/CSE705Assignment1Tests/";
		bc.readInput(testDir + "1_P", testDir + "1_Q");
		bc.performBisimulation();

	}

}
