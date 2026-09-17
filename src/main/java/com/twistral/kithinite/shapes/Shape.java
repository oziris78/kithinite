
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


import com.badlogic.gdx.graphics.Color;
import com.twistral.kithinite.core.Piece;
import com.twistral.kithinite.core.Widget;


public abstract class Shape<T extends Shape<T>> extends Widget<T> {

    // Static variables
    public static final Color DEF_COLOR = Color.WHITE;
    public static final float DEF_LINE_WIDTH = 1f;
    public static final float DEF_ROTATION_DEGREES = 0f;

    // Shape properties
    protected boolean filled;

    protected Shape(boolean filled) {
        this.filled = filled;
    }

    public T setFilled(boolean filled) {
        this.filled = filled;
        return self();
    }

    public boolean isFilled() { return filled; }

}

