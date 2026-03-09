
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


import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Piece {

    private final boolean isWidget;
    protected int x, y, width, height;

    public Piece(boolean isWidget) {
        this.isWidget = isWidget;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public abstract void render(ShapeDrawer drawer);


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    public boolean isWidget() { return this.isWidget; }
    public boolean isNest() { return !this.isWidget; }

    public Piece setX(int x) { this.x = x; return this; }
    public Piece setY(int y) { this.y = y; return this; }
    public Piece setXY(int x, int y) { return setX(x).setY(y); }
    public Piece setXY(int xy) { return setXY(xy, xy); }

    public Piece setWidth(int width) { this.width = width; return this; }
    public Piece setHeight(int height) { this.height = height; return this; }
    public Piece setSize(int width, int height) { return setWidth(width).setHeight(height); }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }


}
