package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *making application initializable
 */

public class Controller implements Initializable {

    MediaPlayer player;

    @FXML
    private MediaView mediaView;


    @FXML
    private Button playBtn;
    @FXML
    private Button preBtn;
    @FXML
    private Button nextBtn;

    @FXML
    private Slider timeSlider;

    /**
     *
     * @param event that makes it so we can choose a video from our files
     *              also makes time slider
     * @try find img pictures for stop buttons etc and make path
     * @catch exception if image file cant be found
     */
    @FXML
    void openSongMenu(ActionEvent event) {
        try {
            System.out.println("open song clicked");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);

            Media m = new Media(file.toURI().toURL().toString());

            if (player != null) {
                player.dispose();
            }


            player = new MediaPlayer(m);

            mediaView.setMediaPlayer(player);


            player.setOnReady(() -> {

                timeSlider.setMin(0);
                timeSlider.setMax(player.getMedia().getDuration().toMinutes());

                timeSlider.setValue(0);
                try {
                    playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            });



            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                    Duration d = player.getCurrentTime();

                    timeSlider.setValue(d.toMinutes());
                }
            });

/**
 * makes time slider skip a certain amount
 * @catch exception for everything
 */

            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (timeSlider.isPressed()) {
                        double val = timeSlider.getValue();
                        player.seek(new Duration(val * 60 * 1000));
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param event action event so that the video can be played and paused
     *              and paths to img
     *              exception if img cant be found
     */
    @FXML
    void play(ActionEvent event) {

        try {

            MediaPlayer.Status status = player.getStatus();

            if (status == MediaPlayer.Status.PLAYING) {

                player.pause();

                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
            } else {
                player.play();

                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/pause.png"))));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param location
     * @param resources
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
            preBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/previous.png"))));
            nextBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/next.png"))));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param event for skip button to skip forward in video
     */
    @FXML
    void preBtnClick(ActionEvent event) {
        double d = player.getCurrentTime().toSeconds();

        d = d - 10;

        player.seek(new Duration(d * 1000));

    }

    /**
     *
     * @param event for back button to go back in video
     */
    @FXML
    void nextBtnClick(ActionEvent event) {
        double d = player.getCurrentTime().toSeconds();

        d = d + 10;

        player.seek(new Duration(d * 1000));

    }
}
