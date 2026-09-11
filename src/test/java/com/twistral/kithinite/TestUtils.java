
// Copyright 2025-2026 Oğuzhan Topaloğlu
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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.twistral.tephrium.prng.SplitMix64Random;

import java.lang.reflect.Method;
import java.util.Objects;


public final class TestUtils {

    private static SplitMix64Random rng = new SplitMix64Random();

    public static Color randColor() {
        return new Color(rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
    }

    public static void setTitleFromClass(Object obj) {
        String title = obj.getClass().getSimpleName().replaceAll("(?<!^)(?=[A-Z])", " ");
        Gdx.graphics.setTitle(title);
    }

    /** To bypass the "protected" access modifier of Piece#layout inside tests */
    public static void invokeLayout(Piece piece) {
        try {
            Method method = Piece.class.getDeclaredMethod("layout");
            method.setAccessible(true);
            method.invoke(piece);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to invoke layout in invokeLayout", e);
        }
    }

}
