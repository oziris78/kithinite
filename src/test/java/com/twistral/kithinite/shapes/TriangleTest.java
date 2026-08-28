
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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.twistral.kithinite.Layer;
import com.twistral.kithinite.Triangle;


public class TriangleTest extends ApplicationAdapter {

    private static final int SCALE = 12;

    private static final float TRI_WIDTH = 12 * SCALE, TRI_HEIGHT = 10 * SCALE;
    private static final float PADDING = 25;
    private static final int MAX_PER_ROW = 4;

    private static final Color C1 = Color.GOLD, C2 = Color.ORANGE, C3 = Color.PURPLE,
                               AC1 = new Color(1f, 0f, 0f, 0.4f),
                               AC2 = new Color(0f, 1f, 0f, 0.4f),
                               AC3 = new Color(0f, 0f, 1f, 0.4f);

    private Layer layer;
    private static int row = 0, col = 0;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Triangles");
        Gdx.graphics.setWindowedMode(800, 600);

        layer = new Layer();

        layer.getRoot().add(
                // All color tests
                tri(true).setColor(C1),
                tri(true).setColor(C1, C2, C3),
                tri(false).setColor(C1),
                tri(false).setColor(C1, C2, C3),

                // Showcase opacity (should be not full supported for filled triangles)
                // NOTE: it should work completely fine for outlined triangles even with 3 colors
                tri(true).setColor(AC1),
                tri(true).setColor(AC1, AC2, AC3),
                tri(false).setColor(AC1),
                tri(false).setColor(AC1, AC2, AC3),

                // Fixed width/height test
                tri(true).setColor(C1).setSize(0f, 0f).setSize(TRI_WIDTH, TRI_HEIGHT),
                tri(true).setColor(C2).setSize(0f, TRI_HEIGHT).setSize(TRI_WIDTH, TRI_HEIGHT),
                tri(true).setColor(C3).setSize(TRI_WIDTH, 0f).setSize(TRI_WIDTH, TRI_HEIGHT),
                tri(true).setColor(C1).setSize(TRI_WIDTH, TRI_HEIGHT).setSize(TRI_WIDTH, TRI_HEIGHT)
        );
    }


    private Triangle tri(boolean filled) {
        Triangle triangle = new Triangle(
            filled,
            0f, 0f,
            TRI_WIDTH / 2f, TRI_HEIGHT,
            TRI_WIDTH, 0f,
            null
        );

        triangle.setXY(
            PADDING + (PADDING + TRI_WIDTH) * row,
            PADDING + (PADDING + TRI_HEIGHT) * col
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



