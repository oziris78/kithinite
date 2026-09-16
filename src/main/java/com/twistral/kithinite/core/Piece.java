
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


package com.twistral.kithinite.core;


import space.earlygrey.shapedrawer.ShapeDrawer;


public abstract class Piece<T extends Piece<T>> {

    /** Equals true if this piece is a widget, false if it's a nest. */
    private final boolean isWidget;

    /**
     * The nester of this piece, can be null if this piece is
     * the root nest or a widget that hasnt been placed into a nest yet.
     */
    protected Nest<?> nester;

    protected float x, y;           // local coords
    protected float absX, absY;     // absolute coords that gets set automatically during Piece#layout
    protected float width, height;

    protected boolean visible;


    public Piece(boolean isWidget) {
        this.isWidget = isWidget;
        this.nester = null;
        this.x = 0f;
        this.y = 0f;
        this.absX = 0f;
        this.absY = 0f;
        this.width = 0f;
        this.height = 0f;
        this.visible = true;
    }


    // Responsible for setting x, y, absX, absY, width, height
    public abstract void layout();

    // Responsible for rendering itself and any piece that it may contain
    public abstract void render(ShapeDrawer drawer);


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    // Main setters
    public T setVisible(boolean visible) { this.visible = visible; return self(); }
    public T setX(float x) { this.x = x; return self(); }
    public T setY(float y) { this.y = y; return self(); }
    public T setWidth(float width) { this.width = width; return self(); }
    public T setHeight(float height) { this.height = height; return self(); }

    // Utility setters
    public T setXY(float x, float y) { return setX(x).setY(y); }
    public T setXY(float xy) { return setXY(xy, xy); }
    public T addX(float x) { return setX(getX() + x); }
    public T addY(float y) { return setY(getY() + y); }
    public T addXY(float x, float y) { return addX(x).addY(y); }
    public T addXY(float xy) { return addX(xy).addY(xy); }
    public T setSize(float width, float height) { return setWidth(width).setHeight(height); }

    // Getters for not read-only fields
    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
    public boolean isVisible() { return this.visible; }
    public boolean isWidget() { return this.isWidget; }
    public boolean isNest() { return !this.isWidget; }

    // Getters for READ ONLY fields
    public Nest<?> getNester() { return nester; }
    public float getAbsX() { return this.absX; }
    public float getAbsY() { return this.absY; }


    /*//////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  HELPER FUNCTIONS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////////*/

    // Helper cast method for fluent subclass method chaining
    @SuppressWarnings("unchecked")
    protected T self() { return (T) this; }

}
