package it.d4n73.asutils

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class GenerateLoremIpsumAction : AnAction() {

    private val loremIpsumText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)

        if (editor != null) {
            val userInput = Messages.showInputDialog(project, "Inserisci il numero di caratteri per il testo Lorem Ipsum:", "Genera Lorem Ipsum", Messages.getQuestionIcon())
            val numChars = userInput?.toIntOrNull()

            if (numChars != null && numChars > 0) {
                val generatedText = generateLoremIpsum(numChars)
                WriteCommandAction.runWriteCommandAction(project) {
                    val document = editor.document
                    val selectionModel: SelectionModel = editor.selectionModel
                    document.insertString(selectionModel.selectionStart, generatedText)
                }
                //Messages.showMessageDialog(project, "Testo Lorem Ipsum generato con successo!", "Completato", Messages.getInformationIcon())
            } else {
                Messages.showMessageDialog(project, "Numero di caratteri non valido. Per favore, inserisci un numero positivo.", "Errore", Messages.getErrorIcon())
            }
        }
    }

    private fun generateLoremIpsum(charCount: Int): String {
        val builder = StringBuilder()

        while (builder.length < charCount) {
            builder.append(loremIpsumText)
        }

        // Taglia il testo generato per ottenere esattamente charCount caratteri
        return builder.substring(0, charCount)
    }
}
