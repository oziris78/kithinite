
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


package com.twistral.kithinite.shapes;


import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.twistral.kithinite.core.*;
import space.earlygrey.shapedrawer.*;
import static com.twistral.kithinite.KithiniteUtils.*;


public class Ellipse extends Shape<Ellipse> {

    private float radiusX, radiusY;
    private float rotationDegrees;
    private float lineWidth;
    private Color innerColor, outerColor;


    public Ellipse(boolean filled, float radiusX, float radiusY, Color innerColor, Color outerColor,
                   float rotationDegrees, float lineWidth)
    {
        super(filled);
        setRadiusX(radiusX); // auto update width
        setRadiusY(radiusY); // auto update height
        setColor(innerColor, outerColor);
        this.rotationDegrees = rotationDegrees;
        this.lineWidth = lineWidth;
    }

    public Ellipse(boolean filled, float radiusX, float radiusY, Color color,
                   float rotationDegrees, float lineWidth)
    {
        this(filled, radiusX, radiusY, color, color, rotationDegrees, lineWidth);
    }

    public Ellipse(boolean filled, float radiusX, float radiusY, Color innerColor, Color outerColor) {
        this(filled, radiusX, radiusY, innerColor, outerColor, DEF_ROTATION_DEGREES, DEF_LINE_WIDTH);
    }

    public Ellipse(boolean filled, float radiusX, float radiusY, Color color) {
        this(filled, radiusX, radiusY, color, color, DEF_ROTATION_DEGREES, DEF_LINE_WIDTH);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    public void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;
        if (this.radiusX <= 0 || this.radiusY <= 0) return;

        final float absCentreX = absX + radiusX;
        final float absCentreY = absY + radiusY;
        final float rotationRadians = this.rotationDegrees * MathUtils.degreesToRadians;

        final Color outColor = prioritySelect(this.outerColor, DEF_COLOR);

        if (filled) {
            Color inColor = prioritySelect(this.innerColor, DEF_COLOR);

            drawer.filledEllipse(
                absCentreX, absCentreY, radiusX, radiusY, rotationRadians, inColor, outColor
            );
        }
        else if (lineWidth > 1f) {
            // Prevent spilling because of lineWidth variable
            final float halfLine = lineWidth / 2f;
            final float adjRadX = radiusX - halfLine;
            final float adjRadY = radiusY - halfLine;

            if (adjRadX > 0 && adjRadY > 0) {
                final float oldColor = drawer.setColor(outColor);
                drawer.ellipse(absCentreX, absCentreY, adjRadX, adjRadY, rotationRadians, lineWidth);
                drawer.setColor(oldColor);
            }
        }

        // Redraw the perimeter line eliminate pixel imperfections (using outer color)
        final float oldColor = drawer.setColor(outColor);
        drawer.ellipse(absCentreX, absCentreY, radiusX, radiusY, rotationRadians, 1f);
        drawer.setColor(oldColor);
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
    public Ellipse setWidth(float width) {
        this.width = width;
        this.radiusX = width / 2f;
        return this;
    }

    @Override
    public Ellipse setHeight(float height) {
        this.height = height;
        this.radiusY = height / 2f;
        return this;
    }

    /*////////////////  SETTERS WITH NO SIDE EFFECTS  ////////////////*/

    public Ellipse setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
        return this;
    }

    public Ellipse setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
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

    @Override
    public Ellipse setColor(Color color) {
        this.innerColor = color;
        this.outerColor = color;
        return this;
    }

    public Ellipse setColor(Color innerColor, Color outerColor) {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        return this;
    }

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

    @Override
    public Color getColor() {
        return prioritySelect(this.innerColor, this.outerColor, null);
    }

    public float getRadiusX() { return this.radiusX; }
    public float getRadiusY() { return this.radiusY; }
    public float getRotationDegrees() { return this.rotationDegrees; }
    public float getLineWidth() { return this.lineWidth; }
    public Color getInnerColor() { return this.innerColor; }
    public Color getOuterColor() { return this.outerColor; }


}
