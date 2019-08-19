import UI.Objects.AlbumArt;
import UI.Objects.Bar;
import UI.Window;
import javafx.application.Application;
import javafx.stage.Stage;
import name_me.Debugger;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class WindowTest extends Application {

	@Test
	void displayWindow() {
		launch();
	}

	@Test
	void AlbumArt() {
		launch("albumart_test");
	}

	@Test
	void Bar() {
		launch("bar_test");
	}

	@Test
	void MultiBar() {
		launch("bars_test");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// First, create the window
		UI.Window window = new Window(primaryStage);
		window.stage.show();
		window.updateOpacity(0.5d);

		Debugger.DEBUG = false;

		// Now check for arguments (tests)
		for (String parameter : getParameters().getRaw()) {
			switch (parameter) {
				case "albumart_test":  // Show the album art object
					window.addToWindow(new AlbumArt());
					break;
				case "bar_test":  // Show the bar object
					window.addToWindow(new Bar());
					//window.addToWindow(new AlbumArt());
					break;
				case "bars_test":  // Show multiple bars
					window.addToWindow(new AlbumArt());
					ArrayList<Bar> bars = new ArrayList<Bar>();
					for (int i = 0; i < 63; i++) {
						Bar bar = new Bar(i);
						window.addToWindow(bar);
						bars.add(bar);
					}
					bars.get(12).setBarHeight(1);
					break;
				default:
					System.out.println("Unrecognized parameter: " + parameter);
					break;
			}
		}
	}
}