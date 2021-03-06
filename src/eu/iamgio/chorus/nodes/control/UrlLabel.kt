package eu.iamgio.chorus.nodes.control

import eu.iamgio.chorus.Chorus
import javafx.scene.Cursor
import javafx.scene.text.Text

/**
 * @author Gio
 */
class UrlLabel(text: String, var url: String) : Text(text) {

    init {
        styleClass += "url"
        style = "-fx-font-size: 13"
        setOnMouseEntered {
            cursor = Cursor.HAND
        }
        setOnMouseExited {
            cursor = Cursor.DEFAULT
        }
        setOnMouseClicked {
            Chorus.getInstance().hostServices.showDocument(url)
        }
    }
}