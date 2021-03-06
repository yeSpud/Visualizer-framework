package UI.Window;

import UI.Objects.ColoredNode;
import UI.RelativeNode;
import UI.Text.ConsolePane;
import Utility.Debugger;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the window that will house all the necessary display elements, but will not be immediately shown to the user.
 * <p>
 * To show the window, call the show() function from the window's stage.
 *
 * @author Spud
 */
@SuppressWarnings("unused")
public class Window {

	/**
	 * The stage used by this object. This is the same stage passed as an argument in the constructor, but is then saved for later use.
	 */
	public Stage stage;

	/**
	 * The root of the window is a BorderPane object, and as such utilizes all of its properties.
	 */
	private BorderPane WindowRoot = new BorderPane();

	/**
	 * A list of all the relative nodes to keep track of for event handling reasons.
	 */
	private List<RelativeNode> relativeNodes = new ArrayList<>();

	/**
	 * A list of all the colored nodes to keep track of for event handing reasons.
	 */
	private List<ColoredNode> coloredNodes = new ArrayList<>();

	/**
	 * TODO Documentation
	 */
	public List<Node> nodes = new ArrayList<>();

	/**
	 * TODO Documentation
	 */
	public static ConsolePane console;

	/**
	 * The scene used by this object. This is inferred from the stage that is passed in the constructor.
	 */
	private Scene scene;

	/**
	 * Creates a window object with the dimensions of 1280 x 720. Note that while a stage needs to be provided, it will not be shown by default.
	 *
	 * @param stage The requires stage. This can either be made new, or be passed from the start function of the JavaFX application
	 */
	public Window(Stage stage) {
		this(1280, 720, stage);
	}

	/**
	 * Creates a window object with given dimensions. Note that while a stage needs to be provided, it will not be shown by default.
	 *
	 * @param width  The initial width of the window.
	 * @param height The initial height of the window.
	 * @param stage  The requires stage. This can either be made new, or be passed from the start function of the JavaFX application
	 */
	public Window(int width, int height, Stage stage) {
		Debugger.d(this.getClass(), String.format("Creating window with dimensions %s x %s", width, height));

		// Set the scene width and height (layout width and height)
		this.scene = new Scene(this.WindowRoot, width, height);

		// Set a minimum size for the window
		stage.setMinWidth(160);
		stage.setMinHeight(90);

		// Add an error prompt to the window
		Window.console = new ConsolePane();
		this.addToWindow(Window.console);

		// Apply the stage
		this.stage = stage;
		this.stage.setScene(this.scene);

		// Set the default background
		this.setStageBackground(Color.BLACK);

		// Setup a listener for when the window changes dimensions
		javafx.beans.value.ChangeListener<Number> dimensionChangeListener = new DimensionChangeListener(this.relativeNodes.toArray(new RelativeNode[0]));

		// TODO Look into operating this listener on a new thread for improved performance?
		this.stage.widthProperty().addListener(dimensionChangeListener);
		this.stage.heightProperty().addListener(dimensionChangeListener);

		// Set it so when the a close is requested, it just shuts down.
		this.stage.setOnCloseRequest((event -> javafx.application.Platform.exit()));

		// If in debug mode, replace the background with the gray and white checkerboard pattern for sizing checks
		if (Debugger.DEBUG) {
			try {
				// Get the background image file
				java.net.URI ImageURI = this.getClass().getResource("DebugBackgroundImage.png").toURI();
				File debugImageFile = new File(ImageURI);
				Debugger.d(this.getClass(), "Debug Image File: " + debugImageFile.getPath());

				// Load the image
				Image debug_image = new Image(new java.io.FileInputStream(debugImageFile));
				BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
						false, false, true, false);
				this.WindowRoot.setBackground(new Background(new BackgroundImage(debug_image, BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, size)));
			} catch (Exception e) {
				Debugger.e(e);
			}
		}
	}

	/**
	 * Updates the window opacity.
	 *
	 * @param opacity The opacity value from 0 to 1.0.
	 */
	public void updateOpacity(double opacity) {
		Debugger.d(this.getClass(), String.format("Setting opacity to %d%%", Math.round(opacity * 100)));
		this.stage.setOpacity(opacity);
	}

	/**
	 * Changes the background color of the window.
	 *
	 * @param color The color to set the background to.
	 */
	public void setStageBackground(Color color) {
		Debugger.d(this.getClass(), "Changing background color to " + color.toString());
		this.WindowRoot.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(color,
				javafx.scene.layout.CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
	}

	/**
	 * Helper function to toggle the visibility a specific node in the window, effectively hiding or showing it.
	 *
	 * @param node The node to apply the visibility changes to
	 * @param hide Whether or not to hide the node.
	 */
	public void hideElement(Node node, boolean hide) {
		Debugger.d(this.getClass(), String.format("Hide %s? %s", node.toString(), hide));
		node.setVisible(!hide);
	}

	/**
	 * Adds UI elements to the window. If the element is an instance of either a RelativeNode, or a ColoredNode it will be added to a list for event handling.
	 * <p>
	 * See: https://stackoverflow.com/questions/6270132/create-a-custom-event-in-java for more details on event handling.
	 *
	 * @param element The UI element to be added to the window.
	 */
	public void addToWindow(Node element) {
		Debugger.d(this.getClass(), "Adding new element to window: " + element.getClass());
		this.WindowRoot.getChildren().add(element);

		// If the element is a relative node, add it to the list for event handling
		if (element instanceof RelativeNode) {
			Debugger.d(this.getClass(), "Adding relative listener");
			this.relativeNodes.add((RelativeNode) element);
		}

		// If the element is a colored node, add it the the list for event handling
		if (element instanceof ColoredNode) {
			Debugger.d(this.getClass(), "Adding color listener");
			this.coloredNodes.add((ColoredNode) element);
		}
	}

	/**
	 * Sets the genre for all the ColoredNodes in the window.
	 *
	 * @param genre The genre to set all the ColoredNodes to.
	 */
	public void setGenre(Utility.GenreColors genre) {
		Debugger.d(this.getClass(), "Updating genre colors to: " + genre.name());
		for (ColoredNode node : this.coloredNodes) {
			node.setColor(genre);
		}
	}

	/**
	 * Adds a listener for key presses to the window.
	 *
	 * @param listener The listener. This must be a EventHandler interface.
	 */
	public void addKeyListener(javafx.event.EventHandler<javafx.scene.input.KeyEvent> listener) {
		Debugger.d(this.getClass(), "Adding key listener");
		this.scene.setOnKeyTyped(listener);
	}
}
