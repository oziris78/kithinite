
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


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.twistral.tempest.*;
import com.twistral.tephrium.prng.*;


public class CurrentDev extends ApplicationAdapter {

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
    private Triangle triangle;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Interactive Triangle Test");
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) randomizeTriVertices();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) makeTriFilled1Color();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) makeTriFilled3Color();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) makeTriOutlined();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            randomizeTriVertices();
            randomizeTriColors();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            logInfoToConsole();
        }

        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            if (rectangle.getColor() == RECT_COLOR) {
                rectangle.setColor(verifyRenderedPixels());
            }
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


    private void randomizeTriVertices() {
        rectangle.setColor(RECT_COLOR);

        triangle.setVertices(0f, 0f, 1f, rng.nextFloat(), rng.nextFloat(), 1f);
        triangle.setXY(WIN_PAD, WIN_PAD).setSize(WIN_SIZE - 2*WIN_PAD, WIN_SIZE - 2*WIN_PAD);
    }

    private void randomizeTriColors() {
        rectangle.setColor(RECT_COLOR);

        int rand = rng.nextInt(0, 3);
        if (rand == 0) makeTriFilled3Color();
        else if (rand == 1) makeTriFilled1Color();
        else makeTriOutlined();
    }

    private void makeTriFilled3Color() {
        rectangle.setColor(RECT_COLOR);

        triangle.setFilled(true);
        triangle.setColor(
            new Color(rng.nextFloat(0.7f, 1f), rng.nextFloat(0.7f, 1f), rng.nextFloat(0.7f, 1f), 1f),
            new Color(rng.nextFloat(0f, 0.4f), rng.nextFloat(0f, 0.4f), rng.nextFloat(0f, 0.4f), 1f),
            new Color(rng.nextFloat(0.4f, 0.7f), rng.nextFloat(0.4f, 0.7f), rng.nextFloat(0.4f, 0.7f), 1f)
        );
    }

    private void makeTriOutlined() {
        rectangle.setColor(RECT_COLOR);

        triangle.setFilled(false);
        triangle.setColor(
            new Color(rng.nextFloat(), rng.nextFloat(0.6f, 1f), rng.nextFloat(0.6f, 1f), 1f)
        );
    }

    private void makeTriFilled1Color() {
        rectangle.setColor(RECT_COLOR);

        triangle.setFilled(true);
        triangle.setColor(
            new Color(1f, rng.nextFloat(0.7f, 1f), rng.nextFloat(0.7f, 1f), 1f)
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
                    boolean isTrianglePixel = (pixelRgba8888 != PACKED_RECT_COLOR);

                    if (isOnBottomEdge && isTrianglePixel) hitBottom = true;
                    if (isOnTopEdge    && isTrianglePixel) hitTop = true;
                    if (isOnRightEdge  && isTrianglePixel) hitRight = true;
                    if (isOnLeftEdge   && isTrianglePixel) hitLeft = true;
                }
            }
        }

        pixmap.dispose();
        return (hitBottom && hitTop && hitLeft && hitRight) ? CORRECT_COLOR : IMPERFECT_COLOR;
    }

    private void logInfoToConsole() {
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


}


