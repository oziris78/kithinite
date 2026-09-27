
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




package com.twistral.kithinite.shapes;


import com.badlogic.gdx.graphics.*;
import com.twistral.kithinite.core.*;
import com.twistral.tephrium.core.functions.TMath;


public abstract class Shape<T extends Shape<T>> extends Widget<T> {

    public static final Color DEF_COLOR = Color.WHITE;
    public static final float DEF_LINE_WIDTH = 1f;
    public static final float DEF_ROTATION_DEGREES = 0f;

    protected boolean filled;
    protected float rotationDegrees;

    protected Shape(boolean filled, float rotationDegrees) {
        this.filled = filled;
        this.rotationDegrees = rotationDegrees;
    }

    public abstract T setColor(Color color);
    public abstract Color getColor();

    public abstract T flipVertically();
    public abstract T flipHorizontally();


    public T setFilled(boolean filled) {
        this.filled = filled;
        return self();
    }

    public T setRotationDegrees(float rotationDegrees) {
        this.rotationDegrees = rotationDegrees;
        return self();
    }

    public boolean isFilled() { return filled; }
    public float getRotationDegrees() { return rotationDegrees; }

    public T setColor(int rgba8888) {
        return this.setColor(new Color(rgba8888));
    }

}

