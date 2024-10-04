package it.d4n73.asutils

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.SelectionModel
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.util.Base64

class Base64DecodeAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        // Ottieni il progetto corrente
        val project: Project? = e.project
        // Ottieni l'editor corrente
        val editor: Editor? = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.EDITOR)

        if (editor != null) {
            // Ottieni il modello di selezione
            val selectionModel: SelectionModel = editor.selectionModel

            // Ottieni il testo selezionato
            val selectedText = selectionModel.selectedText

            if (selectedText != null) {
                // Controlla se il testo è codificato in Base64
                if (isBase64Encoded(selectedText)) {
                    try {
                        // Decodifica il testo da Base64 a testo normale
                        val decodedBytes = Base64.getDecoder().decode(selectedText)
                        val decodedText = String(decodedBytes)

                        // Sostituisci il testo selezionato con quello decodificato
                        WriteCommandAction.runWriteCommandAction(project) {
                            val document = editor.document
                            document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, decodedText)
                        }

                        // Mostra un messaggio di conferma
                        //Messages.showMessageDialog(project, "Testo decodificato da Base64", "Decodifica completata", Messages.getInformationIcon())
                    } catch (e: IllegalArgumentException) {
                        // Mostra un errore se la decodifica fallisce
                        Messages.showMessageDialog(project, "Errore nella decodifica del testo. Il testo selezionato potrebbe non essere correttamente codificato in Base64.", "Errore", Messages.getErrorIcon())
                    }
                } else {
                    // Mostra un messaggio di errore se il testo non è in Base64
                    Messages.showMessageDialog(project, "Il testo selezionato non è codificato in Base64", "Errore", Messages.getErrorIcon())
                }
            } else {
                // Se non è selezionato alcun testo, mostra un messaggio di errore
                Messages.showMessageDialog(project, "Seleziona del testo da decodificare!", "Errore", Messages.getErrorIcon())
            }
        }
    }

    // Funzione per controllare se il testo è codificato in Base64 (la stessa utilizzata nella codifica)
    private fun isBase64Encoded(text: String): Boolean {
        return try {
            // Prova a decodificare il testo
            val decodedBytes = Base64.getDecoder().decode(text)
            // Se il testo può essere decodificato correttamente e ricodificato ugualmente, è Base64
            val encodedText = Base64.getEncoder().encodeToString(decodedBytes)
            text == encodedText
        } catch (e: IllegalArgumentException) {
            // Se c'è un'eccezione, il testo non è Base64
            false
        }
    }
}
