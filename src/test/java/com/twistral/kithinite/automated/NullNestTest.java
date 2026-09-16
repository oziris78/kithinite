
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


package com.twistral.kithinite.automated;


import com.twistral.kithinite.*;
import com.twistral.kithinite.core.Piece;
import com.twistral.kithinite.nests.NullNest;
import com.twistral.kithinite.shapes.Rectangle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.twistral.kithinite.TestUtils.randColor;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class NullNestTest {

    @Test
    @DisplayName("Test NullNest Positioning")
    void testNullNestPositioning() {
        NullNest root = new NullNest();

        final int scale = 4;
        final int s = 30*scale;
        final int inc = 10*scale;

        for (int i = inc; i < 12*inc; i += inc) {
            root.add(new Rectangle(true, randColor()).setSize(s, s).setXY(i, i));
        }

        root.layout();

        int i = inc;
        for (Piece<?> p : root.getPieces()) {
            assertEquals(p.getWidth(), s);
            assertEquals(p.getHeight(), s);
            assertEquals(p.getAbsX(), i);
            assertEquals(p.getAbsY(), i);
            i += inc;
        }
    }


    @Test
    @DisplayName("Test Multilevel Absolute Positioning")
    void testMultilevelAbsolutePositioning() {
        NullNest root = new NullNest();
        root.setXY(10f, 20f);

        NullNest n1 = new NullNest();
        n1.setXY(20f, 100f);

        Rectangle widget = new Rectangle(true, randColor());
        widget.setXY(15f, 25f);

        root.add(n1);
        n1.add(widget);

        root.layout();

        assertEquals(root.getAbsX(), 10f);
        assertEquals(root.getAbsY(), 20f);

        assertEquals(n1.getAbsX(), 30f); // 10 + 20
        assertEquals(n1.getAbsY(), 120f); // 20 + 100

        assertEquals(widget.getAbsX(), 45f); // 30 + 15
        assertEquals(widget.getAbsY(), 145f); // 120 + 25
    }


}
