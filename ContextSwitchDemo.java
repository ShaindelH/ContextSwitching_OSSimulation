package contextSwitching;

import java.util.ArrayList;
import java.util.Random;

public class ContextSwitchDemo {

	public static void main(String[] args) {

		SimProcessor processor = new SimProcessor();

		ArrayList<ProcessControlBlock> ready = new ArrayList<>();
		ArrayList<ProcessControlBlock> blocked = new ArrayList<>();

		Random rand = new Random();

		populate(ready);
		run(ready, blocked, processor, rand);
	}

	public static void populate(ArrayList<ProcessControlBlock> ready) {

		// populate list of ready processes
		ready.add(new ProcessControlBlock(new SimProcess(1, "Word", 101)));
		ready.add(new ProcessControlBlock(new SimProcess(2, "Excel", 202)));
		ready.add(new ProcessControlBlock(new SimProcess(3, "PowerPoint", 303)));
		ready.add(new ProcessControlBlock(new SimProcess(4, "Paint", 212)));
		ready.add(new ProcessControlBlock(new SimProcess(5, "FireFox", 134)));
		ready.add(new ProcessControlBlock(new SimProcess(6, "Calculator", 400)));
		ready.add(new ProcessControlBlock(new SimProcess(7, "Spotify", 104)));
		ready.add(new ProcessControlBlock(new SimProcess(8, "Youtube", 208)));
		ready.add(new ProcessControlBlock(new SimProcess(9, "Canvas", 178)));
		ready.add(new ProcessControlBlock(new SimProcess(10, "Splitwise", 350)));

	}

	public static void run(ArrayList<ProcessControlBlock> ready, ArrayList<ProcessControlBlock> blocked,
			SimProcessor processor, Random rand) {

		int quantumCounter = 0;
		final int QUANTUM = 5;

		processor.setSimProcess(ready.get(0).getSimProcess());
		processor.setCurrInstruction(ready.get(0).getCurrInstruction());
		ProcessState state = ProcessState.READY;

		for (int i = 1; i <= 3000; i++) {

			if (ready.size() == 0) {
				System.out.println("*** Processor is idling ***");
				wakeUp(ready, blocked, rand);
			}

			System.out.print("Step " + i + " ");

			if (state == ProcessState.FINISHED) {
				state = cSwitch(processor, ready, blocked, state);
				quantumCounter = 0;

				// count another step for restore
				i++;
				System.out.print("Step " + i + " ");
				restoreProcess(ready, processor);

			} else if (state == ProcessState.BLOCKED) {
				state = cSwitch(processor, ready, blocked, state);
				quantumCounter = 0;

				// count another step for restore
				i++;
				System.out.print("Step " + i + " ");
				restoreProcess(ready, processor);

			} else if (quantumCounter == QUANTUM) {
				state = cSwitch(processor, ready, blocked, state);
				quantumCounter = 0;

				// count another step for restore
				i++;
				System.out.print("Step " + i + " ");
				restoreProcess(ready, processor);

			}
			// increment quantum counter and execute instruction
			else {
				quantumCounter++;
				state = processor.exectueNextInstruction();

				// display current situation
				if (quantumCounter == QUANTUM)
					System.out.println("*** Quantum expired ***");
				else if (state == ProcessState.FINISHED)
					System.out.println("*** Process completed ***");
				else if (state == ProcessState.BLOCKED)
					System.out.println("*** Process blocked ***");

			}

			// wake up 30% of blocked list
			wakeUp(ready, blocked, rand);
		}
	}

	public static ProcessState cSwitch(SimProcessor processor, ArrayList<ProcessControlBlock> ready,
			ArrayList<ProcessControlBlock> blocked, ProcessState state) {

		saveProcess(ready, processor);

		// switch process
		switch (state) {
		case FINISHED:
			ready.remove(0);
			break;
		case BLOCKED:
			blocked.add(ready.get(0));
			ready.remove(0);
			break;
		default:
			ready.add(ready.get(0));
			ready.remove(0);
		}

		return ProcessState.READY;
	}

	public static void saveProcess(ArrayList<ProcessControlBlock> ready, SimProcessor processor) {

		// update PCB with register values and current instruction from the Processor

		ProcessControlBlock currPCB = ready.get(0);

		for (int i = 1; i <= 4; i++) {
			currPCB.setRegisterValue(i, processor.getRegisterValue(i));
		}

		currPCB.setCurrInstruction(processor.getCurrInstruction());

		// display instruction
		System.out.println("Context switch: saving process: " + currPCB.getSimProcess().getProcName());
		System.out.println("\tInstruction: " + currPCB.getCurrInstruction() + " R1: " + currPCB.getRegisterValue(1)
				+ " R2: " + currPCB.getRegisterValue(2) + " R3: " + currPCB.getRegisterValue(3) + " R4: "
				+ currPCB.getRegisterValue(4));
	}

	public static void restoreProcess(ArrayList<ProcessControlBlock> ready, SimProcessor processor) {

		// place new process on processor
		if (ready.size() > 0) {
			ProcessControlBlock currPCB = ready.get(0);
			processor.setSimProcess(currPCB.getSimProcess());
			processor.setCurrInstruction(currPCB.getCurrInstruction());

			for (int i = 1; i <= 4; i++) {
				processor.setRegisterValue(i, currPCB.getRegisterValue(i));
			}

			// display instruction
			System.out.println("Restoring process: " + currPCB.getSimProcess().getProcName());
			System.out.println("\tInstruction: " + currPCB.getCurrInstruction() + " R1: " + currPCB.getRegisterValue(1)
					+ " R2: " + currPCB.getRegisterValue(2) + " R3: " + currPCB.getRegisterValue(3) + " R4: "
					+ currPCB.getRegisterValue(4));
		}
	}

	public static void wakeUp(ArrayList<ProcessControlBlock> ready, ArrayList<ProcessControlBlock> blocked,
			Random rand) {

		// wake up 30% of blocked list
		int probability;

		for (int i = 0; i < blocked.size(); i++) {

			probability = rand.nextInt(100);

			if (probability < 30) {
				ready.add(blocked.get(i));
				blocked.remove(blocked.get(i));
			}
		}
	}
}
