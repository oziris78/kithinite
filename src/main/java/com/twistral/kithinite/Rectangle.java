
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
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Objects;


public class Rectangle extends Widget {

    // Static variables
    public static final JoinType DEF_JOIN_TYPE = JoinType.POINTY;
    public static final float DEF_ROTATION_DEGREES = 0f;
    public static final Color DEF_COLOR = Color.WHITE;
    public static final float DEF_LINE_WIDTH = 1f;

    // Rect related variables
    private boolean filled;
    private float rotationDegrees;
    private float lineWidth;
    private JoinType joinType;

    // Color variables
    private Color color;
    private Color topRightColor;
    private Color topLeftColor;
    private Color bottomLeftColor;
    private Color bottomRightColor;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    private Rectangle(boolean filled, float rotationDegrees, float lineWidth, JoinType joinType,
                      Color color, Color topRightColor, Color topLeftColor,
                      Color bottomLeftColor, Color bottomRightColor)
    {
        this.filled = filled;
        this.rotationDegrees = rotationDegrees;
        this.lineWidth = lineWidth;
        this.joinType = joinType;
        this.color = color;
        this.topRightColor = topRightColor;
        this.topLeftColor = topLeftColor;
        this.bottomLeftColor = bottomLeftColor;
        this.bottomRightColor = bottomRightColor;
    }

    // Main constructor for rectangles with GRADIENTs
    public Rectangle(boolean filled, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft,
                     float rotationDegrees, float lineWidth, JoinType joinType)
    {
        this(filled, rotationDegrees, lineWidth, joinType, null, topRight, topLeft, bottomLeft, bottomRight);
    }

    // Main constructor for rectangles with COLORs
    public Rectangle(boolean filled, Color color, float rotationDegrees, float lineWidth, JoinType joinType) {
        this(filled, rotationDegrees, lineWidth, joinType, color, null, null, null, null);
    }

    // Secondary constructor for rectangles with GRADIENTs
    public Rectangle(boolean filled, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
        this(filled, topLeft, topRight, bottomRight, bottomLeft,
                DEF_ROTATION_DEGREES, DEF_LINE_WIDTH, DEF_JOIN_TYPE);
    }

    // Secondary constructor for rectangles with COLORs
    public Rectangle(boolean filled, Color color) {
        this(filled, color, DEF_ROTATION_DEGREES, DEF_LINE_WIDTH, DEF_JOIN_TYPE);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;

        final float rotationRadians = this.rotationDegrees * MathUtils.degreesToRadians;

        if (filled) {
            // Priority based handling of the colors
            Color c1 = this.topRightColor;
            if(c1 == null) c1 = this.color;
            if(c1 == null) c1 = DEF_COLOR;

            Color c2 = this.topLeftColor;
            if(c2 == null) c2 = this.color;
            if(c2 == null) c2 = DEF_COLOR;

            Color c3 = this.bottomLeftColor;
            if(c3 == null) c3 = this.color;
            if(c3 == null) c3 = DEF_COLOR;

            Color c4 = this.bottomRightColor;
            if(c4 == null) c4 = this.color;
            if(c4 == null) c4 = DEF_COLOR;

            drawer.filledRectangle(absX, absY, width, height, rotationRadians, c1, c2, c3, c4);
        }
        else {
            // prevent spilling because of lineWidth variable
            final float halfLine = lineWidth / 2f;
            final float adjX = absX + halfLine;
            final float adjY = absY + halfLine;
            final float adjW = width - lineWidth;
            final float adjH = height - lineWidth;

            // Finally render the rectangle
            Color outlineColor = this.color;
            if(outlineColor == null) outlineColor = DEF_COLOR;

            final float oldColor = drawer.setColor(outlineColor);
            drawer.rectangle(adjX, adjY, adjW, adjH, lineWidth, rotationRadians, joinType);
            drawer.setColor(oldColor);
        }
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/


    public Rectangle setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
        return this;
    }

    public Rectangle setJoinType(JoinType joinType) {
        this.joinType = joinType;
        return this;
    }

    public Rectangle setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Rectangle setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public Rectangle setColor(Color color) {
        this.color = color;
        return this;
    }

    public Rectangle setColor(int rgba) {
        if (this.color == null) this.color = new Color();
        this.color.set(rgba);
        return this;
    }

    public Rectangle setGradient(Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
        this.topLeftColor = topLeft;
        this.topRightColor = topRight;
        this.bottomRightColor = bottomRight;
        this.bottomLeftColor = bottomLeft;
        return this;
    }

    public Rectangle setGradient(int topLeftRgba, int topRightRgba, int bottomRightRgba, int bottomLeftRgba) {
        if (this.topLeftColor == null) this.topLeftColor = new Color();
        if (this.topRightColor == null) this.topRightColor = new Color();
        if (this.bottomRightColor == null) this.bottomRightColor = new Color();
        if (this.bottomLeftColor == null) this.bottomLeftColor = new Color();

        this.topLeftColor.set(topLeftRgba);
        this.topRightColor.set(topRightRgba);
        this.bottomRightColor.set(bottomRightRgba);
        this.bottomLeftColor.set(bottomLeftRgba);
        return this;
    }

    public Rectangle setVerticalGradient(Color top, Color bottom) {
        return setGradient(top, top, bottom, bottom);
    }

    public Rectangle setVerticalGradient(int topRgba, int bottomRgba) {
        return setGradient(topRgba, topRgba, bottomRgba, bottomRgba);
    }

    public Rectangle setHorizontalGradient(Color left, Color right) {
        return setGradient(left, right, right, left);
    }

    public Rectangle setHorizontalGradient(int leftRgba, int rightRgba) {
        return setGradient(leftRgba, rightRgba, rightRgba, leftRgba);
    }

    public boolean isFilled() { return filled; }
    public float getRotationDegrees() { return rotationDegrees; }
    public float getLineWidth() { return lineWidth; }
    public JoinType getJoinType() { return joinType; }
    public Color getColor() { return color; }
    public Color getTopRightColor() { return this.topRightColor; }
    public Color getTopLeftColor() { return this.topLeftColor; }
    public Color getBottomLeftColor() { return this.bottomLeftColor; }
    public Color getBottomRightColor() { return this.bottomRightColor; }


}
