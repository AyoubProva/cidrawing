package com.mocircle.cidrawing.command;

import android.graphics.Matrix;

import com.mocircle.cidrawing.board.ElementManager;
import com.mocircle.cidrawing.element.DrawElement;

public class ReshapeCommand extends AbstractCommand {

    private ElementManager elementManager;
    private DrawElement reshapeElement;
    private Matrix displayMatrix;

    @Override
    public void setDrawingBoardId(String boardId) {
        super.setDrawingBoardId(boardId);
        elementManager = drawingBoard.getElementManager();
    }

    @Override
    public boolean isExecutable() {
        if (reshapeElement == null) {
            reshapeElement = elementManager.getSelection().getSingleElement();
        }
        return reshapeElement != null;
    }

    @Override
    public boolean doCommand() {
        displayMatrix = reshapeElement.applyDisplayMatrixToData();
        reshapeElement.resetReferencePoint();
        drawingBoard.getDrawingView().notifyViewUpdated();
        return true;
    }

    @Override
    public void undoCommand() {
        if (reshapeElement != null) {
            reshapeElement.restoreDisplayMatrixFromData(displayMatrix);
            drawingBoard.getDrawingView().notifyViewUpdated();
        }
    }

}
