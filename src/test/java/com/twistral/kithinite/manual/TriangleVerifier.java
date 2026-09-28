
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



package com.twistral.kithinite.manual;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.twistral.kithinite.core.Layer;
import com.twistral.kithinite.shapes.Rectangle;
import com.twistral.kithinite.TestUtils;
import com.twistral.kithinite.shapes.Triangle;
import com.twistral.tempest.TempestUtils;
import com.twistral.tephrium.prng.SplitMix64Random;


public class TriangleVerifier extends ApplicationAdapter {

    private static final int WIN_SIZE = 600, WIN_PAD = 20;
    private SplitMix64Random rng = new SplitMix64Random();

    private static final Color BLEED_COLOR = new Color(0xdd000077),
                               IMPERFECT_COLOR = new Color(0xdddd0077),
                               CORRECT_COLOR = new Color(0x00dd0077);

    private static final Color RECT_COLOR = Color.DARK_GRAY,
                               BG_COLOR = Color.BLACK;

    private static final int PACKED_RECT_COLOR = Color.rgba8888(RECT_COLOR),
                             PACKED_BG_COLOR = Color.rgba8888(BG_COLOR);

    private boolean automaticMode = false;

    private Layer layer;
    private Rectangle rectangle;
    private Triangle triangle;
    private String currentType = "";

    private static int errorCount = 0;
    private static int triangleCount = 0;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(WIN_SIZE, WIN_SIZE);

        layer = new Layer();
        layer.setBgColor(BG_COLOR);

        rectangle = new Rectangle(true, RECT_COLOR);
        rectangle.setXY(WIN_PAD, WIN_PAD).setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);

        triangle = new Triangle(
            rng.nextBoolean(), 0f, 0f, 1f, rng.nextFloat(), rng.nextFloat(), 1f, null
        );
        triangle.setXY(WIN_PAD, WIN_PAD).setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);
        randomizeTriColors();

        layer.getRoot().add(rectangle, triangle);
    }



    @Override
    public void render() {
        TempestUtils.clear();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            automaticMode = !automaticMode;


        // Randomize EVERYTHING until you find a mistake
        if (automaticMode) {
            if (rectangle.getColor() == RECT_COLOR || rectangle.getColor() == CORRECT_COLOR) {
                randomizeTriangleSize();
                randomizeTriColors();
            }
        }
        else {
            // Color modes
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) makeTriFilled1Color();
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) makeTriFilled3Color();
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) makeTriOutlined();

            // Randomize only the vertices
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) randomizeTriangleSize();


            // Randomize EVERYTHING
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                if (rectangle.getColor() == RECT_COLOR || rectangle.getColor() == CORRECT_COLOR) {
                    randomizeTriangleSize();
                    randomizeTriColors();
                }
            }

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F))
            System.out.printf("Tested %d triangles so far (found %d errors)\n", triangleCount, errorCount);

        if (Gdx.input.isKeyJustPressed(Input.Keys.D))
            logInfo();


        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();

        // Auto verify everything
        if (rectangle.getColor() == RECT_COLOR) {
            Color color = verifyRenderedPixels();

            if (color != CORRECT_COLOR) {
                final String errorType = (color == BLEED_COLOR) ? "BLEED" : "IMPERFECT";
                System.out.printf(
                    "[%d, %s] %s (failed for %dth)\n",
                    errorCount++, errorType, currentType, triangleCount
                );
                logInfo(); // Instantly dump coordinates on failure
            }

            rectangle.setColor(color);
        }
    }


    private Color verifyRenderedPixels() {
        final int pad = 3;

        final int px = (int) rectangle.getX() - pad;
        final int py = (int) rectangle.getY() - pad;
        final int pw = (int) rectangle.getWidth() + 2*pad;
        final int ph = (int) rectangle.getHeight() + 2*pad;

        final Pixmap pixmap = Pixmap.createFromFrameBuffer(px, py, pw, ph);

        boolean hitLeft = false, hitRight = false, hitTop = false, hitBottom = false;

        // Coords for the bottom left pixel of the rectangle
        final int minX = pad;
        final int minY = pad;

        // Coords for the top right pixel of the rectangle
        final int maxX = pad + (int) rectangle.getWidth() - 1;
        final int maxY = pad + (int) rectangle.getHeight() - 1;

        for (int x = 0; x < pw; x++) {
            for (int y = 0; y < ph; y++) {
                // Pixmap coord system is y-down, we need to flip y for framebuffer reading
                final int pixelRgba8888 = pixmap.getPixel(x, ph - y - 1);

                final boolean isXInsideRect = (maxX >= x && x >= minX);
                final boolean isYInsideRect = (maxY >= y && y >= minY);
                final boolean isInPaddedArea = !(isXInsideRect && isYInsideRect);

                if (isInPaddedArea) {
                    if (pixelRgba8888 != PACKED_BG_COLOR) {
                        pixmap.dispose();
                        return BLEED_COLOR;
                    }
                }
                else {
                    boolean isOnBottomEdge  = isXInsideRect && (y == minY);
                    boolean isOnTopEdge     = isXInsideRect && (y == maxY);
                    boolean isOnLeftEdge    = isYInsideRect && (x == minX);
                    boolean isOnRightEdge   = isYInsideRect && (x == maxX);
                    boolean isShapePixel = (pixelRgba8888 != PACKED_RECT_COLOR);

                    if (isOnBottomEdge && isShapePixel) hitBottom = true;
                    if (isOnTopEdge    && isShapePixel) hitTop = true;
                    if (isOnRightEdge  && isShapePixel) hitRight = true;
                    if (isOnLeftEdge   && isShapePixel) hitLeft = true;
                }
            }
        }

        pixmap.dispose();
        return (hitBottom && hitTop && hitLeft && hitRight) ? CORRECT_COLOR : IMPERFECT_COLOR;
    }


    private void logInfo() {
        System.out.println("--------------------------------");
        System.out.printf(
                "triangle.vertices = (%.2f, %.2f) (%.2f, %.2f) (%.2f, %.2f)\n",
                triangle.getV1x(), triangle.getV1y(), triangle.getV2x(),
                triangle.getV2y(), triangle.getV3x(), triangle.getV3y()
        );
        System.out.printf("triangle.x = %.2f\n", triangle.getX());
        System.out.printf("triangle.y = %.2f\n", triangle.getY());
        System.out.printf("triangle.width = %.2f\n", triangle.getWidth());
        System.out.printf("triangle.height = %.2f\n", triangle.getHeight());
        System.out.println("--------------------------------");
    }


    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }


    /*//////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  RANDOMIZER FUNCS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////////*/


    private final Runnable[] cases = new Runnable[] {
        this::case0, this::case1, this::case2, this::case3, this::case4,
        this::case5, this::case6, this::case7, this::case8, this::case9,
        this::case10, this::case11, this::case12, this::case13
    };


    private void randomizeTriangleSize() {
        triangleCount++;
        rectangle.setColor(RECT_COLOR);

        // Run random case
        Runnable randomCase = cases[rng.nextInt(0, cases.length)];
        randomCase.run();

        final float ORIG_TRI_W = WIN_SIZE - 2 * WIN_PAD;
        final float ORIG_TRI_H = WIN_SIZE - 2 * WIN_PAD;
        triangle.setXY(WIN_PAD, WIN_PAD).setSize(ORIG_TRI_W, ORIG_TRI_H);

        // Randomly setSize to 0,0 and unset it back
        if (rng.nextBoolean()) {
            triangle.setSize(0f, 0f);
            triangle.setSize(ORIG_TRI_W, ORIG_TRI_H);
        }

        // Randomly do flipping
        for (int i = 0; i < 2; i++) {
            if (rng.nextBoolean()) triangle.flipHorizontally();
            if (rng.nextBoolean()) triangle.flipVertically();
        }

        // Make sure resizing never fucks up the original size etc.
        for (int unused = 0; unused < 15; unused++) {
            triangle.setSize(rng.nextInt(-200, 2000), rng.nextInt(-200, 2000));
            triangle.setSize(ORIG_TRI_W, ORIG_TRI_H);
        }

        // Permute all 6 vertex index orderings (V1, V2, V3)
        int perm = rng.nextInt(0, 6);
        float v1x = triangle.getV1x(), v1y = triangle.getV1y();
        float v2x = triangle.getV2x(), v2y = triangle.getV2y();
        float v3x = triangle.getV3x(), v3y = triangle.getV3y();
        Color c1 = triangle.getV1Color(), c2 = triangle.getV2Color(), c3 = triangle.getV3Color();

        switch (perm) {
            case 0: triangle.setVertices(v1x, v1y, c1, v3x, v3y, c3, v2x, v2y, c2); break; // 1 3 2
            case 1: triangle.setVertices(v2x, v2y, c2, v1x, v1y, c1, v3x, v3y, c3); break; // 2 1 3
            case 2: triangle.setVertices(v2x, v2y, c2, v3x, v3y, c3, v1x, v1y, c1); break; // 2 3 1
            case 3: triangle.setVertices(v3x, v3y, c3, v1x, v1y, c1, v2x, v2y, c2); break; // 3 1 2
            case 4: triangle.setVertices(v3x, v3y, c3, v2x, v2y, c2, v1x, v1y, c1); break; // 3 2 1
            case 5: break; // 1 2 3
        }
    }


    private void randomizeTriColors() {
        triangleCount++;
        switch (rng.nextInt(0, 3)) {
            case 0:  makeTriFilled3Color(); break;
            case 1:  makeTriFilled1Color(); break;
            default: makeTriOutlined(); break;
        }
    }


    private void makeTriFilled3Color() {
        triangleCount++;
        rectangle.setColor(RECT_COLOR);

        triangle.setFilled(true).setColor(
            new Color(rng.nextFloat(0.7f, 1f), rng.nextFloat(0.7f, 1f),
                      rng.nextFloat(0.7f, 1f), rng.nextFloat(0.5f, 1f)),
            new Color(rng.nextFloat(0f, 0.4f), rng.nextFloat(0f, 0.4f),
                      rng.nextFloat(0f, 0.4f), rng.nextFloat(0.5f, 1f)),
            new Color(rng.nextFloat(0.4f, 0.7f), rng.nextFloat(0.4f, 0.7f),
                      rng.nextFloat(0.4f, 0.7f), rng.nextFloat(0.5f, 1f))
        );
    }


    private void makeTriOutlined() {
        triangleCount++;
        rectangle.setColor(RECT_COLOR);

        triangle.setFilled(false).setColor(
            new Color(rng.nextFloat(), rng.nextFloat(0.6f, 1f),
                      rng.nextFloat(0.6f, 1f), rng.nextFloat(0.5f, 1f))
        );
    }


    private void makeTriFilled1Color() {
        triangleCount++;
        rectangle.setColor(RECT_COLOR);

        triangle.setFilled(true).setColor(
            new Color(rng.nextFloat(), rng.nextFloat(0.7f, 1f),
                      rng.nextFloat(0.7f, 1f), rng.nextFloat(0.5f, 1f))
        );
    }



    /*////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  TEST CASES  ///////////////////////////*/
    /*////////////////////////////////////////////////////////////////////*/


    private void case0() {
        currentType = "[CASE-0] Fully randomized";
        triangle.setVertices(
            rng.nextFloat(), rng.nextFloat(), rng.nextFloat(),
            rng.nextFloat(), rng.nextFloat(), rng.nextFloat()
        );
    }


    private void case1() {
        currentType = "[CASE-1] Right-angled triangles";

        float[][] rightAngleTemps = {
            {0f, 0f, 0f, 1f, 1f, 0f},
            {0f, 0f, 0f, 1f, 1f, 1f},
            {0f, 0f, 1f, 0f, 1f, 1f},
            {0f, 1f, 1f, 0f, 1f, 1f}
        };

        float[] t = rightAngleTemps[rng.nextInt(0, rightAngleTemps.length)];
        triangle.setVertices(t[0], t[1], t[2], t[3], t[4], t[5]);
    }


    private void case2() {
        currentType = "[CASE-2] Bounding-box anchored triangles";
        triangle.setVertices( // at least 1 vertex on minX/maxX/minY/maxY
                rng.nextBoolean() ? 0f : rng.nextFloat(), rng.nextBoolean() ? 0f : rng.nextFloat(),
                rng.nextBoolean() ? 1f : rng.nextFloat(), rng.nextBoolean() ? 1f : rng.nextFloat(),
                rng.nextFloat(), rng.nextFloat()
        );
    }


    private void case3() {
        currentType = "[CASE-3] Equilateral-ish templates with <1px jitter";
        final float jitter = rng.nextFloat(-0.05f, 0.05f);
        triangle.setVertices(0f + jitter, 0f, 0.5f + jitter, 1f + jitter, 1f + jitter, 0f);
    }


    private void case4() {
        currentType = "[CASE-4] Negative valued vertices";
        triangle.setVertices(
            rng.nextFloat(-0.2f, 1.2f), rng.nextFloat(-0.2f, 1.2f),
            rng.nextFloat(-0.2f, 1.2f), rng.nextFloat(-0.2f, 1.2f),
            rng.nextFloat(-0.2f, 1.2f), rng.nextFloat(-0.2f, 1.2f)
        );
    }


    private void case5() {
        currentType = "[CASE-5] Isosceles & Equilateral upside-down variants";
        final float topY = 1f;
        final float midX = rng.nextFloat(0.1f, 0.9f);
        triangle.setVertices(0f, topY, 1f, topY, midX, 0f);
    }


    private void case6() {
        currentType = "[CASE-6] Degenerate thin triangles";
        float offset = rng.nextFloat(-0.005f, 0.005f);
        triangle.setVertices(0f, 0f, 0.5f, 0.5f + offset, 1f, 1f);
    }


    private void case7() {
        currentType = "[CASE-7] Axis-aligned right triangles";
        int orientation = rng.nextInt(0, 4);
        switch (orientation) {
            case 0: triangle.setVertices(0f, 0f, 0f, 1f, 1f, 0f); break; // Bottom Left
            case 1: triangle.setVertices(0f, 1f, 0f, 0f, 1f, 1f); break; // Top Left
            case 2: triangle.setVertices(1f, 1f, 0f, 1f, 1f, 0f); break; // Top Right
            case 3: triangle.setVertices(1f, 0f, 0f, 0f, 1f, 1f); break; // Bottom Right
        }
    }


    private void case8() {
        currentType = "[CASE-8] Sub-pixel float boundary noise";
        float n1 = rng.nextFloat(-0.0005f, 0.0005f);
        float n2 = rng.nextFloat(-0.0005f, 0.0005f);
        float n3 = rng.nextFloat(-0.0005f, 0.0005f);
        triangle.setVertices(0f + n1, 0f + n2, 1f + n3, 0f - n1, 0.5f + n2, 1f + n3);
    }


    private void case9() {
        currentType = "[CASE-9] Acute center-peaked triangles";
        float peakX = rng.nextFloat(0.4f, 0.6f);
        triangle.setVertices(0f, 0f, peakX, 1f, 1f, 0f);
    }


    private void case10() {
        currentType = "[CASE-10] Obtuse extended triangles";
        if (rng.nextBoolean())
            triangle.setVertices(0f, 0f, 0.2f, 1f, 1f, 0.1f);
        else
            triangle.setVertices(0f, 0f, 1f, 0.2f, 0.1f, 1f);
    }


    private void case11() {
        currentType = "[CASE-11] Sub-pixel micro triangles";
        float baseMinX = rng.nextFloat(0.1f, 0.8f);
        float baseMinY = rng.nextFloat(0.1f, 0.8f);
        float EPS = 0.001f;
        triangle.setVertices(
                baseMinX, baseMinY,
                baseMinX + EPS, baseMinY + EPS * rng.nextFloat(),
                baseMinX + EPS * rng.nextFloat(), baseMinY + EPS
        );
    }


    private void case12() {
        currentType = "[CASE-12] Not normalized vertices";
        triangle.setVertices(
            rng.nextFloat(-0.5f, -0.01f), rng.nextFloat(1.01f, 1.5f),
            rng.nextFloat(0.2f, 0.8f), rng.nextFloat(-0.5f, -0.01f),
            rng.nextFloat(1.01f, 1.5f), rng.nextFloat(1.01f, 1.5f)
        );
    }


    private void case13() {
        currentType = "[CASE-13] Sliver triangles";
        if (rng.nextBoolean()) {
            currentType += " (Vertical)";
            triangle.setVertices(0f, 0f, 0.001f, 1f, 0.002f, 0f);
        } else {
            currentType += " (Horizontal)";
            triangle.setVertices(0f, 0f, 1f, 0.001f, 0f, 0.002f);
        }
    }


}


