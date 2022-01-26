package contextSwitching;
import java.util.Random;

public class SimProcessor {

	private SimProcess simProcess;
	private int register1;
	private int register2;
	private int register3;
	private int register4;
	private int currInstruction;
	private Random rand = new Random();

	public void setSimProcess(SimProcess sProcess) {
		simProcess = sProcess;
	}
	
	public SimProcess getSimProcess() {
		return simProcess;
	}
	
	public void setRegisterValue(int registerNum, int val) {
		
		switch (registerNum){
			
			case 1:
				register1 = val;
				break;
			case 2:
				register2 = val;
				break;
			case 3:
				register3 = val;
				break;
			case 4:
				register4 = val;
		}
	}
	
	public int getRegisterValue(int registerNum) {
		
		switch (registerNum){
		
			case 1:
				return register1;
			case 2:
				return register2;
			case 3:
				return register3;
			default:
				return register4;
		}
	}
	
	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}
	
	public int getCurrInstruction() {
		return currInstruction;
	}
	
	public ProcessState exectueNextInstruction() { 
		
		ProcessState state = simProcess.execute(currInstruction);
		currInstruction++;
		
		setRegisterValue(1, rand.nextInt());
		setRegisterValue(2, rand.nextInt());
		setRegisterValue(3,rand.nextInt());
		setRegisterValue(4, rand.nextInt());
		
		return state;
		
	}
	
	
	
	
}
