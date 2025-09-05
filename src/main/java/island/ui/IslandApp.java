package island.ui;

import island.config.Config;
import island.world.Cell;
import island.world.Island;
import island.world.ZoneType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IslandApp extends Application {

    private Island island;
    private Canvas canvas;
    private Timeline timeline;
    private Label stats;
    private Label detailedStats;

    @Override
    public void start(Stage stage) {
        island = new Island(Config.WIDTH, Config.HEIGHT);

        BorderPane root = new BorderPane();
        int w = Config.WIDTH * 12, h = Config.HEIGHT * 12;
        canvas = new Canvas(w, h);
        root.setCenter(canvas);

        Button btnPlay = new Button("▶ Пуск");
        Button btnPause = new Button("⏸ Пауза");
        Button btnStep = new Button("⏭ Шаг");
        stats = new Label("—");
        detailedStats = new Label("");

        btnPlay.setOnAction(e -> timeline.play());
        btnPause.setOnAction(e -> timeline.pause());
        btnStep.setOnAction(e -> { island.simulateTurn(); draw(); });

        HBox bar = new HBox(10, btnPlay, btnPause, btnStep, stats, detailedStats);
        bar.setPadding(new Insets(8));
        root.setTop(bar);

        Scene scene = new Scene(root, w+16, h+60);
        stage.setTitle("Island Simulation (JavaRush)");
        stage.setScene(scene);
        stage.show();

        timeline = new Timeline(new KeyFrame(Duration.millis(Config.TICK_MILLIS), e -> {
            island.simulateTurn();
            draw();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        draw();
    }

    private void draw() {
        Cell[][] grid = island.getGrid();
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int cellSize = 12;
        for (int y=0; y<grid.length; y++) {
            for (int x=0; x<grid[0].length; x++) {
                var c = grid[y][x];
                // фон по зоне
                Color bg = switch (c.getZone()) {
                    case FOREST -> Color.DARKGREEN;
                    case FIELD -> Color.LIGHTGREEN;
                    case WATER -> Color.LIGHTBLUE;
                    case MOUNTAIN -> Color.GREY;
                };
                g.setFill(bg);
                g.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);

                // растения
                if (!c.plants().isEmpty()) {
                    g.setFill(Color.GREENYELLOW);
                    g.fillRect(x*cellSize+3, y*cellSize+3, 3,3);
                }
                // животные
                int n = c.animals().size();
                if (n>0) {
                    g.setFill(Color.RED);
                    g.fillOval(x*cellSize+6, y*cellSize+6, 4,4);
                }

                g.setStroke(Color.BLACK);
                g.strokeRect(x*cellSize, y*cellSize, cellSize, cellSize);
            }
        }
        stats.setText("Животных: " + island.totalAnimals() + "  Растений: " + island.totalPlants());

        var counts = island.countBySpecies();
        StringBuilder sb = new StringBuilder();
        for (var e : counts.entrySet()) {
            if (e.getValue() > 0) {
                sb.append(e.getKey().name()).append(": ").append(e.getValue()).append("  ");
            }
        }
        detailedStats.setText(sb.toString());
    }

    @Override
    public void stop() {
        if (timeline!=null) timeline.stop();
        if (island!=null) island.shutdown();
    }

    public static void main(String[] args) { launch(args); }
}
