import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		Timer timer = new Timer();
		timer.begin();

		// PERFORM SCRAPE
		FacebookTask.scrape();

		timer.end();
		timer.printFormattedExecutionTime();
	}
}
