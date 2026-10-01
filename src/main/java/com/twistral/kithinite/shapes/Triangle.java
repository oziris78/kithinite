
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
import com.badlogic.gdx.math.MathUtils;
import com.twistral.kithinite.core.*;
import com.twistral.tephrium.core.functions.TMath;
import com.twistral.tephrium.core.functions.TRange;
import space.earlygrey.shapedrawer.*;
import static com.twistral.kithinite.KithiniteUtils.*;


/**
 * Represents a 2D triangle widget defined by three vertices relative to its bounding box. <br>
 * Supports both filled and outlined (not filled) rendering modes. <br>
 * <b>NOTE: Custom lineWidth values are NOT supported due to pixel imperfections.</b>
 * <b>NOTE: Custom opacity values (alpha < 1f) are NOT supported on filled triangles to prevent
 * edge double blending leftovers. Alpha values are clamped to 1f during render for filled triangles.</b>
 */
public class Triangle extends Shape<Triangle> {

    // Triangle related variables
    private float v1x, v1y, v2x, v2y, v3x, v3y;
    private Color v1Color, v2Color, v3Color;

    // [INTERNAL] normalized vertices for proper width/height scaling
    private float nv1x, nv1y, nv2x, nv2y, nv3x, nv3y;


    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y, float v3x,
                    float v3y, Color v1Color, Color v2Color, Color v3Color, float rotationDegrees)
    {
        super(filled, rotationDegrees);
        setVertices(v1x, v1y, v2x, v2y, v3x, v3y);
        setColor(v1Color, v2Color, v3Color);
    }


    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y, float v3x,
                    float v3y, Color color, float rotationDegrees)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, color, color, color, rotationDegrees);
    }


    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y, float v3x,
                    float v3y, Color v1Color, Color v2Color, Color v3Color)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, v1Color, v2Color, v3Color, DEF_ROTATION_DEGREES);
    }


    public Triangle(boolean filled, float v1x, float v1y, float v2x, float v2y, float v3x,
                    float v3y, Color color)
    {
        this(filled, v1x, v1y, v2x, v2y, v3x, v3y, color, color, color, DEF_ROTATION_DEGREES);
    }



    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    @Override
    public void render(ShapeDrawer drawer) {
        if (!this.visible) return;
        if (this.width <= 0 || this.height <= 0) return;

        final float nesterAbsX = this.nester.getAbsX(),
                    nesterAbsY = this.nester.getAbsY();

        float x1 = round2(nesterAbsX + this.v1x),
              x2 = round2(nesterAbsX + this.v2x),
              x3 = round2(nesterAbsX + this.v3x);

        float y1 = round2(nesterAbsY + this.v1y),
              y2 = round2(nesterAbsY + this.v2y),
              y3 = round2(nesterAbsY + this.v3y);

        final float rotationRadians = this.rotationDegrees * MathUtils.degreesToRadians;
        final boolean isRotated = !TMath.equalsf(rotationRadians, 0f);

        // Apply rotation around the center of mass if needed
        if (isRotated) {
            final float cos = MathUtils.cos(rotationRadians),
                        sin = MathUtils.sin(rotationRadians);

            final float cx = nesterAbsX + (this.v1x + this.v2x + this.v3x) / 3f,
                        cy = nesterAbsY + (this.v1y + this.v2y + this.v3y) / 3f;

            float rx1 = cx + (x1 - cx) * cos - (y1 - cy) * sin;
            float ry1 = cy + (x1 - cx) * sin + (y1 - cy) * cos;
            x1 = rx1; y1 = ry1;

            float rx2 = cx + (x2 - cx) * cos - (y2 - cy) * sin;
            float ry2 = cy + (x2 - cx) * sin + (y2 - cy) * cos;
            x2 = rx2; y2 = ry2;

            float rx3 = cx + (x3 - cx) * cos - (y3 - cy) * sin;
            float ry3 = cy + (x3 - cx) * sin + (y3 - cy) * cos;
            x3 = rx3; y3 = ry3;
        }

        // Fix the weird 1px bleeding bug that happens on right angle triangles
        final float v12x = x2 - x1, v12y = y2 - y1;
        final float v13x = x3 - x1, v13y = y3 - y1;
        final float v23x = x3 - x2, v23y = y3 - y2;

        final float dot1 = v12x * v13x + v12y * v13y;
        final float dot2 = (-v12x) * v23x + (-v12y) * v23y;
        final float dot3 = v13x * v23x + v13y * v23y;

        final boolean rightAngleAtV1 = Math.abs(dot1) < 0.01f;
        final boolean rightAngleAtV2 = Math.abs(dot2) < 0.01f;
        final boolean rightAngleAtV3 = Math.abs(dot3) < 0.01f;
        final boolean needsRightAngleFix = rightAngleAtV1 || rightAngleAtV2 || rightAngleAtV3;

        if (needsRightAngleFix) {
            final float minX = min(x1, x2, x3);
            final float minY = min(y1, y2, y3);
            final float maxY = max(y1, y2, y3);

            final float rx = rightAngleAtV1 ? x1 : (rightAngleAtV2 ? x2 : x3);
            final float ry = rightAngleAtV1 ? y1 : (rightAngleAtV2 ? y2 : y3);

            final boolean isLeft = Math.abs(rx - minX) < 0.01f;
            final boolean isBottom = Math.abs(ry - minY) < 0.01f;

            if (isBottom && isLeft) { // BOTTOM LEFT
                if(x1 == minX) x1++;
                if(x2 == minX) x2++;
                if(x3 == minX) x3++;
            }

            if (!isBottom && isLeft) { // TOP LEFT
                if (y1 == minY) x1++;
                else if (y2 == minY) x2++;
                else if (y3 == minY) x3++;

                if (y1 == maxY) y1--;
                if (y2 == maxY) y2--;
                if (y3 == maxY) y3--;
            }

            if (!isBottom && !isLeft) { // TOP RIGHT
                if (y1 == maxY) y1--;
                if (y2 == maxY) y2--;
                if (y3 == maxY) y3--;
            }
        }

        // Fix the weird upside down equilateral-ish triangle bleed bug
        if (!needsRightAngleFix) {
            final float minX = min(x1, x2, x3);
            final float minY = min(y1, y2, y3);
            final float maxX = max(x1, x2, x3);
            final float maxY = max(y1, y2, y3);

            final boolean isTopLeft1 = (x1 == minX && y1 == maxY);
            final boolean isTopLeft2 = (x2 == minX && y2 == maxY);
            final boolean isTopLeft3 = (x3 == minX && y3 == maxY);
            final boolean hasTopLeftCorner = isTopLeft1 || isTopLeft2 || isTopLeft3;

            final boolean isTopRight1 = (x1 == maxX && y1 == maxY);
            final boolean isTopRight2 = (x2 == maxX && y2 == maxY);
            final boolean isTopRight3 = (x3 == maxX && y3 == maxY);
            final boolean hasTopRightCorner = isTopRight1 || isTopRight2 || isTopRight3;

            final boolean hasBottomMidVertex = (maxX > x1 && x1 > minX && y1 == minY) ||
                    (maxX > x2 && x2 > minX && y2 == minY) || (maxX > x3 && x3 > minX && y3 == minY);

            final boolean needsBottomMidFix = hasTopLeftCorner && hasTopRightCorner && hasBottomMidVertex;

            if (needsBottomMidFix) {
                if(isTopLeft1) y1--;
                else if(isTopLeft2) y2--;
                else if(isTopLeft3) y3--;

                if(isTopRight1) y1--;
                else if(isTopRight2) y2--;
                else if(isTopRight3) y3--;
            }

            final boolean isBottomLeft1 = (x1 == minX && y1 == minY);
            final boolean isBottomLeft2 = (x2 == minX && y2 == minY);
            final boolean isBottomLeft3 = (x3 == minX && y3 == minY);
            final boolean hasBottomLeftCorner = isBottomLeft1 || isBottomLeft2 || isBottomLeft3;

            final boolean hasRightMidVertex = (maxY > y1 && y1 > minY && x1 == maxX) ||
                    (maxY > y2 && y2 > minY && x2 == maxX) || (maxY > y3 && y3 > minY && x3 == maxX);

            final boolean needsRightMidFix = hasTopLeftCorner && hasBottomLeftCorner && hasRightMidVertex;

            if (needsRightMidFix) {
                if (isTopLeft1) x1++;
                if (isTopLeft2) x2++;
                if (isTopLeft3) x3++;

                if (isBottomLeft1) x1++;
                if (isBottomLeft2) x2++;
                if (isBottomLeft3) x3++;
            }
        }

        final Color c1 = prioritySelect(this.v1Color, DEF_COLOR);
        final Color c2 = prioritySelect(this.v2Color, DEF_COLOR);
        final Color c3 = prioritySelect(this.v3Color, DEF_COLOR);

        final float c1Bits = getFloatBits(c1.r, c1.g, c1.b, 1f);
        final float c2Bits = getFloatBits(c2.r, c2.g, c2.b, 1f);
        final float c3Bits = getFloatBits(c3.r, c3.g, c3.b, 1f);

        // Fill the core polygon
        if (filled) {
            drawer.filledTriangle(x1, y1, x2, y2, x3, y3, c1Bits, c2Bits, c3Bits);
        }

        // Render the edges of the triangle to avoid pixel imperfections
        // at the cost of 3 additional line render calls each frame
        drawer.line(x1, y1, x2, y2, 1f, false, c1Bits, c2Bits);
        drawer.line(x2, y2, x3, y3, 1f, false, c2Bits, c3Bits);
        drawer.line(x3, y3, x1, y1, 1f, false, c3Bits, c1Bits);
    }


    @Override
    public Triangle flipVertically() {
        this.rotationDegrees = -this.rotationDegrees;
        this.v1y = 2f * this.y + this.height - this.v1y;
        this.v2y = 2f * this.y + this.height - this.v2y;
        this.v3y = 2f * this.y + this.height - this.v3y;

        recalcYNorms();
        return this;
    }


    @Override
    public Triangle flipHorizontally() {
        this.rotationDegrees = -this.rotationDegrees;
        this.v1x = 2f * this.x + this.width - this.v1x;
        this.v2x = 2f * this.x + this.width - this.v2x;
        this.v3x = 2f * this.x + this.width - this.v3x;

        recalcXNorms();
        return this;
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    /*////////////////  SETTERS WITH SIDE EFFECTS  ////////////////*/

    @Override
    public Triangle setX(float x) {
        float oldX = this.x;
        super.setX(x);
        float dx = this.x - oldX;
        this.v1x += dx;
        this.v2x += dx;
        this.v3x += dx;
        return this;
    }


    @Override
    public Triangle setY(float y) {
        float oldY = this.y;
        super.setY(y);
        float dy = this.y - oldY;
        this.v1y += dy;
        this.v2y += dy;
        this.v3y += dy;
        return this;
    }


    @Override
    public Triangle setWidth(float newWidth) {
        super.setWidth(newWidth);
        this.v1x = this.x + (this.nv1x * this.width);
        this.v2x = this.x + (this.nv2x * this.width);
        this.v3x = this.x + (this.nv3x * this.width);
        return this;
    }


    @Override
    public Triangle setHeight(float newHeight) {
        super.setHeight(newHeight);
        this.v1y = this.y + (this.nv1y * this.height);
        this.v2y = this.y + (this.nv2y * this.height);
        this.v3y = this.y + (this.nv3y * this.height);
        return this;
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

        recalcXNorms();
        recalcYNorms();
    }


    /*////////////////  SETTERS WITH NO SIDE EFFECTS  ////////////////*/

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

    @Override
    public Triangle setColor(Color color) {
        this.v1Color = color;
        this.v2Color = color;
        this.v3Color = color;
        return this;
    }

    public Triangle setColor(Color v1Color, Color v2Color, Color v3Color) {
        this.v1Color = v1Color;
        this.v2Color = v2Color;
        this.v3Color = v3Color;
        return this;
    }

    public Triangle setV1(float v1x, float v1y, Color v1Color) {
        return this.setV1Color(v1Color).setV1(v1x, v1y);
    }

    public Triangle setV2(float v2x, float v2y, Color v2Color) {
        return this.setV2Color(v2Color).setV2(v2x, v2y);
    }

    public Triangle setV3(float v3x, float v3y, Color v3Color) {
        return this.setV3Color(v3Color).setV3(v3x, v3y);
    }

    public Triangle setVertices(float v1x, float v1y, Color v1Color,
                                float v2x, float v2y, Color v2Color,
                                float v3x, float v3y, Color v3Color)
    {
        return this.setColor(v1Color, v2Color, v3Color)
                .setVertices(v1x, v1y, v2x, v2y, v3x, v3y);
    }

    /*////////////////  ALL GETTERS  ////////////////*/

    @Override
    public Color getColor() {
        return prioritySelect(this.v1Color, this.v2Color, this.v3Color, null);
    }

    public float getV1x() { return this.v1x; }
    public float getV1y() { return this.v1y; }
    public float getV2x() { return this.v2x; }
    public float getV2y() { return this.v2y; }
    public float getV3x() { return this.v3x; }
    public float getV3y() { return this.v3y; }
    public Color getV1Color() { return this.v1Color; }
    public Color getV2Color() { return this.v2Color; }
    public Color getV3Color() { return this.v3Color; }


    /*//////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  HELPER FUNCTIONS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////////*/


    private void recalcXNorms() {
        if (this.width > 0f) {
            this.nv1x = (this.v1x - this.x) / this.width;
            this.nv2x = (this.v2x - this.x) / this.width;
            this.nv3x = (this.v3x - this.x) / this.width;
        }
    }

    private void recalcYNorms() {
        if (this.height > 0f) {
            this.nv1y = (this.v1y - this.y) / this.height;
            this.nv2y = (this.v2y - this.y) / this.height;
            this.nv3y = (this.v3y - this.y) / this.height;
        }
    }


}

