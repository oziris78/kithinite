
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
import com.badlogic.gdx.graphics.Color;
import com.twistral.kithinite.core.Layer;
import com.twistral.kithinite.shapes.Rectangle;
import com.twistral.kithinite.TestUtils;
import space.earlygrey.shapedrawer.JoinType;


public class RectangleDemo extends ApplicationAdapter {

    private static final Color c1 = Color.RED, c2 = Color.BLUE, c3 = Color.GREEN, c4 = Color.CYAN;
    private static final float RECT_HEIGHT = 60, PADDING = 25;
    private static final int MAX_PER_ROW = 8;

    private static final float RECT_WIDTH = 2*RECT_HEIGHT;

    private Layer layer;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(1500, 700);

        layer = new Layer();

        // FILLED => TRUE, FALSE
        // COLOR => COLOR, VERT_GRAD, HOR_GRAD, FULL_GRAD
        // JOINTTYPE => POINTY, SMOOTH, NONE
        // LINEWIDTH => 1F, 4F, 8F
        layer.getRoot().add(
            // POINTY 1F
            rect(true).setColor(c1).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(false).setColor(c1).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.POINTY).setLineWidth(1f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.POINTY).setLineWidth(1f),

            // POINTY 4F
            rect(true).setColor(c1).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(false).setColor(c1).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.POINTY).setLineWidth(4f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.POINTY).setLineWidth(4f),

            // POINTY 8F
            rect(true).setColor(c1).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(false).setColor(c1).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.POINTY).setLineWidth(8f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.POINTY).setLineWidth(8f),

            // SMOOTH 1F
            rect(true).setColor(c1).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(false).setColor(c1).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(1f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(1f),

            // SMOOTH 4F
            rect(true).setColor(c1).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(false).setColor(c1).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(4f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(4f),

            // SMOOTH 8F
            rect(true).setColor(c1).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(false).setColor(c1).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(8f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.SMOOTH).setLineWidth(8f),

            // NONE 1F
            rect(true).setColor(c1).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(false).setColor(c1).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.NONE).setLineWidth(1f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.NONE).setLineWidth(1f),

            // NONE 4F
            rect(true).setColor(c1).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(false).setColor(c1).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.NONE).setLineWidth(4f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.NONE).setLineWidth(4f),

            // NONE 8F
            rect(true).setColor(c1).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(true).setVerticalGradient(c2, c3).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(true).setHorizontalGradient(c3, c2).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(true).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(false).setColor(c1).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(false).setVerticalGradient(c2, c3).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(false).setHorizontalGradient(c3, c2).setJoinType(JoinType.NONE).setLineWidth(8f),
            rect(false).setFullGradient(c1, c4, c3, c2).setJoinType(JoinType.NONE).setLineWidth(8f),

            // EXTRA ROTATION TESTS
            rect(true).setColor(c1, c4, c3, c2).setLineWidth(2f).setRotationDegrees(20),
            rect(true).setColor(c1, c4, c3, c2).setLineWidth(4f).setRotationDegrees(-20),
            rect(true).setColor(c1, c4, c3, c2).setLineWidth(8f).setRotationDegrees(180),
            rect(true).setColor(c1, c4, c3, c2).setLineWidth(1f).setRotationDegrees(360),
            rect(false).setColor(c1, c4, c3, c2).setLineWidth(2f).setRotationDegrees(20),
            rect(false).setColor(c1, c4, c3, c2).setLineWidth(4f).setRotationDegrees(-20),
            rect(false).setColor(c1, c4, c3, c2).setLineWidth(8f).setRotationDegrees(180),
            rect(false).setColor(c1, c4, c3, c2).setLineWidth(1f).setRotationDegrees(360)
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


