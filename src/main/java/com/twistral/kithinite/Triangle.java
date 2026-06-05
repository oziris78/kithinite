
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
import com.twistral.tephrium.core.functions.TMath;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;



public class Triangle extends Widget {

    // Static variables
    public static final Color DEF_COLOR = Color.WHITE;
    public static final float DEF_LINE_WIDTH = 1f;

    // Triangle related variables
    private boolean filled;
    private float v1x, v1y, v2x, v2y, v3x, v3y;
    private float lineWidth;

    // Color variables
    private Color color;
    private Color v1Color, v2Color, v3Color;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    private Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                     float v3x, float v3y, float lineWidth,
                     Color color, Color v1Color, Color v2Color, Color v3Color)
    {
        this.filled = filled;
        this.lineWidth = lineWidth;
        this.color = color;
        this.setVertices(v1x, v1y, v1Color, v2x, v2y, v2Color, v3x, v3y, v3Color);
    }

    // Main constructor for triangles with a single color
    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                    float v3x, float v3y, Color color, float lineWidth)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, lineWidth, color, null, null, null);
    }

    // Main constructor for triangles with a gradient
    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y, float v3x,
                    float v3y, Color v1Color, Color v2Color, Color v3Color, float lineWidth)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, lineWidth, null, v1Color, v2Color, v3Color);
    }


    // Secondary constructor for triangles with a single color
    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                    float v3x, float v3y, Color color)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, color, DEF_LINE_WIDTH);
    }

    // Secondary constructor for triangles with a gradient
    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                    float v3x, float v3y, Color v1Color, Color v2Color, Color v3Color)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, v1Color, v2Color, v3Color, DEF_LINE_WIDTH);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;

        // Absolute coord calculation for v1, v2, v3. absX and absY are automatically
        // calculated but not needed here. Instead of x, y we have relative v1, v2, v3 vertices
        final float x1 = this.nester.absX + v1x,
                    x2 = this.nester.absX + v2x,
                    x3 = this.nester.absX + v3x;

        final float y1 = this.nester.absY + v1y,
                    y2 = this.nester.absY + v2y,
                    y3 = this.nester.absY + v3y;

        if (filled) {
            Color c1 = this.v1Color;
            if (c1 == null) c1 = this.color;
            if (c1 == null) c1 = DEF_COLOR;

            Color c2 = this.v2Color;
            if (c2 == null) c2 = this.color;
            if (c2 == null) c2 = DEF_COLOR;

            Color c3 = this.v3Color;
            if (c3 == null) c3 = this.color;
            if (c3 == null) c3 = DEF_COLOR;

            drawer.filledTriangle(
                x1, y1, x2, y2, x3, y3, c1.toFloatBits(), c2.toFloatBits(), c3.toFloatBits()
            );
        }
        else {
            Color c = this.color;
            if (c == null) c = DEF_COLOR;

            drawer.triangle(x1, y1, x2, y2, x3, y3, lineWidth, c.toFloatBits());
        }
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    /*////////////////  SETTERS WITH SIDE EFFECTS  ////////////////*/

    @Override
    public Piece setX(float x) {
        float oldX = this.x;
        super.setX(x);
        float dx = this.x - oldX;
        this.v1x += dx;
        this.v2x += dx;
        this.v3x += dx;
        return this;
    }

    @Override
    public Piece setY(float y) {
        float oldY = this.y;
        super.setY(y);
        float dy = this.y - oldY;
        this.v1y += dy;
        this.v2y += dy;
        this.v3y += dy;
        return this;
    }

    @Override
    public Piece setWidth(float newWidth) {
        final float oldWidth = this.width;
        if (TMath.equalsf(oldWidth, newWidth)) return this;

        super.setWidth(newWidth);

        // To scale the triangle correctly we need to have the coordinate system's origin to
        // be equal to the triangle's (x,y) so that we can just multiply the vertices by the
        // scale and get the correct values without moving the triangle. After scaling, we will
        // add (x,y) change to go back to the original coordinate system.
        if (!TMath.equalsf(oldWidth, 0f)) {
            final float scale = newWidth / oldWidth;
            final float dx = this.x;

            this.v1x = (this.v1x - dx) * scale + dx;
            this.v2x = (this.v2x - dx) * scale + dx;
            this.v3x = (this.v3x - dx) * scale + dx;
        }

        return this;
    }

    @Override
    public Piece setHeight(float newHeight) {
        final float oldHeight = this.height;
        if (TMath.equalsf(oldHeight, newHeight)) return this;

        super.setHeight(newHeight);

        // To scale the triangle correctly we need to have the coordinate system's origin to
        // be equal to the triangle's (x,y) so that we can just multiply the vertices by the
        // scale and get the correct values without moving the triangle. After scaling, we will
        // add (x,y) change to go back to the original coordinate system.
        if (!TMath.equalsf(oldHeight, 0f)) {
            final float scale = newHeight / oldHeight;
            final float dy = this.y;

            this.v1y = (this.v1y - dy) * scale + dy;
            this.v2y = (this.v2y - dy) * scale + dy;
            this.v3y = (this.v3y - dy) * scale + dy;
        }

        return this;
    }

    public Triangle setVertices(float v1x, float v1y, float v2x, float v2y, float v3x, float v3y) {
        this.v1x = v1x;
        this.v1y = v1y;
        this.v2x = v2x;
        this.v2y = v2y;
        this.v3x = v3x;
        this.v3y = v3y;
        syncBoundingBox();
        return this;
    }

    public Triangle setV1(float v1x, float v1y) {
        this.v1x = v1x;
        this.v1y = v1y;
        syncBoundingBox();
        return this;
    }

    public Triangle setV2(float v2x, float v2y) {
        this.v2x = v2x;
        this.v2y = v2y;
        syncBoundingBox();
        return this;
    }

    public Triangle setV3(float v3x, float v3y) {
        this.v3x = v3x;
        this.v3y = v3y;
        syncBoundingBox();
        return this;
    }

    private void syncBoundingBox() {
        final float minX = Math.min(Math.min(v1x, v2x), v3x);
        final float minY = Math.min(Math.min(v1y, v2y), v3y);
        final float maxX = Math.max(Math.max(v1x, v2x), v3x);
        final float maxY = Math.max(Math.max(v1y, v2y), v3y);

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    /*////////////////  SETTERS WITH NO SIDE EFFECTS  ////////////////*/

    public Triangle setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public Triangle setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public Triangle setColor(Color color) {
        this.color = color;
        return this;
    }

    public Triangle setV1Color(Color v1Color) {
        this.v1Color = v1Color;
        return this;
    }

    public Triangle setV2Color(Color v2Color) {
        this.v2Color = v2Color;
        return this;
    }

    public Triangle setV3Color(Color v3Color) {
        this.v3Color = v3Color;
        return this;
    }

    /*////////////////  UTILITY SETTERS  ////////////////*/

    public Triangle setV1(float v1x, float v1y, Color v1Color) {
        this.setV1Color(v1Color);
        return this.setV1(v1x, v1y);
    }

    public Triangle setV2(float v2x, float v2y, Color v2Color) {
        this.setV2Color(v2Color);
        return this.setV2(v2x, v2y);
    }

    public Triangle setV3(float v3x, float v3y, Color v3Color) {
        this.setV3Color(v3Color);
        return this.setV3(v3x, v3y);
    }

    public Triangle setVertices(float v1x, float v1y, Color v1Color,
                                float v2x, float v2y, Color v2Color,
                                float v3x, float v3y, Color v3Color)
    {
        this.setV1Color(v1Color);
        this.setV2Color(v2Color);
        this.setV3Color(v3Color);
        return this.setVertices(v1x, v1y, v2x, v2y, v3x, v3y);
    }

    /*////////////////  ALL GETTERS  ////////////////*/

    public boolean isFilled() { return this.filled; }
    public float getV1x() { return this.v1x; }
    public float getV1y() { return this.v1y; }
    public float getV2x() { return this.v2x; }
    public float getV2y() { return this.v2y; }
    public float getV3x() { return this.v3x; }
    public float getV3y() { return this.v3y; }
    public float getLineWidth() { return this.lineWidth; }
    public Color getColor() { return this.color; }
    public Color getV1Color() { return this.v1Color; }
    public Color getV2Color() { return this.v2Color; }
    public Color getV3Color() { return this.v3Color; }


}

    