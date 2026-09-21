
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

public final class KithiniteUtils {

    private static  final Color tempColor = new Color();

    // No constructor
    private KithiniteUtils() {}

    public static <T> T prioritySelect(T t1, T t2) {
        if (t1 != null) return t1;
        return t2;
    }

    public static <T> T prioritySelect(T t1, T t2, T t3) {
        if (t1 != null) return t1;
        if (t2 != null) return t2;
        return t3;
    }

    public static <T> T prioritySelect(T t1, T t2, T t3, T t4) {
        if (t1 != null) return t1;
        if (t2 != null) return t2;
        if (t3 != null) return t3;
        return t4;
    }

    public static <T> T prioritySelect(T t1, T t2, T t3, T t4, T t5) {
        if (t1 != null) return t1;
        if (t2 != null) return t2;
        if (t3 != null) return t3;
        if (t4 != null) return t4;
        return t5;
    }

    public static float min(float a, float b, float c) { return Math.min(Math.min(a, b), c); }
    public static float max(float a, float b, float c) { return Math.max(Math.max(a, b), c); }

    public static float getFloatBits(float r, float g, float b, float a) {
        return tempColor.set(r, g, b, a).toFloatBits();
    }

}
