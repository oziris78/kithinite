
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


public final class Kithinite {

    public static <T> T prioritySelect(T t1, T t2) {
        if (t1 != null) return t1;
        return t2;
    }

    public static <T> T prioritySelect(T t1, T t2, T t3) {
        if (t1 != null) return t1;
        if (t2 != null) return t2;
        return t3;
    }

    public static float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

    public static float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }


}
