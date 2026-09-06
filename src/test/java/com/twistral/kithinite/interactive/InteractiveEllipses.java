
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



package com.twistral.kithinite.interactive;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.twistral.kithinite.Ellipse;
import com.twistral.kithinite.Layer;
import com.twistral.kithinite.Rectangle;
import com.twistral.tempest.TempestUtils;
import com.twistral.tephrium.prng.SplitMix64Random;


public class InteractiveEllipses extends ApplicationAdapter {

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
    private Ellipse ellipse;

    private static int logCount = 1;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Interactive Ellipse Test");
        Gdx.graphics.setWindowedMode(WIN_SIZE, WIN_SIZE);

        layer = new Layer();
        layer.setBgColor(BG_COLOR);

        rectangle = new Rectangle(true, RECT_COLOR);
        rectangle.setXY(WIN_PAD, WIN_PAD).setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);

        ellipse = new Ellipse(rng.nextBoolean(), 100f, 100f, null, null, 0f, 1f);
        ellipse.setXY(WIN_PAD, WIN_PAD);
        ellipse.setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);

        randomizeEllipseColors();

        layer.getRoot().add(rectangle, ellipse);
    }



    @Override
    public void render() {
        TempestUtils.clear();

        // Color modes
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) makeEllipseFilled1Color();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) makeEllipseFilled2Color();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) makeEllipseOutlined();

        // Randomize shape properties
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) randomizeEllipseSize();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) randomizeEllipseLineWidth();

        // Randomize EVERYTHING
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            randomizeEllipseSize();
            randomizeEllipseLineWidth();
            randomizeEllipseColors();
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


    private void randomizeEllipseSize() {
        rectangle.setColor(RECT_COLOR);

        final float ORIG_WIDTH = (int) rng.nextFloat(50f, WIN_SIZE - 2 * WIN_PAD);
        final float ORIG_HEIGHT = (int) rng.nextFloat(50f, WIN_SIZE - 2 * WIN_PAD);

        rectangle.setSize(ORIG_WIDTH, ORIG_HEIGHT);
        ellipse.setXY(WIN_PAD, WIN_PAD).setSize(ORIG_WIDTH, ORIG_HEIGHT);

        // Randomly setSize to 0,0 and unset it back
        if (rng.nextBoolean()) {
            ellipse.setSize(0f, 0f);
            ellipse.setSize(ORIG_WIDTH, ORIG_HEIGHT);
        }

        // Make sure resizing never fucks up the original size etc.
        for (int unused = 0; unused < 15; unused++) {
            ellipse.setSize(rng.nextInt(-200, 2000), rng.nextInt(-200, 2000));
            ellipse.setSize(ORIG_WIDTH, ORIG_HEIGHT);
        }
    }

    private void randomizeEllipseLineWidth() {
        ellipse.setLineWidth(rng.nextInt(1, 17));
    }

    private void randomizeEllipseColors() {
        rectangle.setColor(RECT_COLOR);

        int rand = rng.nextInt(0, 3);
        if (rand == 0) makeEllipseFilled2Color();
        else if (rand == 1) makeEllipseFilled1Color();
        else makeEllipseOutlined();
    }

    private void makeEllipseFilled2Color() {
        rectangle.setColor(RECT_COLOR);

        ellipse.setFilled(true);
        ellipse.setColor(
            new Color(rng.nextFloat(0.7f, 1f), rng.nextFloat(0.7f, 1f),
                      rng.nextFloat(0.7f, 1f), rng.nextFloat(0.5f, 1f)),
            new Color(rng.nextFloat(0f, 0.4f), rng.nextFloat(0f, 0.4f),
                      rng.nextFloat(0f, 0.4f), rng.nextFloat(0.5f, 1f))
        );
    }

    private void makeEllipseOutlined() {
        rectangle.setColor(RECT_COLOR);

        ellipse.setFilled(false);
        ellipse.setColor(
            new Color(rng.nextFloat(), rng.nextFloat(0.6f, 1f),
                      rng.nextFloat(0.6f, 1f), rng.nextFloat(0.5f, 1f))
        );
    }

    private void makeEllipseFilled1Color() {
        rectangle.setColor(RECT_COLOR);

        ellipse.setFilled(true);
        ellipse.setColor(
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
        System.out.printf("ellipse.radiusX = %.2f\n", ellipse.getRadiusX());
        System.out.printf("ellipse.radiusY = %.2f\n", ellipse.getRadiusY());
        System.out.printf("ellipse.lineWidth = %.2f\n", ellipse.getLineWidth());
        System.out.printf("ellipse.rotationDegrees = %.2f\n", ellipse.getRotationDegrees());
        System.out.printf("ellipse.x = %.2f\n", ellipse.getX());
        System.out.printf("ellipse.y = %.2f\n", ellipse.getY());
        System.out.printf("ellipse.width = %.2f\n", ellipse.getWidth());
        System.out.printf("ellipse.height = %.2f\n", ellipse.getHeight());
        System.out.println("--------------------------------");
    }


}


