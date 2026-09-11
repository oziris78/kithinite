
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
import com.twistral.kithinite.Circle;
import com.twistral.kithinite.Layer;
import com.twistral.kithinite.Rectangle;
import com.twistral.kithinite.TestUtils;
import com.twistral.tempest.TempestUtils;
import com.twistral.tephrium.prng.SplitMix64Random;


public class CircleVerifier extends ApplicationAdapter {

    private static final int WIN_SIZE = 600, WIN_PAD = 20;
    private SplitMix64Random rng = new SplitMix64Random();

    private static final Color BLEED_COLOR = new Color(0xdd000077),
                               IMPERFECT_COLOR = new Color(0xdddd0077),
                               CORRECT_COLOR = new Color(0x00dd0077);

    private static final Color RECT_COLOR = Color.DARK_GRAY,
                               BG_COLOR = Color.BLACK;

    private static final int PACKED_RECT_COLOR = Color.rgba8888(RECT_COLOR),
                             PACKED_BG_COLOR = Color.rgba8888(BG_COLOR);

    private Layer layer;
    private Rectangle rectangle;
    private Circle circle;

    private static int logCount = 1;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(WIN_SIZE, WIN_SIZE);

        layer = new Layer();
        layer.setBgColor(BG_COLOR);

        rectangle = new Rectangle(true, RECT_COLOR);
        rectangle.setXY(WIN_PAD, WIN_PAD).setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);

        circle = new Circle(rng.nextBoolean(), 100f, null, null, 1f);
        circle.setXY(WIN_PAD, WIN_PAD);
        circle.setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);

        randomizeCircleColors();

        layer.getRoot().add(rectangle, circle);
    }



    @Override
    public void render() {
        TempestUtils.clear();

        // Color modes
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) makeCircleFilled1Color();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) makeCircleFilled2Color();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) makeCircleOutlined();

        // Randomize shape properties
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) randomizeCircleSize();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) randomizeCircleLineWidth();

        // Randomize EVERYTHING
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            randomizeCircleSize();
            randomizeCircleLineWidth();
            randomizeCircleColors();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) logInfo();

        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();

        // Auto verify everything
        if (rectangle.getColor() == RECT_COLOR) {
            Color c = verifyRenderedPixels();
            if (c == IMPERFECT_COLOR) System.out.println(logCount++ + "- IMPERFECT");
            if (c == BLEED_COLOR) System.out.println(logCount++ + "- BLEED");
            rectangle.setColor(c);
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


    /*/////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  RANDOMIZERS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////////*/


    private void randomizeCircleSize() {
        rectangle.setColor(RECT_COLOR);

        final float ORIG_SIZE = (int) rng.nextFloat(50f, WIN_SIZE - 2 * WIN_PAD);

        rectangle.setSize(ORIG_SIZE, ORIG_SIZE);
        circle.setXY(WIN_PAD, WIN_PAD).setSize(ORIG_SIZE, ORIG_SIZE);

        // Randomly setSize to 0,0 and unset it back
        if (rng.nextBoolean()) {
            circle.setSize(0f, 0f);
            circle.setSize(ORIG_SIZE, ORIG_SIZE);
        }

        // Make sure resizing never fucks up the original size etc.
        for (int unused = 0; unused < 15; unused++) {
            circle.setSize(rng.nextInt(-200, 2000), rng.nextInt(-200, 2000));
            circle.setSize(ORIG_SIZE, ORIG_SIZE);
        }
    }

    private void randomizeCircleLineWidth() {
        circle.setLineWidth(rng.nextInt(1, 17));
    }

    private void randomizeCircleColors() {
        rectangle.setColor(RECT_COLOR);

        int rand = rng.nextInt(0, 3);
        if (rand == 0) makeCircleFilled2Color();
        else if (rand == 1) makeCircleFilled1Color();
        else makeCircleOutlined();
    }

    private void makeCircleFilled2Color() {
        rectangle.setColor(RECT_COLOR);

        circle.setFilled(true);
        circle.setColor(
            new Color(rng.nextFloat(0.7f, 1f), rng.nextFloat(0.7f, 1f),
                      rng.nextFloat(0.7f, 1f), rng.nextFloat(0.5f, 1f)),
            new Color(rng.nextFloat(0f, 0.4f), rng.nextFloat(0f, 0.4f),
                      rng.nextFloat(0f, 0.4f), rng.nextFloat(0.5f, 1f))
        );
    }

    private void makeCircleOutlined() {
        rectangle.setColor(RECT_COLOR);

        circle.setFilled(false);
        circle.setColor(
            new Color(rng.nextFloat(), rng.nextFloat(0.6f, 1f),
                      rng.nextFloat(0.6f, 1f), rng.nextFloat(0.5f, 1f))
        );
    }

    private void makeCircleFilled1Color() {
        rectangle.setColor(RECT_COLOR);

        circle.setFilled(true);
        circle.setColor(
            new Color(rng.nextFloat(), rng.nextFloat(0.7f, 1f),
                      rng.nextFloat(0.7f, 1f), rng.nextFloat(0.5f, 1f))
        );
    }


    /*////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  TEST FUNCS  ///////////////////////////*/
    /*////////////////////////////////////////////////////////////////////*/


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
        System.out.printf("circle.radius = %.2f\n", circle.getRadius());
        System.out.printf("circle.lineWidth = %.2f\n", circle.getLineWidth());
        System.out.printf("circle.x = %.2f\n", circle.getX());
        System.out.printf("circle.y = %.2f\n", circle.getY());
        System.out.printf("circle.width = %.2f\n", circle.getWidth());
        System.out.printf("circle.height = %.2f\n", circle.getHeight());
        System.out.println("--------------------------------");
    }


}


