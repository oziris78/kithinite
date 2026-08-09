
// Copyright 2026 Oğuzhan Topaloğlu
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.twistral.kithinite.widgets.shapes;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.twistral.kithinite.core.Piece;
import com.twistral.kithinite.widgets.Widget;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.twistral.kithinite.Kithinite.prioritySelect;


public class Ellipse extends Widget {

    // Static variables
    public static final float DEF_ROTATION_DEGREES = 0f;
    public static final Color DEF_COLOR = Color.WHITE;
    public static final float DEF_LINE_WIDTH = 1f;

    // Ellipse related variables
    private boolean filled;
    private float radiusX, radiusY;
    private float rotationDegrees;
    private float lineWidth;

    // Color variables
    private Color color;
    private Color innerColor;
    private Color outerColor;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    private Ellipse(boolean filled, float radiusX, float radiusY, float rotationDegrees,
                    float lineWidth, Color color, Color innerColor, Color outerColor)
    {
        this.filled = filled;
        setRadiusX(radiusX); // auto update width
        setRadiusY(radiusY); // auto update height
        this.rotationDegrees = rotationDegrees;
        this.lineWidth = lineWidth;
        this.color = color;
        this.innerColor = innerColor;
        this.outerColor = outerColor;
    }

    // Main constructor for ellipses with a SINGLE COLOR
    public Ellipse(boolean filled, float radiusX, float radiusY, Color color,
                   float rotationDegrees, float lineWidth)
    {
        this(filled, radiusX, radiusY, rotationDegrees, lineWidth, color, null, null);
    }

    // Main constructor for ellipses with TWO COLORS
    public Ellipse(boolean filled, float radiusX, float radiusY, Color innerColor, Color outerColor,
                   float rotationDegrees, float lineWidth)
    {
        this(filled, radiusX, radiusY, rotationDegrees, lineWidth, null, innerColor, outerColor);
    }


    // Secondary constructor for ellipses with a SINGLE COLOR
    public Ellipse(boolean filled, float radiusX, float radiusY, Color color) {
        this(filled, radiusX, radiusY, color, DEF_ROTATION_DEGREES, DEF_LINE_WIDTH);
    }

    // Secondary constructor for ellipses with TWO COLORS
    public Ellipse(boolean filled, float radiusX, float radiusY, Color innerColor, Color outerColor) {
        this(filled, radiusX, radiusY, innerColor, outerColor, DEF_ROTATION_DEGREES, DEF_LINE_WIDTH);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;
        if (this.radiusX <= 0 || this.radiusY <= 0) return;

        final float absCentreX = absX + radiusX;
        final float absCentreY = absY + radiusY;
        final float rotationRadians = this.rotationDegrees * MathUtils.degreesToRadians;

        if (filled) {
            Color inColor = prioritySelect(this.innerColor, this.color, DEF_COLOR);
            Color outColor = prioritySelect(this.outerColor, this.color, DEF_COLOR);

            drawer.filledEllipse(
                absCentreX, absCentreY, radiusX, radiusY, rotationRadians, inColor, outColor
            );
        }
        else {
            // prevent spilling because of lineWidth variable
            final float halfLine = lineWidth / 2f;
            final float adjRadiusX = radiusX - halfLine;
            final float adjRadiusY = radiusY - halfLine;

            Color outlineColor = prioritySelect(this.color, DEF_COLOR);
            final float oldColor = drawer.setColor(outlineColor);
            drawer.ellipse(absCentreX, absCentreY, adjRadiusX, adjRadiusY, rotationRadians, lineWidth);
            drawer.setColor(oldColor);
        }
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    /*////////////////  SETTERS WITH SIDE EFFECTS  ////////////////*/

    public Ellipse setRadiusX(float radiusX) {
        this.radiusX = radiusX;
        this.width = radiusX * 2f; // effects width
        return this;
    }

    public Ellipse setRadiusY(float radiusY) {
        this.radiusY = radiusY;
        this.height = radiusY * 2f; // effects height
        return this;
    }

    @Override
    public Piece setWidth(float width) {
        this.width = width;
        this.radiusX = width / 2f;
        return this;
    }

    @Override
    public Piece setHeight(float height) {
        this.height = height;
        this.radiusY = height / 2f;
        return this;
    }

    /*////////////////  SETTERS WITH NO SIDE EFFECTS  ////////////////*/

    public Ellipse setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public Ellipse setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Ellipse setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
        return this;
    }

    public Ellipse setColor(Color color) {
        this.color = color;
        return this;
    }

    public Ellipse setColor(Color innerColor, Color outerColor) {
        return setInnerColor(innerColor).setOuterColor(outerColor);
    }

    public Ellipse setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
        return this;
    }

    public Ellipse setOuterColor(Color outerColor) {
        this.outerColor = outerColor;
        return this;
    }

    /*////////////////  UTILITY SETTERS  ////////////////*/

    public Ellipse setCentreX(float centreX) {
        this.x = centreX - this.radiusX;
        return this;
    }

    public Ellipse setCentreY(float centreY) {
        this.y = centreY - this.radiusY;
        return this;
    }

    public Ellipse setCentre(float centreX, float centreY) {
        return setCentreX(centreX).setCentreY(centreY);
    }

    /*////////////////  ALL GETTERS  ////////////////*/

    public boolean isFilled() { return this.filled; }
    public float getRadiusX() { return this.radiusX; }
    public float getRadiusY() { return this.radiusY; }
    public float getRotationDegrees() { return this.rotationDegrees; }
    public float getLineWidth() { return this.lineWidth; }
    public Color getColor() { return this.color; }
    public Color getInnerColor() { return this.innerColor; }
    public Color getOuterColor() { return this.outerColor; }


}
