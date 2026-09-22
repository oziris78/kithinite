
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
import com.twistral.tephrium.core.functions.TMath;
import space.earlygrey.shapedrawer.*;
import static com.twistral.kithinite.KithiniteUtils.*;


public class Rectangle extends Shape<Rectangle> {

    // Static variables
    public static final JoinType DEF_JOIN_TYPE = JoinType.POINTY;

    // Rectangle related variables
    private float rotationDegrees;
    private float lineWidth;
    private JoinType joinType;
    private Color topLeftColor, topRightColor, bottomRightColor, bottomLeftColor;


    public Rectangle(boolean filled, Color topLeftColor, Color topRightColor,
                     Color bottomRightColor, Color bottomLeftColor,
                     float rotationDegrees, float lineWidth, JoinType joinType)
    {
        super(filled);
        setColor(topLeftColor, topRightColor, bottomRightColor, bottomLeftColor);
        this.rotationDegrees = rotationDegrees;
        this.lineWidth = lineWidth;
        this.joinType = joinType;
    }

    public Rectangle(boolean filled, Color color,
                     float rotationDegrees, float lineWidth, JoinType joinType)
    {
        this(filled, color, color, color, color, rotationDegrees, lineWidth, joinType);
    }

    public Rectangle(boolean filled, Color topLeftColor, Color topRightColor,
                     Color bottomRightColor, Color bottomLeftColor)
    {
        this(filled, topLeftColor, topRightColor, bottomRightColor, bottomLeftColor,
                DEF_ROTATION_DEGREES, DEF_LINE_WIDTH, DEF_JOIN_TYPE);
    }

    public Rectangle(boolean filled, Color color) {
        this(filled, color, color, color, color, DEF_ROTATION_DEGREES, DEF_LINE_WIDTH, DEF_JOIN_TYPE);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    public void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;
        if (this.width <= this.lineWidth) return; // adjW will fail
        if (this.height <= this.lineWidth) return; // adjH will fail

        final float rotationRadians = this.rotationDegrees * MathUtils.degreesToRadians;

        final Color cTL = prioritySelect(this.topLeftColor, DEF_COLOR);
        final Color cTR = prioritySelect(this.topRightColor, DEF_COLOR);
        final Color cBR = prioritySelect(this.bottomRightColor, DEF_COLOR);
        final Color cBL = prioritySelect(this.bottomLeftColor, DEF_COLOR);

        // [MAIN PATH 1/2] Filled rects only need one render call
        if (filled) {
            drawer.filledRectangle(absX, absY, width, height, rotationRadians, cTR, cTL, cBL, cBR);
            return;
        }

        // [FAST PATH] Single color, unrotated, not-filled rects only need one render call too
        final boolean isSingleColor = cTL.equals(cTR) && cTR.equals(cBR) && cBR.equals(cBL);
        final boolean isRotated = !TMath.equalsf(rotationRadians, 0f);

        final float halfLine = lineWidth / 2f;
        final float adjX = absX + halfLine;
        final float adjY = absY + halfLine;
        final float adjW = width - lineWidth;
        final float adjH = height - lineWidth;

        if (isSingleColor && !isRotated) {
            final float oldColor = drawer.setColor(cTL);
            drawer.rectangle(adjX, adjY, adjW, adjH, lineWidth, 0f, joinType);
            drawer.setColor(oldColor);
            return;
        }

        // [MAIN PATH 2/2] Multi-color, not-filled rects need 4 render calls to preserve their gradients
        float x1, y1, x2, y2, x3, y3, x4, y4;

        // For some reason, multi-color not-filled rectangles with 1px lineWidth are always
        // missing 1 pixel at their top left corner because of float addition of halfLife.
        if (lineWidth <= 1f) {
            // V1 = BOTTOM LEFT
            // V2 = TOP LEFT
            // V3 = TOP RIGHT
            // V4 = BOTTOM RIGHT

            // To fix this bug we move:
            //   - top edge (V2+V3) 1px down
            //   - bottom left corner 1px right (without touching V2 since its correctly placed)
            x1 = absX + 1f;            y1 = absY;
            x2 = absX;                 y2 = absY + height - 1f;
            x3 = absX + width;         y3 = absY + height - 1f;
            x4 = absX + width;         y4 = absY;
        }
        else {
            x1 = adjX;                 y1 = adjY;
            x2 = adjX;                 y2 = adjY + adjH;
            x3 = adjX + adjW;          y3 = adjY + adjH;
            x4 = adjX + adjW;          y4 = adjY;
        }

        // Apply rotation around the center of the rectangle if needed (this will cause bleeding)
        if (isRotated) {
            final float cos = MathUtils.cos(rotationRadians),
                        sin = MathUtils.sin(rotationRadians);

            final float cx = absX + width / 2f,
                        cy = absY + height / 2f;

            float rx1 = cx + (x1 - cx) * cos - (y1 - cy) * sin;
            float ry1 = cy + (x1 - cx) * sin + (y1 - cy) * cos;
            x1 = rx1; y1 = ry1;

            float rx2 = cx + (x2 - cx) * cos - (y2 - cy) * sin;
            float ry2 = cy + (x2 - cx) * sin + (y2 - cy) * cos;
            x2 = rx2; y2 = ry2;

            float rx3 = cx + (x3 - cx) * cos - (y3 - cy) * sin;
            float ry3 = cy + (x3 - cx) * sin + (y3 - cy) * cos;
            x3 = rx3; y3 = ry3;

            float rx4 = cx + (x4 - cx) * cos - (y4 - cy) * sin;
            float ry4 = cy + (x4 - cx) * sin + (y4 - cy) * cos;
            x4 = rx4; y4 = ry4;
        }

        drawer.line(x2, y2, x3, y3, this.lineWidth, false, cTL, cTR); // Top edge
        drawer.line(x3, y3, x4, y4, this.lineWidth, false, cTR, cBR); // Right edge
        drawer.line(x4, y4, x1, y1, this.lineWidth, false, cBR, cBL); // Bottom edge
        drawer.line(x1, y1, x2, y2, this.lineWidth, false, cBL, cTL); // Left edge
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    /*////////////////  SETTERS WITH NO SIDE EFFECTS  ////////////////*/

    public Rectangle setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
        return this;
    }

    public Rectangle setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Rectangle setJoinType(JoinType joinType) {
        this.joinType = joinType;
        return this;
    }

    /*////////////////  UTILITY SETTERS  ////////////////*/

    @Override
    public Rectangle setColor(Color color) {
        this.topLeftColor = color;
        this.topRightColor = color;
        this.bottomRightColor = color;
        this.bottomLeftColor = color;
        return this;
    }

    public Rectangle setColor(Color topLeftColor, Color topRightColor,
                              Color bottomRightColor, Color bottomLeftColor)
    {
        this.topLeftColor = topLeftColor;
        this.topRightColor = topRightColor;
        this.bottomRightColor = bottomRightColor;
        this.bottomLeftColor = bottomLeftColor;
        return this;
    }

    // Just an alias to setColor(Color, Color, Color, Color)
    public Rectangle setFullGradient(Color topLeftColor, Color topRightColor,
                                     Color bottomRightColor, Color bottomLeftColor)
    {
        return this.setColor(topLeftColor, topRightColor, bottomRightColor, bottomLeftColor);
    }

    public Rectangle setVerticalGradient(Color topColor, Color bottomColor) {
        return this.setColor(topColor, topColor, bottomColor, bottomColor);
    }

    public Rectangle setHorizontalGradient(Color leftColor, Color rightColor) {
        return this.setColor(leftColor, rightColor, rightColor, leftColor);
    }

    /*////////////////  ALL GETTERS  ////////////////*/

    @Override
    public Color getColor() {
        return prioritySelect(topLeftColor, topRightColor, bottomRightColor, bottomLeftColor, null);
    }

    public float getRotationDegrees() { return rotationDegrees; }
    public float getLineWidth() { return lineWidth; }
    public JoinType getJoinType() { return joinType; }
    public Color getTopRightColor() { return this.topRightColor; }
    public Color getTopLeftColor() { return this.topLeftColor; }
    public Color getBottomLeftColor() { return this.bottomLeftColor; }
    public Color getBottomRightColor() { return this.bottomRightColor; }


}

