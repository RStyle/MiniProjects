import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

// created by 2 other (anonymous) students and me

public class MandelbrotJulia extends Application {
	
	private final int iterations = 256;		// wie weit wird die Reihe geprüft?

	public void start(Stage primaryStage)throws Exception{
	
		GridPane pane = new GridPane();
		
		TextField xstart = new TextField("-3");
		TextField ystart = new TextField("-2");
		TextField xend = new TextField("2");
		TextField yend = new TextField("2");
		TextField width = new TextField("600");
		TextField height = new TextField("450");
		TextField varA = new TextField("-0.52");
		TextField varB = new TextField("0.57");
		TextField colorOffsetButton = new TextField("0.0");
		TextField gridpaneoffset = new TextField(" ");
		gridpaneoffset.setVisible(false);
		Button mandelbrot = new Button("Male Mandelbrot");
		Button julia = new Button("Male Julia");

		pane.add(new Label("X Start: "), 1, 1); pane.add(xstart, 2, 1); pane.add(new Label ("X Ende:"),3 ,1 ); pane.add(xend,4 ,1 );
		pane.add(new Label("Y Start: "),1 ,2 ); pane.add(ystart, 2, 2); pane.add(new Label ("Y Ende: "),3 ,2); pane.add(yend,4 ,2 );
		pane.add(new Label("Breite: "),1 ,3 ); pane.add(width,2 ,3 ); pane.add(new Label("Höhe: "),3 ,3 ); pane.add(height,4 ,3 );
		pane.add(new Label("Variable a: "),1 ,4 );pane.add(varA,2 ,4 );pane.add(new Label("Variable b"),3 ,4 );pane.add(varB,4 , 4);
		pane.add(new Label("hsl-Farbabweichung (0-360): "),2 ,5 );pane.add(colorOffsetButton, 3,5 );
		pane.add(mandelbrot,2 ,6 );pane.add(julia,3 ,6 );pane.add(gridpaneoffset,1 ,5 );
		pane.setHgap(15); pane.setVgap(5);
		
		mandelbrot.setOnAction(event ->{
			try{
				if ((Double.parseDouble(xstart.getText()) < Double.parseDouble(xend.getText())) 
				&& (Double.parseDouble(ystart.getText()) < Double.parseDouble(yend.getText()))
				&& (Integer.valueOf(width.getText()) > 0) 
				&& (Integer.valueOf(height.getText()) > 0)) {
					Double xs = Double.parseDouble(xstart.getText());
					Double ys = Double.parseDouble(ystart.getText());
					Double xe = Double.parseDouble(xend.getText());
					Double ye = Double.parseDouble(yend.getText());
					Integer w = Integer.valueOf(width.getText());
					Integer h = Integer.valueOf(height.getText());
					Double colorChange = Double.parseDouble(colorOffsetButton.getText());
					calculateMaJu(xs, ys, xe, ye, w, h, colorChange, null, null); // Apfelmaennchen
				}
				else 
					System.err.println("Ungültige Eingabe");
			}
			catch(Exception e){
				System.err.println("Ungültige Eingabe. Fehler.");
			}
		});
		
		julia.setOnAction(event ->{
			try{
				if ((Double.parseDouble(xstart.getText()) < Double.parseDouble(xend.getText())) 
				&& (Double.parseDouble(ystart.getText()) < Double.parseDouble(yend.getText()))
				&& (Integer.valueOf(width.getText()) > 0) 
				&& (Integer.valueOf(height.getText()) > 0)) {
					Double xs = Double.parseDouble(xstart.getText());
					Double ys = Double.parseDouble(ystart.getText());
					Double xe = Double.parseDouble(xend.getText());
					Double ye = Double.parseDouble(yend.getText());
					Integer w = Integer.valueOf(width.getText());
					Integer h = Integer.valueOf(height.getText());
					Double a = Double.parseDouble(varA.getText());
					Double b = Double.parseDouble(varB.getText());
					Double colorChange = Double.parseDouble(colorOffsetButton.getText());
					calculateMaJu(xs, ys, xe, ye, w, h, colorChange, a, b);	// Julia Set
				}
				else
					System.err.println("Ungültige Eingabe");
			}
			catch (Exception e){
				System.err.println("Ungültige Eingabe. Fehler.");
			}
		});
		
		Scene scene = new Scene(pane, 675, 180);
		primaryStage.setTitle("Mandelbrot und Julia v1.0.1");
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
		
	}
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	// checke, ob Reihe mit diesen Werten divergiert
	// gibt Farb-CSS Argument für die Anzahl der Iterationen aus
	private String iterate(double x, double y, double a, double b, int iterations, double colorOffset) {
		
		// z = z^2+C
		
		// z=x+yi
		// C=a+bi
		
		// z^2+C=(x+yi)*(x+yi)+a+bi
		//     =xx+yiyi+2xyi+a+bi
		//     =xx-yy+i(2xy+b)+a
		//     =(x^2-y^2+a)+i(2xy+b)
		
		double tmp;
		for (int i = 0; i < iterations; i++) {
			    tmp = x*x-y*y+a;
			    y = 2*x*y+b;
			    x = tmp;
			    if(x*x+y*y > 4.0)		// Abstand zum Nullpunkt (complex plane) darf nicht > 2 sein
            		return "hsl(" + ((double) i / (double) iterations * 360.0 + colorOffset) % 360.0 + ", 100%, 100%)";	// Reihe divergiert
		}
		return "rgb(0, 0, 0)";
	}
	
	// male Mandelbrot oder Julia
	// wenn a oder b = null, malen wir ein Mandelbrot (da Werte für Julia fehlen)
	// ansonsten malen wir ein Julia Set
	// a = reeler Teil der komplexen Konstante C
	// b = imaginärer Teil der komplexen Konstante C
	public void calculateMaJu(double xStart, double yStart, double xEnd, double yEnd, int width, int height, double colorOffset, Double a, Double b) {
										
		Pane pane;
		Stage stage;
		HBox hbox;
		Canvas canvas;
		Button save;
		TextField tf;
		
		double stepX = (xEnd - xStart) / width;
		double stepY = (yEnd - yStart) / height;
		String color;
		boolean isMandelbrot;
		
		if(a == null || b == null)
			isMandelbrot = true;
		else
			isMandelbrot = false;
			
		pane = new Pane();
		stage = new Stage();
		hbox = new HBox(10);
		canvas = new Canvas(width, height);
		hbox.setPadding(new Insets(height + 30, 0, 0, 50));

		save = new Button("Speichern");
		if(isMandelbrot)
			tf = new TextField("Mandelbrot");
		else
			tf = new TextField("Julia");
		hbox.getChildren().addAll(save, new Label("Speichern als:"), tf, new Label(".jpg"));
		PixelWriter pi = canvas.getGraphicsContext2D().getPixelWriter();
			
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				
				if(isMandelbrot)	// Mandelbrot Z0 = [0, 0], C = [x, y]
					color = iterate(0, 0, xStart + stepX * i, yEnd - stepY * j, iterations, colorOffset);
				else				// Julia Set  Z0 = [x, y]. C = [a, b]
					color = iterate(xStart + stepX * i, yEnd - stepY * j, a, b, iterations, colorOffset);
				Color c;
				c = Color.web(color);
				pi.setColor(i, j, c);
			}
		}
		
		Stage stage1 = new Stage();
		
		save.setOnAction(event ->{
			
			WritableImage wim = new WritableImage(width, height);
			wim = canvas.snapshot(null, null);
			BufferedImage bim = SwingFXUtils.fromFXImage(wim, null);
			// fix jpg Farbskala:
			BufferedImage imageRGB = new BufferedImage( bim.getWidth(), bim.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = imageRGB.createGraphics();
			graphics.drawImage( bim, 0, 0, null);
			graphics.dispose();	
			try {
				ImageIO.write(imageRGB, "jpg", new File(tf.getText() + ".jpg"));
				Label label = new Label("Bild wurde erfolgreich gespeichert.");
				Scene scene = new Scene(label);
				stage1.setScene(scene);
				stage1.show();
			} catch (IOException e) {
				Label label = new Label("Bild konnte nicht gespeichert werden.");
				Scene scene = new Scene(label);
				stage1.setScene(scene);
				stage1.show();
			}
		});
		
		pane.getChildren().add(canvas);
		pane.getChildren().add(hbox);
		
		Scene scene = new Scene(pane, width - 10, height + 100);
		
		if(isMandelbrot)
			stage.setTitle("Mandelbrot");
		else
			stage.setTitle("Julia");
		tf.setOnAction(event ->{
			Event.fireEvent(save, new ActionEvent());
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent arg0) {
				if (arg0.getCode() == KeyCode.S && arg0.isControlDown()){
					Event.fireEvent(save, new ActionEvent());
				}
			}
			
		});
		stage.setScene(scene);
		stage.show();
		stage.setResizable(false);
	}
}