package com.mocircle.cidrawing.element;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * A ElementGroup is a element container basic class which contains sub elements.
 */
public abstract class ElementGroup extends DrawElement {

    protected transient RectF initBoundingBox;
    protected transient Path boundingPath;
    protected List<DrawElement> elements;

    public ElementGroup() {
    }

    public ElementGroup(List<DrawElement> elements) {
        setElements(elements);
    }

    public List<DrawElement> getElements() {
        return elements;
    }

    public void setElements(List<DrawElement> elements) {
        this.elements = elements;
        recalculateBoundingBox();
    }

    @Override
    public void applyMatrixForData(Matrix matrix) {
        super.applyMatrixForData(matrix);

        boundingPath = new Path();
        boundingPath.addRect(initBoundingBox, Path.Direction.CW);
        boundingPath.transform(dataMatrix);
    }

    @Override
    public Matrix applyDisplayMatrixToData() {
        Matrix matrix = new Matrix(getDisplayMatrix());
        applyMatrixForData(getDisplayMatrix());
        getDisplayMatrix().reset();
        recalculateBoundingBox();
        return matrix;
    }

    @Override
    public void restoreDisplayMatrixFromData(Matrix matrix) {
        if (matrix != null) {
            Matrix invertDisplayMatrix = new Matrix();
            matrix.invert(invertDisplayMatrix);
            applyMatrixForData(invertDisplayMatrix);
            getDisplayMatrix().set(new Matrix(matrix));
            recalculateBoundingBox();
        }
    }

    @Override
    public void updateBoundingBox() {
        if (boundingPath != null) {
            RectF box = new RectF();
            boundingPath.computeBounds(box, true);
            setBoundingBox(box);
        }
    }

    @Override
    protected void cloneTo(BaseElement element) {
        super.cloneTo(element);
        if (element instanceof ElementGroup) {
            ElementGroup obj = (ElementGroup) element;
            obj.initBoundingBox = new RectF(initBoundingBox);
            obj.boundingPath = new Path(boundingPath);
            if (elements != null) {
                obj.elements = new ArrayList<>();
                for (DrawElement e : elements) {
                    obj.elements.add((DrawElement) e.clone());
                }
            }
        }
    }

    protected void recalculateBoundingBox() {
        RectF box = new RectF();
        for (DrawElement element : elements) {
            box.union(element.getOuterBoundingBox());
        }

        initBoundingBox = new RectF(box);
        boundingPath = new Path();
        boundingPath.addRect(box, Path.Direction.CW);
        updateBoundingBox();
    }

}
