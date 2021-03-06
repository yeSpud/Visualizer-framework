package UI.Objects;

import UI.RelativeNode;
import Utility.Debugger;
import Utility.GenreColors;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Creates the bar that is primarily used when there isn't a track playing, or the track is being loaded.
 *
 * @author Spud
 */
public class LoadingBar extends javafx.scene.shape.Rectangle implements ColoredNode, RelativeNode {

	/**
	 * Creates a new loadingbar object.
	 */
	public LoadingBar() {
		this.setX(117.5);
		this.setY(360);
		this.setWidth(1043.5);
		this.setHeight(3);
		this.setStrokeType(javafx.scene.shape.StrokeType.CENTERED);
		this.setColor(GenreColors.ELECTRONIC);
		this.setStrokeWidth(3);
		this.fillProperty();
	}

	/**
	 * Plays the loading animation of the loading bar.
	 * <p>
	 * It should be noted that once this has finished, the bar will not return to its origin.
	 */
	@Deprecated
	public void playAnimation() {

		// Creating Translate Transition
		TranslateTransition stage1 = this.stage1Animation();

		ParallelTransition stage2 = this.stage2Animation(), stage3 = this.stage3Animation();
		stage2.stop();
		stage3.stop();

		stage3.setOnFinished((e) -> Debugger.d(this.getClass(), "Stage 3 done"));

		stage2.setOnFinished(e -> {
			Debugger.d(this.getClass(), "Stage 2 done");
			stage3.play();
		});

		stage1.setOnFinished(e -> {
			Debugger.d(this.getClass(), "Stage 1 done");
			stage2.play();
		});

		//Playing the animation
		Debugger.d(this.getClass(), "Playing loading animation");
		stage1.playFromStart();

	}

	/**
	 * Sets up the first sequence of the animation.
	 *
	 * @return The first sequence of the animation.
	 */
	private TranslateTransition stage1Animation() {

		// https://www.tutorialspoint.com/javafx/javafx_animations.htm

		// Creating Translate Transition
		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), this);

		// Setting the value of the transition along the x axis.
		translateTransition.setFromX(-this.getWidth() - this.getX());
		translateTransition.setToX(0);

		// Setting auto reverse value to false
		translateTransition.setAutoReverse(false);

		return translateTransition;
	}

	/**
	 * Sets up the second sequence of the animation.
	 *
	 * @return The second sequence of the animation.
	 */
	private ParallelTransition stage2Animation() {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000));
		scaleTransition.setByX(-(1 - (6 / this.getWidth())));
		scaleTransition.setAutoReverse(false);

		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000));
		translateTransition.setFromX(0);
		translateTransition.setToX(this.getWidth() / 2);
		translateTransition.setAutoReverse(false);

		return new ParallelTransition(this, scaleTransition, translateTransition);
	}

	/**
	 * Sets up the third sequence of the animation.
	 *
	 * @return The third sequence of the animation.
	 */
	private ParallelTransition stage3Animation() {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000));
		scaleTransition.setByX(1 - (6 / this.getWidth()));
		scaleTransition.setAutoReverse(false);

		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000));
		translateTransition.setFromX(this.getWidth() / 2);
		translateTransition.setToX(this.getWidth() + (this.getX() * 2));
		translateTransition.setAutoReverse(false);

		return new ParallelTransition(this, scaleTransition, translateTransition);
	}

	@Override
	public void setColor(Color color) {
		Debugger.d(this.getClass(), "Changing loading bar color to " + color.toString());
		this.setFill(color);
	}

	@Override
	public void setColor(GenreColors color) {
		this.setColor(color.getColor());
	}

	@Override
	public void updateWidth(double oldWidth, double newWidth) {
		// TODO
	}

	@Override
	public void updateHeight(double oldHeight, double newHeight) {
		// TODO
	}

	/*
	@Override
	public void updatePosition(double width, double height) {
		// Get the percentage change
		double percentWidthChange = width / 1280, percentHeightChange = height / 720;
		Debugger.d(this.getClass(), "Percentage width change: " + percentWidthChange);
		Debugger.d(this.getClass(), "Percentage height change: " + percentHeightChange);

		// Set the new height of the loadingbar
		this.setHeight(3 * percentHeightChange);

		// Set the new width of the loadingbar
		this.setWidth(1043.5 * percentWidthChange);

		// Initial x position for a 1080 display is 117.5 (plus the index), and y is 360
		double differenceWidth = 117.5 / (1280 / width), differenceHeight = 360 / (720 / height);
		Debugger.d(this.getClass(), "Setting new X position to: " + differenceWidth);
		Debugger.d(this.getClass(), "Setting new Y position to: " + differenceHeight);
		this.setX(differenceWidth);
		this.setY(differenceHeight);
	}
	 */
}
