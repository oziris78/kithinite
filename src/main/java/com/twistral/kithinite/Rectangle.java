
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


import com.badlogic.gdx.graphics.Color;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Objects;


public class Rectangle extends Widget {

    private Color color;
    private boolean filled;

    public Rectangle(Color color, boolean filled) {
        this.color = color;
        this.filled = filled;
    }

    public Rectangle(Color color) {
        this(color, true);
    }


    @Override
    protected void renderInternal(ShapeDrawer drawer) {
        final int drawX = nester.x + this.x;
        final int drawY = nester.y + this.y;

        if (filled) {
            drawer.filledRectangle(drawX, drawY, width, height, color);
        }
        else {
            drawer.rectangle(drawX, drawY, width, height, color);
        }
    }


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    public Rectangle setFilled(boolean filled) { this.filled = filled; return this; }
    public Rectangle setColor(Color color) { this.color = color; return this; }

    public boolean isFilled() { return filled; }
    public Color getColor() { return color; }


}
