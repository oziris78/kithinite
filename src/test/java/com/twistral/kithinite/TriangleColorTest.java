
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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


public class TriangleColorTest extends ApplicationAdapter {

    private static final Color COLOR0 = Color.PURPLE;
    private static final Color COLOR1 = Color.RED;
    private static final Color COLOR2 = Color.CYAN;
    private static final Color COLOR3 = Color.GOLD;
    private static final int MAX_PER_ROW = 3;

    private Layer layer;

    private int row = 0, col = 0;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Triangle Color Test");
        Gdx.graphics.setWindowedMode(1250, 500);

        layer = new Layer();

        layer.getRoot().add(
            // filled triangle #1 with 6 diff colors
            tri(true, 50, 50, 150, 50, 100, 150),
            tri(true, 1f, 1f, 2f, 1f, 1.5f, 2f),
            tri(true, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(true, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(true, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(true, 0f, 0f, 1f, 0f, 0.5f, 1f),

            // outlined triangle #1 with 6 diff colors
            tri(false, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(false, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(false, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(false, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(false, 0f, 0f, 1f, 0f, 0.5f, 1f),
            tri(false, 0f, 0f, 1f, 0f, 0.5f, 1f),

            // filled triangle #2 with 6 diff colors
            tri(true, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(true, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(true, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(true, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(true, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(true, 0f, 0f, 6f, 20f, 20f, 12f),

            // outlined triangle #2 with 6 diff colors
            tri(false, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(false, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(false, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(false, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(false, 0f, 0f, 6f, 20f, 20f, 12f),
            tri(false, 0f, 0f, 6f, 20f, 20f, 12f)
        );
    }

    private Triangle tri(boolean filled, float v1x, float v1y, float v2x, float v2y, float v3x, float v3y) {
        Triangle triangle = new Triangle(filled, v1x, v1y, v2x, v2y, v3x, v3y, null);

        int colorIndex = (row * MAX_PER_ROW + col) % 6;
        if (colorIndex == 0) triangle.setColor(COLOR0);
        if (colorIndex == 1) triangle.setColor(COLOR1);
        if (colorIndex == 2) triangle.setColor(COLOR2);
        if (colorIndex == 3) triangle.setColor(COLOR0, COLOR2, COLOR3);
        if (colorIndex == 4) triangle.setColor(COLOR1, COLOR3, COLOR0);
        if (colorIndex == 5) triangle.setColor(COLOR2, COLOR0, COLOR1);

        triangle.setSize(100, 100);
        triangle.setXY(50 + 150f * row, 50 + 150f * col);

        if (++col == MAX_PER_ROW) {
            row++;
            col = 0;
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


