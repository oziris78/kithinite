
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



import com.badlogic.gdx.graphics.*;
import com.twistral.tephrium.core.functions.*;
import space.earlygrey.shapedrawer.*;
import static com.twistral.kithinite.Kithinite.*;


/**
 * Represents a 2D triangle widget defined by three vertices relative to its bounding box. <br>
 * Supports both filled and and outlined (not filled) rendering modes. <br>
 * <b>NOTE: Custom lineWidth values are NOT supported due to pixel imperfections.</b>
 * <b>NOTE: Custom opacity values (alpha < 1f) are NOT supported on filled triangles to prevent
 * edge double blending leftovers. Alpha values are clamped to 1f during render for filled triangles.</b>
 */
public class Triangle extends Widget {

    // Static variables
    public static final Color DEF_COLOR = Color.WHITE;

    // Triangle related variables
    private boolean filled;
    private float v1x, v1y, v2x, v2y, v3x, v3y;

    // Color variables
    private Color color;
    private Color v1Color, v2Color, v3Color;

    // Variables for correct triangle scaling (setWidth, setHeight, setSize)
    private float v1xNorm, v1yNorm, v2xNorm, v2yNorm, v3xNorm, v3yNorm;

    // For performance
    private final Color tempColor = new Color();


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    private Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                     float v3x, float v3y, Color color, Color v1Color, Color v2Color, Color v3Color)
    {
        this.filled = filled;
        this.color = color;
        this.setVertices(v1x, v1y, v1Color, v2x, v2y, v2Color, v3x, v3y, v3Color);
    }

    // Main constructor for triangles with a single color
    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                    float v3x, float v3y, Color color)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, color, null, null, null);
    }

    // Main constructor for triangles with a gradient
    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y,
                    float v3x, float v3y, Color v1Color, Color v2Color, Color v3Color)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, null, v1Color, v2Color, v3Color);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;

        final float x1 = this.nester.absX + v1x;
        final float y1 = this.nester.absY + v1y;
        final float x2 = this.nester.absX + v2x;
        final float y2 = this.nester.absY + v2y;
        final float x3 = this.nester.absX + v3x;
        final float y3 = this.nester.absY + v3y;

        final Color c1 = prioritySelect(this.v1Color, this.color, DEF_COLOR);
        final Color c2 = prioritySelect(this.v2Color, this.color, DEF_COLOR);
        final Color c3 = prioritySelect(this.v3Color, this.color, DEF_COLOR);

        final float c1Bits = tempColor.set(c1.r, c1.g, c1.b, 1f).toFloatBits();
        final float c2Bits = tempColor.set(c2.r, c2.g, c2.b, 1f).toFloatBits();
        final float c3Bits = tempColor.set(c3.r, c3.g, c3.b, 1f).toFloatBits();

        // Render the edges of the triangle
        drawer.line(x1, y1, x2, y2, 1f, false, c1Bits, c2Bits);
        drawer.line(x2, y2, x3, y3, 1f, false, c2Bits, c3Bits);
        drawer.line(x3, y3, x1, y1, 1f, false, c3Bits, c1Bits);

        // Fill the core polygon
        if (filled) {
            drawer.filledTriangle(x1, y1, x2, y2, x3, y3, c1Bits, c2Bits, c3Bits);
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
        super.setWidth(newWidth);
        recalculateVerticesFromNorm();
        return this;
    }


    @Override
    public Piece setHeight(float newHeight) {
        super.setHeight(newHeight);
        recalculateVerticesFromNorm();
        return this;
    }


    private void recalculateVerticesFromNorm() {
        this.v1x = this.x + (this.v1xNorm * this.width);
        this.v1y = this.y + (this.v1yNorm * this.height);
        this.v2x = this.x + (this.v2xNorm * this.width);
        this.v2y = this.y + (this.v2yNorm * this.height);
        this.v3x = this.x + (this.v3xNorm * this.width);
        this.v3y = this.y + (this.v3yNorm * this.height);
    }


    public Triangle setVertices(float v1x, float v1y, float v2x, float v2y, float v3x, float v3y) {
        this.v1x = v1x; this.v1y = v1y;
        this.v2x = v2x; this.v2y = v2y;
        this.v3x = v3x; this.v3y = v3y;
        syncBoundingBox();
        return this;
    }

    public Triangle setV1(float v1x, float v1y) {
        this.v1x = v1x; this.v1y = v1y;
        syncBoundingBox();
        return this;
    }

    public Triangle setV2(float v2x, float v2y) {
        this.v2x = v2x; this.v2y = v2y;
        syncBoundingBox();
        return this;
    }

    public Triangle setV3(float v3x, float v3y) {
        this.v3x = v3x; this.v3y = v3y;
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

        if (this.width > 0f) {
            this.v1xNorm = (v1x - minX) / this.width;
            this.v2xNorm = (v2x - minX) / this.width;
            this.v3xNorm = (v3x - minX) / this.width;
        }

        if (this.height > 0f) {
            this.v1yNorm = (v1y - minY) / this.height;
            this.v2yNorm = (v2y - minY) / this.height;
            this.v3yNorm = (v3y - minY) / this.height;
        }
    }

    public Triangle setColor(Color color) {
        this.color = color;
        this.v1Color = null;
        this.v2Color = null;
        this.v3Color = null;
        return this;
    }

    /*////////////////  SETTERS WITH NO SIDE EFFECTS  ////////////////*/

    public Triangle setFilled(boolean filled) {
        this.filled = filled;
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

    public Triangle setColor(Color v1Color, Color v2Color, Color v3Color) {
        return this.setV1Color(v1Color).setV2Color(v2Color).setV3Color(v3Color);
    }

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
    public Color getColor() { return this.color; }
    public Color getV1Color() { return this.v1Color; }
    public Color getV2Color() { return this.v2Color; }
    public Color getV3Color() { return this.v3Color; }


}


