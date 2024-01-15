package invaders.engine;

import java.util.List;
import java.util.ArrayList;


import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;

import java.util.Map;

import javafx.scene.media.Media;



public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private Pane pane;
    private GameEngine model;
    private List<EntityView> entityViews;
    private Renderable background;

    private double xViewportOffset = 0.0;
    private double yViewportOffset = 0.0;
    private static final double VIEWPORT_MARGIN = 280.0;

    public Timeline timeline;
    private GameEngine gameEngine;

    public GameWindow(GameEngine model, int width, int height){
		this.width = width;
        this.height = height;
        this.model = model;
        pane = new Pane();
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        Map<String, MediaPlayer> sounds = initializeSounds();
        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model, sounds);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

        entityViews = new ArrayList<EntityView>();

    }
    private Map<String, MediaPlayer> initializeSounds() {
        Map<String, MediaPlayer> sounds = new HashMap<>();
        URL mediaUrl = getClass().getResource("/shoot.wav");
        String jumpURL = mediaUrl.toExternalForm();
        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("shoot", mediaPlayer);
        return sounds;
    }

	public void run() {
        this.timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));

        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public void stopGameLoop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void draw(){
        model.update();

        if (!model.isGameRunning()) {
            stopGameLoop();
            return;
        }


        List<Renderable> renderables = model.getRenderables();
        // 将外部边界检查与渲染对象分离
        for(Renderable ro: renderables){
            if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
                continue;
            }
            if(ro.getPosition().getX() + ro.getWidth() >= 640) {
                ro.getPosition().setX(639-ro.getWidth());
            }

            if(ro.getPosition().getX() <= 0) {
                ro.getPosition().setX(1);
            }

            if(ro.getPosition().getY() + ro.getHeight() >= 400) {
                ro.getPosition().setY(399-ro.getHeight());
            }

            if(ro.getPosition().getY() <= 0) {
                ro.getPosition().setY(1);
            }
        }

        for (int i = entityViews.size() - 1; i >= 0; i--) {
            boolean find=false;
            for (Renderable renderable : renderables) {
                if(entityViews.get(i).matchesEntity(renderable)){
                    find=true;
                }
            }
            if(!find){
                pane.getChildren().remove(entityViews.get(i).getNode());
                entityViews.remove(i);
            }
        }

        for (Renderable entity : renderables) {
            boolean find=false;
            for (EntityView entityView : entityViews) {
                if(entityView.matchesEntity(entity)){
                    find=true;
                    entityView.update(xViewportOffset, yViewportOffset);
                }
            }
            if(!find){
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

    }

	public Scene getScene() {
        return scene;
    }
}
