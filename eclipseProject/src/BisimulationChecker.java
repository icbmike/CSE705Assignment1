import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BisimulationChecker {

	private LabeledTransitionSystem p;
	private LabeledTransitionSystem q;
	private Set<String> actions;
	private Set<Set<State>> rho;

	public BisimulationChecker() {
		actions = new HashSet<>();
	}

	public void readInput(String fileP, String fileQ) {
		try {
			p = createLTS(Files.readAllLines(
					FileSystems.getDefault().getPath(fileP),
					StandardCharsets.UTF_8), "P");
			q = createLTS(Files.readAllLines(
					FileSystems.getDefault().getPath(fileQ),
					StandardCharsets.UTF_8), "Q");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private LabeledTransitionSystem createLTS(List<String> lines, String name) {
		LabeledTransitionSystem lts = new LabeledTransitionSystem(name);

		for (String line : lines) {
			if (line.startsWith("!"))
				break;
			String[] split = line.split("[,:]");

			// Add all states
			lts.addState(Integer.parseInt(split[0]), name);

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

		rho = new HashSet<Set<State>>();
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

	}

	private Set<State> tap(String action, Set<State> partition,
			Set<State> pPrime) {
		HashSet<State> accumulator = new HashSet<State>();
		
		for(State s : partition){
			Set<State> successors = s.getSuccessors(action);
			if(successors != null){
				for(State successor : successors)
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
		StringBuilder sb = new StringBuilder();
		sb.append(processOutput(p));
		sb.append("\n");
		sb.append(processOutput(q));
		sb.append("\nBisimulation Results\n");
		for (Set<State> partition : rho){
			sb.append(joinStates(partition, ","));
			sb.append("\n");
		}
		sb.append("Bisumulation Answer\n");
		sb.append(isBisimilar() ? "Yes" : "No");
		
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
			bufferedWriter.write(sb.toString());
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private boolean isBisimilar() {
		boolean isBisimilar = true;
        for (Set<State> states : rho) {
            HashSet<Character> prefixes = new HashSet<Character>();

            for (State s : states) {
                prefixes.add(s.toString().charAt(0));
            }

            isBisimilar = prefixes.contains(p.getName()) && prefixes.contains(q.getName()) && isBisimilar;
            
        }
		
		return isBisimilar;
	}

	private String processOutput(LabeledTransitionSystem process) {
		//Output for Process
		StringBuilder sb = new StringBuilder("Process "+process.getName()+"\n");
		//States
		sb.append("S = ");
		sb.append(joinStates(process.getStates(), ","));
		//Actions
		sb.append("\nA = ");
		Set<String> pActions =  new HashSet<>();
		for (State state : process.getStates()) {
			pActions.addAll(state.getTransitions());
		}
		sb.append(joinActions(pActions, ","));
		//Transitions
		sb.append("\nT = ");
		Set<String> pTransitions =  new HashSet<>();
		for (State state : process.getStates()) {
			for(String action: state.getTransitions()){
				for(State successor : state.getSuccessors(action)){
					pTransitions.add("(" + state.niceOutput() + "," + "action(" + action + ")" + "," + successor.niceOutput()+")");
				}
				
			}
		}
		sb.append(join(pTransitions, ","));
		return sb.toString();
	}
	
	static String joinStates(Collection<State> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<State> iter = s.iterator();
	     while (iter.hasNext()) {
	         builder.append(iter.next().niceOutput());
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }
	
	static String join(Collection<String> s, String delimiter) {
		StringBuilder builder = new StringBuilder();
		Iterator<String> iter = s.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());
			if (!iter.hasNext()) {
				break;                  
			}
			builder.append(delimiter);
		}
		return builder.toString();
	}
	
	
	
	static String joinActions(Collection<String> s, String delimiter) {
	     StringBuilder builder = new StringBuilder();
	     Iterator<String> iter = s.iterator();
	     while (iter.hasNext()) {
	    	 builder.append("action(");
	         builder.append(iter.next());
	         builder.append(")");
	         if (!iter.hasNext()) {
	           break;                  
	         }
	         builder.append(delimiter);
	     }
	     return builder.toString();
	 }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BisimulationChecker bc = new BisimulationChecker();
		String testDir = "/Users/michaellittle/Repos/CSE705Assignment1Tests/";
		bc.readInput(testDir + "bookP", testDir + "bookQ");
		bc.performBisimulation();
		bc.writeOutput("");

	}

}
