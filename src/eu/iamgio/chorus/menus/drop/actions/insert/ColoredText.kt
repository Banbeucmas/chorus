package eu.iamgio.chorus.menus.drop.actions.insert

import eu.iamgio.chorus.Chorus
import eu.iamgio.chorus.editor.EditorArea
import eu.iamgio.chorus.menus.coloredtexteditor.ColoredTextEditor
import eu.iamgio.chorus.menus.drop.actions.DropMenuAction

/**
 * @author Gio
 */
class ColoredText : DropMenuAction() {

    override fun onAction(area: EditorArea, x: Double, y: Double) {
        val editor = ColoredTextEditor()
        val root = Chorus.getInstance().root
        editor.prefWidthProperty().bind(root.widthProperty().divide(1.8))
        editor.prefHeightProperty().bind(root.heightProperty().divide(3))
        editor.translateXProperty().bind(root.widthProperty().divide(2).subtract(editor.prefWidthProperty().divide(2)))
        editor.translateYProperty().bind(root.heightProperty().divide(2).subtract(editor.prefHeightProperty().divide(2)))
        editor.show()
    }
}