
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


package com.twistral.kithinite;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class Circle extends Widget {

    // Static variables
    public static final Color DEF_COLOR = Color.WHITE;
    public static final float DEF_LINE_WIDTH = 1f;

    // Circle related variables
    private boolean filled;
    private float radius;
    private float lineWidth;

    // Color variables
    private Color color;
    private Color innerColor;
    private Color outerColor;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    private Circle(boolean filled, float radius, float lineWidth,
                   Color color, Color innerColor, Color outerColor)
    {
        this.filled = filled;
        this.radius = radius;
        this.lineWidth = lineWidth;
        this.color = color;
        this.innerColor = innerColor;
        this.outerColor = outerColor;
    }

    // Main constructor for circles with a SINGLE COLOR
    public Circle(boolean filled, float radius, Color color, float lineWidth) {
        this(filled, radius, lineWidth, color, null, null);
    }

    // Main constructor for circles with TWO COLORS
    public Circle(boolean filled, float radius, Color innerColor, Color outerColor, float lineWidth) {
        this(filled, radius, lineWidth, null, innerColor, outerColor);
    }

    // Secondary constructor for circles with a SINGLE COLOR
    public Circle(boolean filled, float radius, Color color) {
        this(filled, radius, color, DEF_LINE_WIDTH);
    }

    // Secondary constructor for circles with TWO COLORS
    public Circle(boolean filled, float radius, Color innerColor, Color outerColor) {
        this(filled, radius, innerColor, outerColor, DEF_LINE_WIDTH);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;
        if (this.radius <= 0) return;

        final float absCentreX = absX + radius;
        final float absCentreY = absY + radius;

        if (filled) {
            Color inColor = this.innerColor;
            if(inColor == null) inColor = this.color;
            if(inColor == null) inColor = DEF_COLOR;

            Color outColor = this.outerColor;
            if(outColor == null) outColor = this.color;
            if(outColor == null) outColor = DEF_COLOR;

            drawer.filledEllipse(absCentreX, absCentreY, radius, radius, 0f, inColor, outColor);
        }
        else {
            Color outlineColor = this.color;
            if(outlineColor == null) outlineColor = DEF_COLOR;

            final float oldColor = drawer.setColor(outlineColor);
            drawer.ellipse(absCentreX, absCentreY, radius, radius, 0f, lineWidth);
            drawer.setColor(oldColor);
        }
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
    public Piece setWidth(float width) {
        this.width = width;
        this.height = width;
        this.radius = width / 2f;
        return this;
    }

    @Override
    public Piece setHeight(float height) {
        this.height = height;
        this.width = height;
        this.radius = height / 2f;
        return this;
    }

    /*////////////////  Setters with NO SIDE EFFECTS  ////////////////*/

    public Circle setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public Circle setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Circle setColor(Color color) {
        this.color = color;
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

    public boolean isFilled() { return this.filled; }
    public float getRadius() { return this.radius; }
    public float getLineWidth() { return this.lineWidth; }
    public Color getColor() { return this.color; }
    public Color getInnerColor() { return this.innerColor; }
    public Color getOuterColor() { return this.outerColor; }


}
