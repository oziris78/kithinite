
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



package com.twistral.kithinite.demos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.twistral.kithinite.core.Layer;
import com.twistral.kithinite.TestUtils;
import com.twistral.kithinite.shapes.Triangle;


public class TriangleDemo extends ApplicationAdapter {

    private static final Color c1 = Color.CORAL, c2 = Color.LIME, c3 = Color.ROYAL;
    private static final Color ac1 = new Color(1f, 0f, 0.5f, 0.4f),
            ac2 = new Color(0.4f, 1f, 0.2f, 0.4f),
            ac3 = new Color(0f, 0.4f, 1f, 0.4f);

    private static final float PADDING = 25f;
    private static final float RA = 180; // rotation angle
    private static final int MAX_PER_ROW = 4;
    private static final int SCALE = 15;
    private static final float TRI_W = 10 * SCALE, TRI_H = 8 * SCALE;

    private Layer layer;
    private static int row = 0, col = 0;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(1100, 650);

        layer = new Layer();

        layer.getRoot().add(
                // Right angled
                tri('r', true).setColor(c1),
                tri('r', true).setColor(c1, c2, c3),
                tri('r', false).setColor(c1),
                tri('r', false).setColor(c1, c2, c3),

                // Equilateral-ish
                tri('e', true).setColor(c1),
                tri('e', true).setColor(c1, c2, c3),
                tri('e', false).setColor(c1),
                tri('e', false).setColor(c1, c2, c3),

                // Custom / Scalene
                tri('c', true).setColor(c1),
                tri('c', true).setColor(c1, c2, c3),
                tri('c', false).setColor(c1),
                tri('c', false).setColor(c1, c2, c3),

                // Showcase opacity (should be not full supported for filled triangles)
                // NOTE: it should work completely fine for outlined triangles even with 3 colors
                tri('c', true).setColor(ac1),
                tri('c', true).setColor(ac1, ac2, ac3),
                tri('c', false).setColor(ac1),
                tri('c', false).setColor(ac1, ac2, ac3),

                // Fixed width/height test
                tri('c', true).setColor(c1).setSize(0f, 0f).setSize(TRI_W, TRI_H),
                tri('c', true).setColor(c1, c2, c3).setSize(0f, TRI_H).setSize(TRI_W, TRI_H),
                tri('c', false).setColor(c1).setSize(TRI_W, 0f).setSize(TRI_W, TRI_H),
                tri('c', false).setColor(c1, c2, c3).setSize(TRI_W, TRI_H).setSize(TRI_W, TRI_H),

                // Rotated triangles
                tri('r', true).setColor(c1).setRotationDegrees(15f),
                tri('e', true).setColor(c1, c2, c3).setRotationDegrees(90f),
                tri('e', false).setColor(c1).setRotationDegrees(180f),
                tri('c', true).setColor(c1, c2, c3).setRotationDegrees(360f)
        );

    }


    private Triangle tri(char type, boolean filled) {
        Triangle triangle = new Triangle(filled, 0f, 0f, 0f, 0f, 0f, 0f, null);

        if (type == 'e')
            triangle.setVertices(0f, 0f, TRI_W / 2f, TRI_H, TRI_W, 0f);
        else if (type == 'r')
            triangle.setVertices(0f, 0f, 0f, TRI_H, TRI_W, 0f);
        else if (type == 'c')
            triangle.setVertices(0f, 0f, TRI_W * 0.5f, TRI_H, TRI_W, TRI_H * 0.2f);

        triangle.setXY(
            PADDING + (PADDING + TRI_W) * row,
            PADDING + (PADDING + TRI_H) * col
        );

        if (++col >= MAX_PER_ROW) {
            col = 0;
            row++;
        }

        return triangle;
    }


    @Override
    public void render() {
        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }

    @Override
    public void dispose() {
        layer.dispose();
    }


}


