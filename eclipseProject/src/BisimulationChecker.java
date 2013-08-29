import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;


public class BisimulationChecker {

	
	private LabeledTransitionSystem p;
	private LabeledTransitionSystem q;

	public BisimulationChecker(){
		
	}
	
	public void readInput(String fileP, String fileQ){
		try {
			p = createLTS(Files.readAllLines(FileSystems.getDefault().getPath(fileP), StandardCharsets.UTF_8));
			q = createLTS(Files.readAllLines(FileSystems.getDefault().getPath(fileQ), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private LabeledTransitionSystem createLTS(List<String> lines) {
		LabeledTransitionSystem lts = new LabeledTransitionSystem();
		for(String line : lines){
			if(line.startsWith("!")) break;
			String[] split = line.split("[,:]");
			
			lts.addState(Integer.parseInt(split[0]));
			lts.addSuccesor(Integer.parseInt(split[0]), split[1], Integer.parseInt(split[2]));
		}
		return lts;
	}

	public void performBisimulation(){
		
	}
	
	public void writeOutput(String filename){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BisimulationChecker bc = new BisimulationChecker();
		String testDir = "/Users/michaellittle/Repos/CSE705Assignment1Tests/";
		bc.readInput(testDir + "1_P", testDir + "1_Q");
	}

}
