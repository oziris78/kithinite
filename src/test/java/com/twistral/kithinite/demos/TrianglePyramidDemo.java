
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


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.twistral.kithinite.Layer;
import com.twistral.kithinite.TestUtils;
import com.twistral.kithinite.Triangle;
import com.twistral.tempest.*;
import com.twistral.tephrium.prng.*;
import java.util.*;


public class TrianglePyramidDemo extends ApplicationAdapter {

    private static final int WIN_SIZE = 600, WIN_PAD = 40;

    private final List<Triangle> triangles = new ArrayList<>();
    private Layer layer;

    private SplitMix64Random rng = new SplitMix64Random();


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(WIN_SIZE, WIN_SIZE);

        layer = new Layer();
        regenPyramid();
    }


    @Override
    public void render() {
        TempestUtils.clear();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) regenPyramid();

        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();
    }


    private void regenPyramid() {
        // Create a random palette
        final int paletteSize = rng.nextInt(4, 9);

        final Color[] palette = new Color[paletteSize];
        for (int i = 0; i < palette.length; i++) {
            palette[i] = new Color().fromHsv(
                rng.nextFloat(0f, 360f), rng.nextFloat(0.4f, 0.8f), rng.nextFloat(0.5f, 0.9f)
            );
        }

        // Remove all existing triangles
        layer.getRoot().remove(triangles);
        triangles.clear();

        // Randomize pyramid properties
        int baseUprightCount = rng.nextInt(4, 17);
        boolean fake3D = rng.nextBoolean();

        // Rebuild the pyramid
        final float totalW = WIN_SIZE - 2 * WIN_PAD;
        final float totalH = WIN_SIZE - 2 * WIN_PAD;

        final float triW = totalW / baseUprightCount;
        final float triH = totalH / baseUprightCount;
        final float startX = WIN_PAD;
        final float startY = WIN_PAD;

        for (int row = 0; row < baseUprightCount; row++) {
            final float rowY = startY + (baseUprightCount - 1 - row) * triH;
            final float rowStartX = startX + (baseUprightCount - 1 - row) * (triW / 2f);

            for (int col = 0; col <= row; col++) {
                final float x1 = rowStartX + col * triW;
                final float y1 = rowY;
                final float x2 = x1 + triW;
                final float y2 = rowY;
                final float x3 = x1 + (triW / 2f);
                final float y3 = rowY + triH;

                addTriangle(palette, fake3D, true, x1, y1, x2, y2, x3, y3);
            }

            for (int col = 0; col < row; col++) {
                final float x1 = rowStartX + (col * triW) + (triW / 2f);
                final float y1 = rowY + triH;
                final float x2 = x1 + triW;
                final float y2 = rowY + triH;
                final float x3 = x1 + (triW / 2f);
                final float y3 = rowY;

                addTriangle(palette, fake3D, false, x1, y1, x2, y2, x3, y3);
            }
        }
    }


    private void addTriangle(Color[] palette, boolean fake3D, boolean up,
                             float x1, float y1, float x2, float y2, float x3, float y3)
    {
        Color randColor = palette[rng.nextInt(0, palette.length)].cpy()
                .mul(rng.nextFloat(0.5f, 1f), rng.nextFloat(0.5f, 1f), rng.nextFloat(0.5f, 1f), 1f);

        if (!fake3D) {
            Triangle tri = new Triangle(true, x1, y1, x2, y2, x3, y3, randColor);
            trackTriangle(tri);
            return;
        }

        // In an upright triangle:
        //   - left face is brighter than original
        //   - bottom face is a little darker
        //   - right face is shadowed
        // In an inverted triangle:
        //   - bottom face is brighter than original
        //   - left face is a little darker
        //   - right face is shadowed
        final Color colorBright = randColor.cpy().mul(1.2f, 1.2f, 1.2f, 1f);
        final Color colorDarker = randColor.cpy().mul(0.8f, 0.8f, 0.8f, 1f);
        final Color colorShadow = randColor.cpy().mul(0.6f, 0.6f, 0.6f, 1f);

        final float cx = (x1 + x2 + x3) / 3f;
        final float cy = (y1 + y2 + y3) / 3f;

        if (up) {
            trackTriangle(
                new Triangle(true, x1, y1, x3, y3, cx, cy, colorBright),  // left
                new Triangle(true, x1, y1, x2, y2, cx, cy, colorDarker),  // bottom
                new Triangle(true, x2, y2, x3, y3, cx, cy, colorShadow)   // right
            );
        }
        else {
            trackTriangle(
                new Triangle(true, x1, y1, x2, y2, cx, cy, colorBright),  // bottom
                new Triangle(true, x1, y1, x3, y3, cx, cy, colorDarker),  // left
                new Triangle(true, x2, y2, x3, y3, cx, cy, colorShadow)   // right
            );
        }
    }


    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }


    @Override
    public void dispose() {
        layer.dispose();
    }


    private void trackTriangle(Triangle... tris) {
        for (Triangle tri : tris) {
            triangles.add(tri);
            layer.getRoot().add(tri);
        }
    }


}

