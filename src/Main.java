import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		Timer timer = new Timer();
		timer.begin();

		// PERFORM SCRAPE

		timer.end();
		timer.printFormattedExecutionTime();
	}
}
