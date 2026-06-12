
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
import com.badlogic.gdx.math.Vector2;
import com.twistral.tephrium.core.functions.TMath;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.twistral.kithinite.Kithinite.*;


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

    // Optimization & rendering related variables
    private float rv1x, rv1y, rv2x, rv2y, rv3x, rv3y;
    private float iv1x, iv1y, iv2x, iv2y, iv3x, iv3y;


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

        // Calculate render/outer vertices in absolute coords using relative v1, v2, v3 values
        this.rv1x = this.nester.absX + v1x;
        this.rv1y = this.nester.absY + v1y;
        this.rv2x = this.nester.absX + v2x;
        this.rv2y = this.nester.absY + v2y;
        this.rv3x = this.nester.absX + v3x;
        this.rv3y = this.nester.absY + v3y;

        if (filled) {
            Color c1 = prioritySelect(this.v1Color, this.color, DEF_COLOR);
            Color c2 = prioritySelect(this.v2Color, this.color, DEF_COLOR);
            Color c3 = prioritySelect(this.v3Color, this.color, DEF_COLOR);

            drawer.filledTriangle(
                rv1x, rv1y, rv2x, rv2y, rv3x, rv3y,
                c1.toFloatBits(), c2.toFloatBits(), c3.toFloatBits()
            );
        }
        else {
            final Color c = prioritySelect(this.color, DEF_COLOR);
            final float cBits = c.toFloatBits();

            if (this.lineWidth <= DEF_LINE_WIDTH) {
                drawer.triangle(rv1x, rv1y, rv2x, rv2y, rv3x, rv3y, this.lineWidth, cBits);
            }
            else {
                // Use MESH RENDERING to achieve thick lines without spilling
                addPaddingToVertices(); // changes rv1, rv2, rv3
                computeInnerVertices(); // computes iv1, iv2, iv3

                // Edge 1 to 2
                drawer.filledTriangle(rv1x, rv1y, rv2x, rv2y, iv2x, iv2y, cBits, cBits, cBits);
                drawer.filledTriangle(rv1x, rv1y, iv2x, iv2y, iv1x, iv1y, cBits, cBits, cBits);

                // Edge 2 to 3
                drawer.filledTriangle(rv2x, rv2y, rv3x, rv3y, iv3x, iv3y, cBits, cBits, cBits);
                drawer.filledTriangle(rv2x, rv2y, iv3x, iv3y, iv2x, iv2y, cBits, cBits, cBits);

                // Edge 3 to 1
                drawer.filledTriangle(rv3x, rv3y, rv1x, rv1y, iv1x, iv1y, cBits, cBits, cBits);
                drawer.filledTriangle(rv3x, rv3y, iv1x, iv1y, iv3x, iv3y, cBits, cBits, cBits);
            }
        }
    }



    /**
     * Pads certain vertices (outer edge vertices of the bounding box) by half a pixel
     * to try to prevent rounding errors
     */
    private void addPaddingToVertices() {
        final float padding = 0.5f;
        final float minBoundX = this.nester.absX + min(v1x, v2x, v3x);
        final float maxBoundX = this.nester.absX + max(v1x, v2x, v3x);
        final float minBoundY = this.nester.absY + min(v1y, v2y, v3y);
        final float maxBoundY = this.nester.absY + max(v1y, v2y, v3y);

        if (rv1x == maxBoundX) rv1x += padding;
        if (rv2x == maxBoundX) rv2x += padding;
        if (rv3x == maxBoundX) rv3x += padding;

        if (rv1y == maxBoundY) rv1y += padding;
        if (rv2y == maxBoundY) rv2y += padding;
        if (rv3y == maxBoundY) rv3y += padding;

        if (rv1x == minBoundX) rv1x -= padding;
        if (rv2x == minBoundX) rv2x -= padding;
        if (rv3x == minBoundX) rv3x -= padding;

        if (rv1y == minBoundY) rv1y -= padding;
        if (rv2y == minBoundY) rv2y -= padding;
        if (rv3y == minBoundY) rv3y -= padding;
    }


    private void computeInnerVertices() {
        // Some triangles can have way too sharp edges etc. meaning they can be "collapsed" or
        // "degenerate" without spilling so its better to just draw them raw. We can check this
        // by looking at the edge vector's length (we use len^2 to check since sqrt isnt needed here)
        float edge12x = rv2x - rv1x, edge12y = rv2y - rv1y;
        float edge23x = rv3x - rv2x, edge23y = rv3y - rv2y;
        float edge31x = rv1x - rv3x, edge31y = rv1y - rv3y;

        final float lenSq12 = edge12x * edge12x + edge12y * edge12y;
        final float lenSq23 = edge23x * edge23x + edge23y * edge23y;
        final float lenSq31 = edge31x * edge31x + edge31y * edge31y;

        if (lenSq12 <= 0 || lenSq23 <= 0 || lenSq31 <= 0) {
            this.iv1x = rv1x; this.iv1y = rv1y;
            this.iv2x = rv2x; this.iv2y = rv2y;
            this.iv3x = rv3x; this.iv3y = rv3y;
            return;
        }

        // Since the triangle isnt collapsed/degenerate we can normalize the edge vectors
        final float len12 = (float) Math.sqrt(lenSq12);
        final float len23 = (float) Math.sqrt(lenSq23);
        final float len31 = (float) Math.sqrt(lenSq31);

        edge12x = edge12x / len12;
        edge12y = edge12y / len12;
        edge23x = edge23x / len23;
        edge23y = edge23y / len23;
        edge31x = edge31x / len31;
        edge31y = edge31y / len31;

        // Now we determine our normal vectors which are pointing towards the inside of the triangle.
        // To determine them we calculate the cross prod. and determine if the vertices are laid
        // out clockwise or counter-clockwise.
        final float crossProduct = edge12x * edge23y - edge12y * edge23x;
        final float sign = (crossProduct > 0) ? 1f : -1f;

        final float n12x = -edge12y * sign, n12y = edge12x * sign;
        final float n23x = -edge23y * sign, n23y = edge23x * sign;
        final float n31x = -edge31y * sign, n31y = edge31x * sign;

        // Now we take a point on each outer edge and shift it inward along our normal vectors by
        // lineWidth amount. This gives us an absolute tracking point on each shifted line segment
        // defining the inner perimeter. We will intersect these inner walls to determine our
        // inner vertices (iv1, iv2, iv3)
        final float path12x = rv1x + n12x * this.lineWidth,
                    path12y = rv1y + n12y * this.lineWidth,
                    path23x = rv2x + n23x * this.lineWidth,
                    path23y = rv2y + n23y * this.lineWidth,
                    path31x = rv3x + n31x * this.lineWidth,
                    path31y = rv3y + n31y * this.lineWidth;

        // Intersect path31 and path12  =>  calculate iv1
        // If the denominator is close to 0 then the lines are nearly/exactly parallel
        final float denom1 = edge31x * edge12y - edge31y * edge12x;
        if (Math.abs(denom1) > 0.001f) {
            float t1 = ((path12x - path31x) * edge12y - (path12y - path31y) * edge12x) / denom1;
            this.iv1x = path31x + edge31x * t1;
            this.iv1y = path31y + edge31y * t1;
        }
        else {
            this.iv1x = rv1x;
            this.iv1y = rv1y;
        }

        // Intersect path23 and path12  =>  calculate iv2
        final float denom2 = edge12x * edge23y - edge12y * edge23x;
        if (Math.abs(denom2) > 0.001f) {
            float t2 = ((path23x - path12x) * edge23y - (path23y - path12y) * edge23x) / denom2;
            this.iv2x = path12x + edge12x * t2;
            this.iv2y = path12y + edge12y * t2;
        }
        else {
            this.iv2x = rv2x;
            this.iv2y = rv2y;
        }

        // Intersect path31 and path23  =>  calculate iv3
        final float denom3 = edge23x * edge31y - edge23y * edge31x;
        if (Math.abs(denom3) > 0.001f) {
            float t3 = ((path31x - path23x) * edge31y - (path31y - path23y) * edge31x) / denom3;
            this.iv3x = path23x + edge23x * t3;
            this.iv3y = path23y + edge23y * t3;
        }
        else {
            this.iv3x = rv3x;
            this.iv3y = rv3y;
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

        // To scale the triangle correctly without changing x and y, we scale the vertices
        // relative to the triangle's bounding box origin (x and y) acting as our origin
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

        // To scale the triangle correctly without changing x and y, we scale the vertices
        // relative to the triangle's bounding box origin (x and y) acting as our origin
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
        final float minX = min(v1x, v2x, v3x);
        final float minY = min(v1y, v2y, v3y);
        final float maxX = max(v1x, v2x, v3x);
        final float maxY = max(v1y, v2y, v3y);

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

