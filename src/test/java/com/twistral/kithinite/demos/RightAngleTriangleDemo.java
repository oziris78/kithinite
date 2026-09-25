
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
import com.twistral.kithinite.TestUtils;
import com.twistral.kithinite.core.Layer;
import com.twistral.kithinite.shapes.Triangle;


public class RightAngleTriangleDemo extends ApplicationAdapter {

    private static final Color c1 = Color.CORAL, c2 = Color.LIME, c3 = Color.ROYAL;

    private final static float[][] templates = {
            {0f, 0f, 0f, 1f, 1f, 0f},
            {0f, 0f, 0f, 1f, 1f, 1f},
            {0f, 0f, 1f, 0f, 1f, 1f},
            {0f, 1f, 1f, 0f, 1f, 1f}
    };

    private static final float PADDING = 25f;
    private static final int MAX_PER_ROW = templates.length;
    private static final int SCALE = 15;
    private static final float TRI_W = 10 * SCALE, TRI_H = 8 * SCALE;

    private Layer layer;
    private static int row = 0, col = 0;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(1100, 650);

        layer = new Layer();

        for (int i = 0; i < templates.length; i++)
            layer.getRoot().add(tri(i, true).setColor(c1));
        for (int i = 0; i < templates.length; i++)
            layer.getRoot().add(tri(i, false).setColor(c1));
        for (int i = 0; i < templates.length; i++)
            layer.getRoot().add(tri(i, true).setColor(c1, c2, c3));
        for (int i = 0; i < templates.length; i++)
            layer.getRoot().add(tri(i, false).setColor(c1, c2, c3));

    }


    private Triangle tri(int tempIndex, boolean filled) {
        Triangle triangle = new Triangle(filled, 0f, 0f, 0f, 0f, 0f, 0f, null);
        float[] t = templates[tempIndex];
        triangle.setVertices(t[0], t[1], t[2], t[3], t[4], t[5]);

        triangle.setXY(
            PADDING + (PADDING + TRI_W) * row,
            PADDING + (PADDING + TRI_H) * col
        );

        triangle.setSize(TRI_W, TRI_H);

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


