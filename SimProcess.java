package contextSwitching;
import java.util.Random;

public class SimProcess {

	private int pid;
	private String procName;
	private int totalInstructions;
	
	public SimProcess(int pid, String procName, int totalInstructions) {
		
		this.pid = pid;
		this.procName = procName;
		this.totalInstructions = totalInstructions;
	}
	
	public ProcessState execute(int i) {
		
		System.out.print("PID: " + pid);
		System.out.print(", Process Name: " + procName);
		System.out.println(", executing instruction: " + i);
		
		Random rand = new Random();
		int probability = rand.nextInt(100);
		
		if (i >= totalInstructions) {
			
			return ProcessState.FINISHED;
		}
		
		else if (probability < 15) {
			
			return ProcessState.BLOCKED;
		}
		
		else {
		
			return ProcessState.READY;
		
		}
	}
	
	public String getProcName() {
		return procName;
	}
}
