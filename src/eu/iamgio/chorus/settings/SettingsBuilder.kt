package eu.iamgio.chorus.settings

import eu.iamgio.chorus.settings.nodes.SettingButton
import eu.iamgio.chorus.settings.nodes.SettingCheckBox
import eu.iamgio.chorus.settings.nodes.SettingComboBox
import eu.iamgio.chorus.settings.nodes.SettingTextField
import eu.iamgio.chorus.theme.Themes
import eu.iamgio.chorus.util.config
import eu.iamgio.chorus.util.stringToList
import eu.iamgio.chorus.util.toObservableList
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox

/**
 * @author Gio
 */
class SettingsBuilder private constructor() {

    companion object {
        private val values = config.keys.sortedByDescending {it.toString()}
        private val actions = HashMap<String, List<Runnable>>()
        private val nodes = HashMap<String, Node>()

        @JvmStatic fun buildLeft(): List<SettingButton> {
            val stringList = ArrayList<String>()
            val list = ArrayList<SettingButton>()
            values.reversed().filter {!it.toString().startsWith(".")}.forEach {
                val s = it.toString().replace("_", " ").split(".")[1]
                if(!stringList.contains(s)) {
                    stringList += s
                }
            }
            stringList.forEach {list += SettingButton(it)}
            return list
        }

        @JvmStatic fun buildRight(s: String): List<HBox> {
            val list = ArrayList<HBox>()
            values.reversed().filter {it.toString().split(".")[1].contains(s) && !it.toString().contains("%style") && !it.toString().startsWith(".")}
                    .forEach {
                        val label = Label(it.toString().split(".")[3].replace("_", " "))
                        label.styleClass += "setting-label"

                        val inputSettingString = config.getInternalString("$it%style")
                        val settingInput = SettingInput.valueOf(inputSettingString.split(" ")[0].split("{")[0])
                        val input = settingInput.clazz.newInstance()
                        input.id = it.toString()
                        input.styleClass += settingInput.styleClass
                        nodes += it.toString() to input
                        when(input) {
                            is TextField -> {
                                if(inputSettingString.contains(" ")) {
                                    (input as SettingTextField).regex = Regex(inputSettingString.replace("TEXTFIELD ", ""))
                                }
                                input.text = config.getString(it.toString())
                                input.textProperty().addListener {_ -> actions[it]?.forEach {it.run()}}
                            }
                            is SettingComboBox -> {
                                if(it == "1.Appearance.1.Theme") {
                                    input.items = Themes.getThemes().map {it.name.toLowerCase().capitalize()}.toObservableList()
                                    input.value = Themes.byConfig().name.toLowerCase().capitalize()

                                } else {
                                    input.items = stringToList(inputSettingString).toObservableList()
                                    input.value = config.getString(it.toString()).toLowerCase().capitalize()
                                }
                                input.selectionModel.selectedItemProperty().addListener {_ -> actions[it]?.forEach {it.run()}}
                            }
                            is SettingCheckBox -> {
                                input.isSelected = config.getBoolean(it.toString())
                                input.selectedProperty().addListener {_ -> actions[it]?.forEach {it.run()}}
                            }
                        }
                        val hbox = HBox(25.0, label, input)
                        hbox.alignment = Pos.CENTER_LEFT
                        list += hbox
                    }
            return list
        }

        @JvmStatic fun addAction(setting: String, runnable: Runnable) {
            if(!actions.containsKey(setting)) {
                actions += setting to listOf(runnable)
            } else {
                var actions = actions[setting]!!
                actions += runnable
                this.actions += setting to actions
            }
        }

        @JvmStatic fun getNode(setting: String) = nodes[setting]
    }
}