public class Timer {

	private long startTime;
	private long endTime;
	private long executionTime;

	public Timer() {

	}

	// /////////////////
	// PUBLIC METHODS //
	// /////////////////

	public void begin() {
		startTime = System.currentTimeMillis();
	}

	public void end() {
		endTime = System.currentTimeMillis();
		executionTime = endTime - startTime;
	}

	public void printFormattedExecutionTime() {
		String convert = String.format(
				"Execution time: %d hour(s), %d minute(s), and %d second(s)",
				executionTime / (1000 * 60 * 60),
				(executionTime % (1000 * 60 * 60)) / (1000 * 60),
				((executionTime % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
		System.out.println("\n" + convert);
	}

	// //////////////////
	// GLOBAL GETTERS //
	// //////////////////

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getExecutionTime() {
		return executionTime;
	}
}
