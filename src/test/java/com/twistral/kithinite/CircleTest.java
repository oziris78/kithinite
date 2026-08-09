
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
import com.twistral.kithinite.runtime.Layer;
import com.twistral.kithinite.shapes.Circle;


public class CircleTest extends ApplicationAdapter {

    private static final float RAD = 50, PADDING = 20;
    private static final int MAX_PER_ROW = 4;

    private Layer layer;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Circles");
        Gdx.graphics.setWindowedMode(500, 500);

        layer = new Layer();

        final Color color1 = Color.GOLD;
        final Color color2 = Color.ORANGE;
        final Color color3 = Color.PURPLE;

        layer.getRoot().add(
            // filled + color settings test
            circle(true).setColor(color1),
            circle(true).setColor(color2, color3),
            circle(false).setColor(color1),
            circle(false).setColor(color2, color3),

            // lineWidth test with all the above
            circle(true).setColor(color1).setLineWidth(4f),
            circle(true).setColor(color2, color3).setLineWidth(4f),
            circle(false).setColor(color1).setLineWidth(4f),
            circle(false).setColor(color2, color3).setLineWidth(4f),

            circle(true).setColor(color1).setLineWidth(8f),
            circle(true).setColor(color2, color3).setLineWidth(8f),
            circle(false).setColor(color1).setLineWidth(8f),
            circle(false).setColor(color2, color3).setLineWidth(8f),

            circle(true).setColor(color1).setLineWidth(20f),
            circle(true).setColor(color2, color3).setLineWidth(20f),
            circle(false).setColor(color1).setLineWidth(20f),
            circle(false).setColor(color2, color3).setLineWidth(20f)
        );
    }

    private static int row = 0, col = 0;

    private Circle circle(boolean filled) {
        Circle circle = new Circle(filled, RAD, null);

        circle.setXY(
            PADDING + (PADDING + 2*RAD) * row,
            PADDING + (PADDING + 2*RAD) * col
        );

        if (++col >= MAX_PER_ROW) {
            col = 0;
            row++;
        }

        return circle;
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


