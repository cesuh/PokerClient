package clientNetwork;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public abstract class Client implements Runnable {

	protected Socket socket;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected String IP_ADDRESS;

	public Client(int portNumber, String IP_ADDRESS) throws IOException {

		this.IP_ADDRESS = IP_ADDRESS;
		socket = new Socket(IP_ADDRESS, portNumber);
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());

	}

	public String getIPAddress() {
		return IP_ADDRESS;
	}

	public void sendMessage(String message) {
		if(message != null)
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			System.out.println("Failed to send message from client. " + e);
		}
	}

	protected void letterbox(final Scene scene, final Pane contentPane) {

		final double initWidth = scene.getWidth();
		final double initHeight = scene.getHeight();
		final double ratio = initWidth / initHeight;

		SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth,
				contentPane);
		scene.widthProperty().addListener(sizeListener);
		scene.heightProperty().addListener(sizeListener);
	}


	protected static class SceneSizeChangeListener implements ChangeListener<Number> {

		private final Scene scene;
		private final double ratio;
		private final double initHeight;
		private final double initWidth;
		private final Pane contentPane;

		private SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth,
				Pane contentPane) {
			this.scene = scene;
			this.ratio = ratio;
			this.initHeight = initHeight;
			this.initWidth = initWidth;
			this.contentPane = contentPane;
		}

		@Override
		public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

			final double newWidth = scene.getWidth();
			final double newHeight = scene.getHeight();

			double scaleFactor = newWidth / newHeight > ratio ? newHeight / initHeight : newWidth / initWidth;
	
			if (scaleFactor >= 1) {
				Scale scale = new Scale(scaleFactor, scaleFactor);
				scale.setPivotX(0);
				scale.setPivotY(0);
				scene.getRoot().getTransforms().setAll(scale);
				contentPane.setPrefWidth(newWidth / scaleFactor);
				contentPane.setPrefHeight(newHeight / scaleFactor);
			} else {
				contentPane.setPrefWidth(Math.max(initWidth, newWidth));
				contentPane.setPrefHeight(Math.max(initHeight, newHeight));
			}
		}
	}
}