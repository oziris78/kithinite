
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
import com.twistral.kithinite.shapes.Ellipse;
import com.twistral.kithinite.core.Layer;
import com.twistral.kithinite.TestUtils;


public class EllipseDemo extends ApplicationAdapter {

    private static final float RAD_A = 60, RAD_B = 40, PADDING = 25;
    private static final int MAX_PER_ROW = 4;

    private Layer layer;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(900, 450);

        layer = new Layer();

        final Color color1 = Color.GOLD;
        final Color color2 = Color.ORANGE;
        final Color color3 = Color.PURPLE;

        layer.getRoot().add(
            // filled + color settings test
            ellipse(true).setColor(color1),
            ellipse(true).setColor(color2, color3),
            ellipse(false).setColor(color1),
            ellipse(false).setColor(color2, color3),

            // lineWidth test with all the above
            ellipse(true).setColor(color1).setLineWidth(4f),
            ellipse(true).setColor(color2, color3).setLineWidth(4f),
            ellipse(false).setColor(color1).setLineWidth(4f),
            ellipse(false).setColor(color2, color3).setLineWidth(4f),

            ellipse(true).setColor(color1).setLineWidth(8f),
            ellipse(true).setColor(color2, color3).setLineWidth(8f),
            ellipse(false).setColor(color1).setLineWidth(8f),
            ellipse(false).setColor(color2, color3).setLineWidth(8f),

            ellipse(true).setColor(color1).setLineWidth(20f),
            ellipse(true).setColor(color2, color3).setLineWidth(20f),
            ellipse(false).setColor(color1).setLineWidth(20f),
            ellipse(false).setColor(color2, color3).setLineWidth(20f),

            // rotation test (should spill)
            ellipse(true).setColor(color1).setRotationDegrees(30f),
            ellipse(true).setColor(color2, color3).setRotationDegrees(30f),
            ellipse(false).setColor(color1).setRotationDegrees(30f),
            ellipse(false).setColor(color2, color3).setRotationDegrees(30f),

            // vertical ellipse test
            ellipse(true).setColor(color1).setRadiusX(RAD_B).setRadiusY(RAD_A).addY(PADDING),
            ellipse(false).setColor(color1).setRadiusX(RAD_B).setRadiusY(RAD_A).addY(5f*PADDING)
        );
    }

    private static int row = 0, col = 0;

    private Ellipse ellipse(boolean filled) {
        Ellipse ellipse = new Ellipse(filled, RAD_A, RAD_B, null);

        ellipse.setXY(
            PADDING + (PADDING + 2*RAD_A) * row,
            PADDING + (PADDING + 2*RAD_B) * col
        );

        if (++col >= MAX_PER_ROW) {
            col = 0;
            row++;
        }

        return ellipse;
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


