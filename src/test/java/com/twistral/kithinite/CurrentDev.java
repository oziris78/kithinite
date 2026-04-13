
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
import space.earlygrey.shapedrawer.JoinType;


public class CurrentDev extends ApplicationAdapter {

    private static final float RECT_WIDTH = 150, RECT_HEIGHT = 75, PADDING = 25;
    private static final int MAX_PER_ROW = 4;

    private Layer layer;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Rectangles!!!!");
        Gdx.graphics.setWindowedMode(750, 450);

        layer = new Layer();

        layer.getRoot().add(
            rect(true).setColor(Color.RED),
            rect(true).setVerticalGradient(Color.BLUE, Color.GREEN),
            rect(true).setHorizontalGradient(Color.GREEN, Color.BLUE),
            rect(true).setGradient(Color.RED, Color.CYAN, Color.GREEN, Color.BLUE),

            rect(false).setColor(Color.YELLOW),
            rect(false).setGradient(Color.RED, Color.CYAN, Color.GREEN, Color.BLUE),
            rect(false).setJoinType(JoinType.SMOOTH).setColor(Color.YELLOW),
            rect(false).setJoinType(JoinType.NONE).setColor(Color.YELLOW),

            rect(true).setColor(0xff0000ff).setLineWidth(2f).setRotationDegrees(11),
            rect(true).setColor(0xffff00ff).setLineWidth(2f).setRotationDegrees(-11),
            rect(true).setColor(0xff00ffff).setLineWidth(2f).setRotationDegrees(180),
            rect(true).setColor(0x00ffffff).setLineWidth(2f).setRotationDegrees(360),

            rect(false).setColor(Color.RED),
            rect(false).setColor(Color.RED).setLineWidth(2f),
            rect(false).setColor(Color.RED).setLineWidth(4f),
            rect(false).setColor(Color.RED).setLineWidth(16f)
        );
    }


    private static int row = 0, col = 0;

    private Rectangle rect(boolean filled) {
        Rectangle rectangle = new Rectangle(filled, Rectangle.DEF_COLOR);
        rectangle.setX(PADDING + (PADDING+RECT_WIDTH) * row)
                .setY(PADDING + (PADDING+RECT_HEIGHT) * col)
                .setSize(RECT_WIDTH, RECT_HEIGHT);

        if (++col >= MAX_PER_ROW) {
            col = 0;
            row++;
        }

        return rectangle;
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }


    @Override
    public void render() {
        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


