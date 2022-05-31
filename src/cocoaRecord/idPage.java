package cocoaRecord;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;


import javafx.scene.input.Clipboard;
import javafx.util.Duration;

public class idPage {
    @FXML
    private Text txtCopied;

    @FXML
    private Label lb;

    @FXML
    private Button btCopied;




    @FXML
    void copyID(ActionEvent event) {
        copyToClip(lb.getText());

        txtCopied.setVisible(true);


        Timeline blinker = createBlinker(txtCopied);

        FadeTransition fader = createFader(txtCopied);

        SequentialTransition blinkThenFade = new SequentialTransition(
                txtCopied,
                blinker,
                fader
        );

        blinkThenFade.play();
    }


    private Timeline createBlinker(Node node) {
        Timeline blink = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(
                                node.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(0.5),
                        new KeyValue(
                                node.opacityProperty(),
                                0,
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(
                                node.opacityProperty(),
                                1,
                                Interpolator.DISCRETE
                        )
                )
        );
        blink.setCycleCount(3);

        return blink;
    }

    private FadeTransition createFader(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(2), node);
        fade.setFromValue(1);
        fade.setToValue(0);

        return fade;
    }

    @FXML
    private void initialize(){
        lb.setText(String.valueOf(ShareData.farmerID));


    }



    public  void copyToClip(String data) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(data);
        clipboard.setContent(content);
    }

}

