
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


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.twistral.kithinite.*;
import com.twistral.kithinite.core.*;
import com.twistral.kithinite.shapes.*;
import com.twistral.tempest.*;
import com.twistral.tephrium.prng.*;
import java.util.*;
import java.util.function.*;


/**
 * Mode switches:                                     <br>
 * Z - Mode on/off for "random size"                  <br>
 * X - Mode on/off for "automatic testing"            <br>
 * C - Cycle specific test case (-1 to case.length)   <br>
 *                                                    <br>
 * Color modes:                                       <br>
 * 1 - Randomize color "filled 1"                     <br>
 * 2 - Randomize color "filled 3"                     <br>
 * 3 - Randomize color "outlined"                     <br>
 *                                                    <br>
 * Testing:                                           <br>
 * E - Randomize case                                 <br>
 * R - Randomize everything                           <br>
 *                                                    <br>
 * Debugging:                                         <br>
 * D- log triangle to console                         <br>
 * F- log results to console                          <br>
 */
public class TriangleVerifier extends ApplicationAdapter {

    private static final int WIN_SIZE = 700, WIN_PAD = 20;
    private static final int RECT_MAX_W = 650, RECT_MAX_H = 650;

    private static final int TOTAL_PAD = 2 * WIN_PAD;

    private static SplitMix64Random rng = new SplitMix64Random();

    private static final Color BLEED_COLOR = new Color(0xdd000077),
                               IMPERFECT_COLOR = new Color(0xdddd0077),
                               CORRECT_COLOR = new Color(0x00dd0077);

    private static final Color RECT_COLOR = Color.DARK_GRAY,
                               BG_COLOR = Color.BLACK;

    private static final int PACKED_RECT_COLOR = Color.rgba8888(RECT_COLOR),
                             PACKED_BG_COLOR = Color.rgba8888(BG_COLOR);

    private boolean automaticMode = false;
    private boolean randomizeSize = false;

    private Layer layer;
    private Rectangle rectangle;
    private Triangle triangle;

    private static int errorCount = 0;
    private static int triangleCount = 0;
    private static int caseIndex = -1; // -1 for rand, [0, case.length) for specific
    private static int activeCaseIndex = 0;

    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(WIN_SIZE, WIN_SIZE);

        layer = new Layer();
        layer.setBgColor(BG_COLOR);

        rectangle = new Rectangle(true, RECT_COLOR);
        triangle = new Triangle(true, 0f, 0f, 1f, rng.nextFloat(), rng.nextFloat(), 1f, null);
        randomizeCase();
        randomizeTriColors();

        layer.getRoot().add(rectangle, triangle);
    }



    @Override
    public void render() {
        TempestUtils.clear();

        // Mode switches
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            randomizeSize = !randomizeSize;
            System.out.printf(">> Toggled rand size mode to: %s\n", randomizeSize ? "ON" : "OFF");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            automaticMode = !automaticMode;
            System.out.printf(">> Toggled automatic mode to: %s\n", automaticMode ? "ON" : "OFF");
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            caseIndex = (caseIndex + 2) % (caseFuncs.size() + 1) - 1;
            System.out.printf(">> Locked Case: %s (%d/%d)\n",
                    caseIndex == -1 ? "RANDOM" : caseTypes.get(caseIndex),
                    caseIndex+1, caseTypes.size());
        }

        // Randomize EVERYTHING until you find a mistake
        if (automaticMode) {
            randomizeCase();
            randomizeTriColors();
        }
        else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) makeTriFilled1Color();
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) makeTriFilled3Color();
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) makeTriOutlined();

            if (Gdx.input.isKeyPressed(Input.Keys.E)) randomizeCase();

            // Randomize EVERYTHING
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                if (rectangle.getColor() == RECT_COLOR || rectangle.getColor() == CORRECT_COLOR) {
                    randomizeCase();
                    randomizeTriColors();
                }
            }

        }

        // Debugging
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) logTestResults();
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) logInfo();

        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();

        // Auto verify everything
        if (rectangle.getColor() == RECT_COLOR) {
            Color color = verifyRenderedPixels();

            if (color != CORRECT_COLOR) {
                final String errorType = (color == BLEED_COLOR) ? "BLEED" : "IMPERFECT";
                System.out.printf(
                    "[%d, %s] %s (failed for %dth)\n",
                    errorCount++, errorType, caseTypes.get(activeCaseIndex), triangleCount
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


    private void logTestResults() {
        System.out.printf(
            "Tested %5d triangles so far (found %d errors) [AUTO: %s, RAND_SIZE: %s]\n",
            triangleCount, errorCount, automaticMode ? "ON" : "OFF", randomizeSize ? "ON" : "OFF"
        );
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


    private void randomizeCase() {
        triangleCount++;
        rectangle.setColor(RECT_COLOR);

        // Run random case
        activeCaseIndex = (caseIndex != -1) ? caseIndex : rng.nextInt(0, caseFuncs.size());
        caseFuncs.get(activeCaseIndex).accept(triangle);

        // Randomize size of rect and triangle (if randomizeSize is enabled)
        final float RAND_W = randomizeSize ? rng.nextInt(1, RECT_MAX_W - TOTAL_PAD)
                                           : (WIN_SIZE - TOTAL_PAD);

        final float RAND_H = randomizeSize ? rng.nextInt(1, RECT_MAX_H - TOTAL_PAD)
                                           : (WIN_SIZE - TOTAL_PAD);

        rectangle.setXY(WIN_PAD, WIN_PAD).setSize(RAND_W, RAND_H);
        triangle.setXY(WIN_PAD, WIN_PAD).setSize(RAND_W, RAND_H);

        // Randomly setSize to 0,0 and unset it back
        if (rng.nextBoolean()) {
            triangle.setSize(0f, 0f);
            triangle.setSize(RAND_W, RAND_H);
        }

        // Randomly do flipping
        for (int i = 0; i < 2; i++) {
            if (rng.nextBoolean()) triangle.flipHorizontally();
            if (rng.nextBoolean()) triangle.flipVertically();
        }

        // Make sure resizing never fucks up the original size etc.
        for (int unused = 0; unused < 15; unused++) {
            triangle.setSize(rng.nextInt(-200, 2000), rng.nextInt(-200, 2000));
            triangle.setSize(RAND_W, RAND_H);
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


    private static final List<Consumer<Triangle>> caseFuncs = new ArrayList<>(32);
    private static final List<String> caseTypes = new ArrayList<>(32);

    private static void defineCase(String type, Consumer<Triangle> func) {
        caseTypes.add(type);
        caseFuncs.add(func);
    }

    static {
        defineCase("[CASE-0] Fully randomized", TriangleVerifier::case0);
        defineCase("[CASE-1] Right angled template", TriangleVerifier::case1);
        defineCase("[CASE-2] Single point degenerate", TriangleVerifier::case2);
        defineCase("[CASE-3] Negative valued", TriangleVerifier::case3);
        defineCase("[CASE-4] Sliver (Vertical)", TriangleVerifier::case4);
        defineCase("[CASE-5] Sliver (Horizontal)", TriangleVerifier::case5);
        defineCase("[CASE-6] Out of bounds coords", TriangleVerifier::case6);
        defineCase("[CASE-7] Bounding box anchored", TriangleVerifier::case7);
        defineCase("[CASE-8] Collinear line degenerate", TriangleVerifier::case8);
        defineCase("[CASE-9] Upside down obtuse", TriangleVerifier::case9);
        defineCase("[CASE-10] Needle", TriangleVerifier::case10);
        defineCase("[CASE-11] Floating point jitter", TriangleVerifier::case11);
        defineCase("[CASE-12] Diagonal slit", TriangleVerifier::case12);
        defineCase("[CASE-13] Subpixel micro triangles", TriangleVerifier::case13);
        defineCase("[CASE-14] Pseudorandom-ish triangles", TriangleVerifier::case14);
    }


    // "[CASE-0] Fully randomized"
    private static void case0(Triangle triangle) {
        triangle.setVertices(
            rng.nextFloat(), rng.nextFloat(), rng.nextFloat(),
            rng.nextFloat(), rng.nextFloat(), rng.nextFloat()
        );
    }


    // "[CASE-1] Right angled template"
    private static void case1(Triangle triangle) {
        final float[][] rightAngleTemps = {
            {0f, 0f, 0f, 1f, 1f, 0f},
            {0f, 0f, 0f, 1f, 1f, 1f},
            {0f, 0f, 1f, 0f, 1f, 1f},
            {0f, 1f, 1f, 0f, 1f, 1f}
        };

        float[] t = rightAngleTemps[rng.nextInt(0, rightAngleTemps.length)];
        triangle.setVertices(t[0], t[1], t[2], t[3], t[4], t[5]);
    }


    // "[CASE-2] Single point degenerate"
    private static void case2(Triangle triangle) {
        float x = rng.nextFloat(), y = rng.nextFloat();
        triangle.setVertices(x, y, x, y, x, y);
    }


    // "[CASE-3] Negative valued"
    private static void case3(Triangle triangle) {
        triangle.setVertices(
            rng.nextFloat(-2f, 2f), rng.nextFloat(-2f, 2f),
            rng.nextFloat(-2f, 2f), rng.nextFloat(-2f, 2f),
            rng.nextFloat(-2f, 2f), rng.nextFloat(-2f, 2f)
        );
    }


    // "[CASE-4] Sliver (Vertical)"
    private static void case4(Triangle triangle) {
        triangle.setVertices(
            0f, 0f, rng.nextFloat(0.001f, 0.01f), 1f, rng.nextFloat(0.002f, 0.02f), 0f
        );
    }


    // "[CASE-5] Sliver (Horizontal)"
    private static void case5(Triangle triangle) {
        triangle.setVertices(
            0f, 0f, 1f, rng.nextFloat(0.001f, 0.01f), 0f, rng.nextFloat(0.002f, 0.02f)
        );
    }


    // "[CASE-6] Out of bounds coords"
    private static void case6(Triangle triangle) {
        float a = rng.nextFloat(-10f, -5f), b = rng.nextFloat(5f, 15f);
        triangle.setVertices(a, a, b, 0.5f, 0.5f, b);
    }


    // "[CASE-7] Bounding box anchored"
    private static void case7(Triangle triangle) {
        triangle.setVertices( // at least 1 vertex on minX/maxX/minY/maxY
            rng.nextBoolean() ? 0f : rng.nextFloat(), rng.nextBoolean() ? 0f : rng.nextFloat(),
            rng.nextBoolean() ? 1f : rng.nextFloat(), rng.nextBoolean() ? 1f : rng.nextFloat(),
            rng.nextFloat(), rng.nextFloat()
        );
    }


    // "[CASE-8] Collinear line degenerate"
    private static void case8(Triangle triangle) {
        float t = rng.nextFloat();
        triangle.setVertices(0f, 0f, 0.5f, 0.5f, t, t);
    }


    // "[CASE-9] Upside down obtuse"
    private static void case9(Triangle triangle) {
        if (rng.nextBoolean())
            triangle.setVertices(0f, 1f, 1f, 1f, rng.nextFloat(0.1f, 0.9f), 0f);
        else
            triangle.setVertices(0f, 0f, rng.nextFloat(0.2f, 0.4f), 1f, 1f, 0.1f);
    }


    // "[CASE-10] Needle"
    private static void case10(Triangle triangle) {
        float midX = rng.nextFloat(0.45f, 0.55f);
        triangle.setVertices(midX - 0.05f, 0f, midX + 0.05f, 0f, midX, 1f);
    }


    // "[CASE-11] Floating point jitter"
    private static void case11(Triangle triangle) {
        float scale = rng.nextFloat(0f, 0.1f);
        float n1 = rng.nextFloat(-scale, scale);
        float n2 = rng.nextFloat(-scale, scale);
        float n3 = rng.nextFloat(-scale, scale);

        switch (rng.nextInt(0, 5)) {
            case 0: triangle.setVertices(0f + n1, 0f, 0.5f + n2, 1f + n3, 1f + n1, 0f); break;
            case 1: triangle.setVertices(0f, 0f, 0.5f, 0.5f + n1, 1f, 1f); break;
            case 2: triangle.setVertices(0f, 0.5f, 1f, 0.5f, 0.5f, 0.5f + Math.max(n1, 1E-6f)); break;
            case 3: triangle.setVertices(0f + n1, 0f + n2, 1f - n3, 0f + n1, 0.5f + n2, 1f - n3); break;
            case 4: triangle.setVertices(0f + n1, 0f + n2, 1f + n3, 0f - n1, 0.5f + n2, 1f + n3); break;
        }
    }


    // "[CASE-12] Diagonal slit"
    private static void case12(Triangle triangle) {
        triangle.setVertices(0f, 0f, 1f, 1f, rng.nextFloat(0.001f, 0.01f), 0f);
    }


    // "[CASE-13] Subpixel micro triangles"
    private static void case13(Triangle triangle) {
        final float x = rng.nextFloat(0.1f, 0.8f);
        final float y = rng.nextFloat(0.1f, 0.8f);
        final float s = rng.nextFloat(0.001f, 0.1f);
        final float r = rng.nextFloat();
        triangle.setVertices(x, y, x + s, y + s * r, x + s * r, y + s);
    }

    // "[CASE-14] Pseudorandom-ish triangles"
    private static void case14(Triangle triangle) {
        generateRandomVertex();
        final float v1x = tx, v1y = ty;

        float v2x, v2y;
        do {
            generateRandomVertex();
            v2x = tx;
            v2y = ty;
        }
        while (v2x == v1x && v2y == v1y);

        // Generate V3 (ensure distinct from V1 and V2)
        float v3x, v3y;
        do {
            generateRandomVertex();
            v3x = tx;
            v3y = ty;
        }
        while ((v3x == v1x && v3y == v1y) || (v3x == v2x && v3y == v2y));

        triangle.setVertices(v1x, v1y, v2x, v2y, v3x, v3y);
    }


    /*//////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  HELPER FUNCTIONS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////////*/

    private static float tx, ty;

    private static void generateRandomVertex() {
        switch (rng.nextInt(0, 3)) {
            case 0: { // Random Corner
                switch (rng.nextInt(0, 4)) {
                    case 0:  { tx = 0f; ty = 0f; } break; // BL
                    case 1:  { tx = 0f; ty = 1f; } break; // TL
                    case 2:  { tx = 1f; ty = 0f; } break; // BR
                    default: { tx = 1f; ty = 1f; } break; // TR
                }
            } break;
            case 1: { // Random Edge
                switch (rng.nextInt(0, 4)) {
                    case 0:  { tx = rng.nextFloat(); ty = 0f; } break; // B
                    case 1:  { tx = rng.nextFloat(); ty = 1f; } break; // T
                    case 2:  { tx = 0f; ty = rng.nextFloat(); } break; // L
                    default: { tx = 1f; ty = rng.nextFloat(); } break; // R
                }
            } break;
            default: { // Random Quadrant
                switch (rng.nextInt(0, 4)) {
                    case 0:  { tx = rng.nextFloat(0f, 0.5f); ty = rng.nextFloat(0f, 0.5f); } break; // BL
                    case 1:  { tx = rng.nextFloat(0.5f, 1f); ty = rng.nextFloat(0f, 0.5f); } break; // BR
                    case 2:  { tx = rng.nextFloat(0f, 0.5f); ty = rng.nextFloat(0.5f, 1f); } break; // TL
                    default: { tx = rng.nextFloat(0.5f, 1f); ty = rng.nextFloat(0.5f, 1f); } break; // TR
                }
            } break;
        }
    }


}


