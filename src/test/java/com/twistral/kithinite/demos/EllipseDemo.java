
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

    private static final Color c1 = Color.GOLD, c2 = Color.ORANGE, c3 = Color.PURPLE;
    private static final int SCALE = 15;
    private static final int MAX_PER_ROW = 8;

    private static final float RAD_A = 5*SCALE, RAD_B = 2*SCALE, PADDING = 1.25f*SCALE;

    private Layer layer;


    @Override
    public void create() {
        TestUtils.setTitleFromClass(this);
        Gdx.graphics.setWindowedMode(1500, 700);

        layer = new Layer();

        layer.getRoot().add(
            // filled + color settings + lineWidth test
            ellipse(true).setColor(c1),
            ellipse(true).setColor(c2, c3),
            ellipse(false).setColor(c1),
            ellipse(false).setColor(c2, c3),
            ellipse(true).setColor(c1).setLineWidth(4f),
            ellipse(true).setColor(c2, c3).setLineWidth(4f),
            ellipse(false).setColor(c1).setLineWidth(4f),
            ellipse(false).setColor(c2, c3).setLineWidth(4f),

            ellipse(true).setColor(c1).setLineWidth(8f),
            ellipse(true).setColor(c2, c3).setLineWidth(8f),
            ellipse(false).setColor(c1).setLineWidth(8f),
            ellipse(false).setColor(c2, c3).setLineWidth(8f),
            ellipse(true).setColor(c1).setLineWidth(20f),
            ellipse(true).setColor(c2, c3).setLineWidth(20f),
            ellipse(false).setColor(c1).setLineWidth(20f),
            ellipse(false).setColor(c2, c3).setLineWidth(20f),

            // Flipping vertically test
            ellipse(true).setColor(c1).flipVertically(),
            ellipse(true).setColor(c2, c3).flipVertically(),
            ellipse(false).setColor(c1).flipVertically(),
            ellipse(false).setColor(c2, c3).flipVertically(),
            ellipse(true).setColor(c1).setLineWidth(4f).flipVertically(),
            ellipse(true).setColor(c2, c3).setLineWidth(4f).flipVertically(),
            ellipse(false).setColor(c1).setLineWidth(4f).flipVertically(),
            ellipse(false).setColor(c2, c3).setLineWidth(4f).flipVertically(),

            // Flipping horizontally test
            ellipse(true).setColor(c1).flipHorizontally(),
            ellipse(true).setColor(c2, c3).flipHorizontally(),
            ellipse(false).setColor(c1).flipHorizontally(),
            ellipse(false).setColor(c2, c3).flipHorizontally(),
            ellipse(true).setColor(c1).setLineWidth(4f).flipHorizontally(),
            ellipse(true).setColor(c2, c3).setLineWidth(4f).flipHorizontally(),
            ellipse(false).setColor(c1).setLineWidth(4f).flipHorizontally(),
            ellipse(false).setColor(c2, c3).setLineWidth(4f).flipHorizontally(),

            // rotation test (should spill)
            ellipse(true).setColor(c1).setRotationDegrees(15f),
            ellipse(true).setColor(c2, c3).setRotationDegrees(15f),
            ellipse(false).setColor(c1).setRotationDegrees(15f),
            ellipse(false).setColor(c2, c3).setRotationDegrees(15f),
            ellipse(true).setColor(c1).setLineWidth(4f).setRotationDegrees(15f),
            ellipse(true).setColor(c2, c3).setLineWidth(4f).setRotationDegrees(15f),
            ellipse(false).setColor(c1).setLineWidth(4f).setRotationDegrees(15f),
            ellipse(false).setColor(c2, c3).setLineWidth(4f).setRotationDegrees(15f),

            // Rotation + Flipping vertically test (should spill)
            ellipse(true).setColor(c1).setRotationDegrees(15f).flipVertically(),
            ellipse(true).setColor(c2, c3).setRotationDegrees(15f).flipVertically(),
            ellipse(false).setColor(c1).setRotationDegrees(15f).flipVertically(),
            ellipse(false).setColor(c2, c3).setRotationDegrees(15f).flipVertically(),
            ellipse(true).setColor(c1).setLineWidth(4f).setRotationDegrees(15f).flipVertically(),
            ellipse(true).setColor(c2, c3).setLineWidth(4f).setRotationDegrees(15f).flipVertically(),
            ellipse(false).setColor(c1).setLineWidth(4f).setRotationDegrees(15f).flipVertically(),
            ellipse(false).setColor(c2, c3).setLineWidth(4f).setRotationDegrees(15f).flipVertically(),

            // Rotation + Flipping horizontally test (should spill)
            ellipse(true).setColor(c1).setRotationDegrees(15f).flipHorizontally(),
            ellipse(true).setColor(c2, c3).setRotationDegrees(15f).flipHorizontally(),
            ellipse(false).setColor(c1).setRotationDegrees(15f).flipHorizontally(),
            ellipse(false).setColor(c2, c3).setRotationDegrees(15f).flipHorizontally(),
            ellipse(true).setColor(c1).setLineWidth(4f).setRotationDegrees(15f).flipHorizontally(),
            ellipse(true).setColor(c2, c3).setLineWidth(4f).setRotationDegrees(15f).flipHorizontally(),
            ellipse(false).setColor(c1).setLineWidth(4f).setRotationDegrees(15f).flipHorizontally()
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


