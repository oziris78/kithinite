
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
import com.twistral.kithinite.core.*;
import space.earlygrey.shapedrawer.*;
import static com.twistral.kithinite.KithiniteUtils.*;


public class Circle extends Shape<Circle> {

    private float radius;
    private float lineWidth;
    private Color innerColor, outerColor;


    public Circle(boolean filled, float radius, Color innerColor, Color outerColor, float lineWidth) {
        super(filled);
        setRadius(radius); // auto update width & height
        setColor(innerColor, outerColor);
        this.lineWidth = lineWidth;
    }

    public Circle(boolean filled, float radius, Color color, float lineWidth) {
        this(filled, radius, color, color, lineWidth);
    }

    public Circle(boolean filled, float radius, Color innerColor, Color outerColor) {
        this(filled, radius, innerColor, outerColor, DEF_LINE_WIDTH);
    }

    public Circle(boolean filled, float radius, Color color) {
        this(filled, radius, color, color, DEF_LINE_WIDTH);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    public void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;
        if (this.radius <= 0) return;

        final float absCentreX = absX + radius;
        final float absCentreY = absY + radius;

        final Color outColor = prioritySelect(this.outerColor, DEF_COLOR);

        if (filled) {
            Color inColor = prioritySelect(this.innerColor, DEF_COLOR);

            drawer.filledEllipse(
                absCentreX, absCentreY, radius, radius, 0f, inColor, outColor
            );
        }
        else if (lineWidth > 1f) {
            // Prevent spilling because of lineWidth variable
            final float halfLine = lineWidth / 2f;
            final float adjRad = radius - halfLine;

            if (adjRad > 0) {
                final float oldColor = drawer.setColor(outColor);
                drawer.ellipse(absCentreX, absCentreY, adjRad, adjRad, 0f, lineWidth);
                drawer.setColor(oldColor);
            }
        }

        // Redraw the perimeter line eliminate pixel imperfections (using outer color)
        final float oldColor = drawer.setColor(outColor);
        drawer.ellipse(absCentreX, absCentreY, radius, radius, 0f, 1f);
        drawer.setColor(oldColor);
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    /*////////////////  SETTERS WITH SIDE EFFECTS  ////////////////*/

    public Circle setRadius(float radius) {
        this.radius = radius;
        this.width = radius * 2f; // effects width
        this.height = radius * 2f; // effects height
        return this;
    }

    @Override
    public Circle setWidth(float width) {
        this.width = width;
        this.height = width;
        this.radius = width / 2f;
        return this;
    }

    @Override
    public Circle setHeight(float height) {
        this.height = height;
        this.width = height;
        this.radius = height / 2f;
        return this;
    }

    @Override
    public Circle setSize(float width, float height) {
        // Enforce equal width and height for circles
        final float size = Math.min(width, height);
        super.setSize(size, size);
        this.radius = size / 2f;
        return this;
    }


    /*////////////////  Setters with NO SIDE EFFECTS  ////////////////*/

    public Circle setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Circle setInnerColor(Color innerColor) {
        this.innerColor = innerColor;
        return this;
    }

    public Circle setOuterColor(Color outerColor) {
        this.outerColor = outerColor;
        return this;
    }

    /*////////////////  UTILITY SETTERS  ////////////////*/

    @Override
    public Circle setColor(Color color) {
        this.innerColor = color;
        this.outerColor = color;
        return this;
    }

    public Circle setColor(Color innerColor, Color outerColor) {
        this.innerColor = innerColor;
        this.outerColor = outerColor;
        return this;
    }

    public Circle setCentreX(float centreX) {
        this.x = centreX - this.radius;
        return this;
    }

    public Circle setCentreY(float centreY) {
        this.y = centreY - this.radius;
        return this;
    }

    public Circle setCentre(float centreX, float centreY) {
        return setCentreX(centreX).setCentreY(centreY);
    }

    /*////////////////  ALL GETTERS  ////////////////*/

    @Override
    public Color getColor() {
        return prioritySelect(this.innerColor, this.outerColor, null);
    }

    public float getRadius() { return this.radius; }
    public float getLineWidth() { return this.lineWidth; }
    public Color getInnerColor() { return this.innerColor; }
    public Color getOuterColor() { return this.outerColor; }


}
