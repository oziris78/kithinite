
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

import java.util.Objects;

public abstract class Piece {

    private final boolean isWidget;

    protected Nest nester;

    protected int x, y;        // local coords
    protected int absX, absY;  // absolute coords

    protected int width, height;
    protected boolean visible;

    public Piece(boolean isWidget) {
        this.isWidget = isWidget;
        this.x = 0;
        this.y = 0;
        this.absX = 0;
        this.absY = 0;
        this.width = 0;
        this.height = 0;
        this.visible = true;
        this.nester = null;
    }

    public abstract void layout();

    public void render(ShapeDrawer drawer) {
        if (this.visible)
            this.renderInternal(drawer);
    }

    protected abstract void renderInternal(ShapeDrawer drawer);


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

    public Piece setVisible(boolean visible) { this.visible = visible; return this; }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return visible; }


    /*////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  OBJECT METHODS  ///////////////////////////*/
    /*////////////////////////////////////////////////////////////////////////*/

    @Override
    public String toString() {
        return "Piece{" + "isWidget=" + isWidget + ", nester=" + nester +
            ", x=" + x + ", y=" + y + ", absX=" + absX + ", absY=" + absY +
            ", width=" + width + ", height=" + height + ", visible=" + visible + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return isWidget == piece.isWidget && x == piece.x
                && y == piece.y && absX == piece.absX &&
                absY == piece.absY && width == piece.width
                && height == piece.height && visible ==
                piece.visible && Objects.equals(nester, piece.nester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isWidget, nester, x, y, absX, absY, width, height, visible);
    }

}
