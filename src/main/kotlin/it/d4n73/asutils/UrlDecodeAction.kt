package it.d4n73.asutils

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class UrlDecodeAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project: Project? = e.project
        val editor: Editor? = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)

        if (editor != null) {
            val selectionModel: SelectionModel = editor.selectionModel
            val selectedText = selectionModel.selectedText

            if (selectedText != null) {
                try {
                    val decodedText = URLDecoder.decode(selectedText, StandardCharsets.UTF_8.toString())
                    WriteCommandAction.runWriteCommandAction(project) {
                        val document = editor.document
                        document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, decodedText)
                    }
                    //Messages.showMessageDialog(project, "Testo decodificato da URL", "Decodifica completata", Messages.getInformationIcon())
                } catch (exception: Exception) {
                    Messages.showMessageDialog(project, "Errore nella decodifica dell'URL. Il testo selezionato potrebbe non essere correttamente codificato come URL.", "Errore", Messages.getErrorIcon())
                }
            } else {
                Messages.showMessageDialog(project, "Seleziona del testo da decodificare!", "Errore", Messages.getErrorIcon())
            }
        }
    }
}
