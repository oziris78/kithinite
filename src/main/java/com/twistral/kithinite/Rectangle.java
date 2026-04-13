
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
                      Color color, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft)
    {
        this.color = (!filled && color == null) ? DEF_COLOR : color;
        this.rotationDegrees = rotationDegrees;
        this.bottomRightColor = bottomRight;
        this.bottomLeftColor = bottomLeft;
        this.topRightColor = topRight;
        this.topLeftColor = topLeft;
        this.lineWidth = lineWidth;
        this.joinType = joinType;
        this.filled = filled;
    }

    // Main constructor for rectangles with GRADIENTs
    public Rectangle(boolean filled, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft,
                     float rotationDegrees, float lineWidth, JoinType joinType)
    {
        this(
            filled, rotationDegrees, lineWidth, joinType,
            null, topLeft, topRight, bottomRight, bottomLeft
        );
    }

    // Main constructor for rectangles with COLORs
    public Rectangle(boolean filled, Color color, float rotationDegrees, float lineWidth, JoinType joinType) {
        this(
            filled, rotationDegrees, lineWidth, joinType,
            color, null, null, null, null
        );
    }

    // Secondary constructor for rectangles with GRADIENTs
    public Rectangle(boolean filled, Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
        this(
            filled, topLeft, topRight, bottomRight, bottomLeft,
            DEF_ROTATION_DEGREES, DEF_LINE_WIDTH, DEF_JOIN_TYPE
        );
    }

    // Secondary constructor for rectangles with COLORs
    public Rectangle(boolean filled, Color color) {
        this(
            filled, color,
            DEF_ROTATION_DEGREES, DEF_LINE_WIDTH, DEF_JOIN_TYPE
        );
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;

        // SANITY CHECK: Ensure we have SOMETHING to draw with
        if (this.color == null && topLeftColor == null) {
            this.setColor(DEF_COLOR);
        }

        final float rotationRadians = this.rotationDegrees * MathUtils.degreesToRadians;

        if (filled) {
            final Color c1 = (color != null) ? color : topRightColor;
            final Color c2 = (color != null) ? color : topLeftColor;
            final Color c3 = (color != null) ? color : bottomLeftColor;
            final Color c4 = (color != null) ? color : bottomRightColor;
            drawer.filledRectangle(absX, absY, width, height, rotationRadians, c1, c2, c3, c4);
        }
        else {
            // color CANT be null here since that means rect is outlined but the color is missing
            final Color outlineColor = (this.color != null) ? this.color : DEF_COLOR;

            // prevent spilling because of lineWidth variable
            final float halfLine = lineWidth / 2f;
            final float adjX = absX + halfLine;
            final float adjY = absY + halfLine;
            final float adjW = width - lineWidth;
            final float adjH = height - lineWidth;

            // Finally render the rectangle
            final float oldColor = drawer.setColor(outlineColor);
            drawer.rectangle(adjX, adjY, adjW, adjH, lineWidth, rotationRadians, joinType);
            drawer.setColor(oldColor);
        }
    }


    /*////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  COLOR SETTINGS  ///////////////////////////*/
    /*////////////////////////////////////////////////////////////////////////*/


    public Rectangle setColor(Color color) {
        this.color = color;
        this.topRightColor = null;
        this.topLeftColor = null;
        this.bottomLeftColor = null;
        this.bottomRightColor = null;
        return this;
    }

    public Rectangle setGradient(Color topLeft, Color topRight, Color bottomRight, Color bottomLeft) {
        this.color = null;
        this.topLeftColor = Objects.requireNonNull(topLeft, "topLeftColor cannot be null");
        this.topRightColor = Objects.requireNonNull(topRight, "topRightColor cannot be null");
        this.bottomRightColor = Objects.requireNonNull(bottomRight, "bottomRightColor cannot be null");
        this.bottomLeftColor = Objects.requireNonNull(bottomLeft, "bottomLeftColor cannot be null");
        return this;
    }

    public Rectangle setVerticalGradient(Color top, Color bottom) {
        return setGradient(top, top, bottom, bottom);
    }

    public Rectangle setHorizontalGradient(Color left, Color right) {
        return setGradient(left, right, right, left);
    }

    /*////////////////  RGBA Overrides ////////////////*/

    public Rectangle setColor(int rgba) {
        return this.setColor(new Color(rgba));
    }

    public Rectangle setGradient(int topLeftRgba, int topRightRgba, int bottomRightRgba, int bottomLeftRgba) {
        return this.setGradient(new Color(topLeftRgba), new Color(topRightRgba),
                new Color(bottomRightRgba), new Color(bottomLeftRgba));
    }

    public Rectangle setVerticalGradient(int topRgba, int bottomRgba) {
        return this.setVerticalGradient(new Color(topRgba), new Color(bottomRgba));
    }

    public Rectangle setHorizontalGradient(int leftRgba, int rightRgba) {
        return this.setHorizontalGradient(new Color(leftRgba), new Color(rightRgba));
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/


    public Rectangle setRotationDegrees(float rotDeg) { this.rotationDegrees = rotDeg; return this; }
    public Rectangle setJoinType(JoinType joinType) { this.joinType = joinType; return this; }
    public Rectangle setLineWidth(float lineWidth) { this.lineWidth = lineWidth; return this; }
    public Rectangle setFilled(boolean filled) { this.filled = filled; return this; }

    public float getRotationDegrees() { return rotationDegrees; }
    public JoinType getJoinType() { return joinType; }
    public float getLineWidth() { return lineWidth; }
    public boolean isFilled() { return filled; }

    public Color getColor() { return color; }
    public Color getTopLeftColor() { return this.topLeftColor; }
    public Color getTopRightColor() { return this.topRightColor; }
    public Color getBottomRightColor() { return this.bottomRightColor; }
    public Color getBottomLeftColor() { return this.bottomLeftColor; }


}
