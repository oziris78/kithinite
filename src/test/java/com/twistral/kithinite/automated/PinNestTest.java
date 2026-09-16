
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
import com.twistral.kithinite.nests.PinNest;
import com.twistral.kithinite.shapes.Rectangle;
import org.junit.jupiter.api.*;
import static com.twistral.kithinite.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class PinNestTest {

    @Test
    @DisplayName("Test Nested PinNest Positions")
    void testNestedPinNestPositions() {
        final int s = 100;

        Piece<?> r1 = new Rectangle(true, randColor()).setXY(50, 50).setSize(s, s),  // 150 250
              r2 = new Rectangle(true, randColor()).setXY(60, 60).setSize(s, s),  // 160 260
              r3 = new Rectangle(true, randColor()).setXY(20, 20).setSize(s, s),  // 120 20
              r4 = new Rectangle(true, randColor()).setXY(30, 30).setSize(s, s),  // 130 30
              r5 = new Rectangle(true, randColor()).setXY(40, 40).setSize(s, s);  // 140 40

        PinNest nest1 = new PinNest();
        nest1.add(r1, r2);

        PinNest root = new PinNest();
        root.add(r3, r4, r5);
        root.add(nest1);

        nest1.setY(200);
        root.setX(100);

        root.layout();

        // R1 150 250
        assertEquals(r1.getAbsX(), 150f);
        assertEquals(r1.getAbsY(), 250f);

        // R2 160 260
        assertEquals(r2.getAbsX(), 160f);
        assertEquals(r2.getAbsY(), 260f);

        // R3 120 20
        assertEquals(r3.getAbsX(), 120f);
        assertEquals(r3.getAbsY(), 20f);

        // R4 130 30
        assertEquals(r4.getAbsX(), 130f);
        assertEquals(r4.getAbsY(), 30f);

        // R5 140 40
        assertEquals(r5.getAbsX(), 140f);
        assertEquals(r5.getAbsY(), 40f);
    }


    @Test
    @DisplayName("Test Multilevel Absolute Positioning")
    void testMultilevelAbsolutePositioning() {
        PinNest root = new PinNest();
        root.setXY(10, 20);

        Piece<?> w1 = new Rectangle(true, randColor()).setXY(20, 20).setSize(50, 50);
        Piece<?> w2 = new Rectangle(true, randColor()).setXY(80, 80).setSize(50, 50);
        Piece<?> w3 = new Rectangle(true, randColor()).setXY(500, 500).setSize(50, 50);

        PinNest n1 = new PinNest();
        n1.setXY(20, 100);
        Piece<?> wA = new Rectangle(true, randColor()).setXY(10, 10).setSize(50, 50);
        Piece<?> wB = new Rectangle(true, randColor()).setXY(60, 60).setSize(50, 50);

        PinNest n2 = new PinNest();
        Piece<?> wK = new Rectangle(true, randColor()).setXY(20, 20).setSize(50, 50);
        Piece<?> wL = new Rectangle(true, randColor()).setXY(200, 200).setSize(50, 50);

        root.add(w1, w2, n1, w3);
        n1.add(wA, n2, wB);
        n2.add(wK, wL);

        root.layout();

        // "root" x=10, y=20                       => absX=10 , absY=20
        //    |- "w1" x=20, y=20                   => absX=30 , absY=40
        //    |- "w2" x=80, y=80,                  => absX=90 , absY=100
        //    |- "n1" x=20, y=100                  => absX=30 , absY=120
        //          |- "wA" x=10, y=10             => absX=40 , absY=130
        //          |- "n2" x=0, y=0               => absX=30 , absY=120
        //                |- "wK" x=20, y=20       => absX=50 , absY=140
        //                |- "wL" x=200, y=200     => absX=230 , absY=320
        //          |- "wB" x=60, y=60             => absX=90 , absY=180
        //    |- "w3" x=500, y=500                 => absX=510 , absY=520
        //  ............................................................

        // "root" x=10, y=20                       => absX=10 , absY=20
        assertEquals(root.getAbsX(), 10f);
        assertEquals(root.getAbsY(), 20f);
        //    |- "w1" x=20, y=20                   => absX=30 , absY=40
        assertEquals(w1.getAbsX(), 30f);
        assertEquals(w1.getAbsY(), 40f);
        //    |- "w2" x=80, y=80,                  => absX=90 , absY=100
        assertEquals(w2.getAbsX(), 90f);
        assertEquals(w2.getAbsY(), 100f);
        //    |- "n1" x=20, y=100                  => absX=30 , absY=120
        assertEquals(n1.getAbsX(), 30f);
        assertEquals(n1.getAbsY(), 120f);
        //          |- "wA" x=10, y=10             => absX=40 , absY=130
        assertEquals(wA.getAbsX(), 40f);
        assertEquals(wA.getAbsY(), 130f);
        //          |- "n2" x=0, y=0               => absX=30 , absY=120
        assertEquals(n2.getAbsX(), 30f);
        assertEquals(n2.getAbsY(), 120f);
        //                |- "wK" x=20, y=20       => absX=50 , absY=140
        assertEquals(wK.getAbsX(), 50f);
        assertEquals(wK.getAbsY(), 140f);
        //                |- "wL" x=200, y=200     => absX=230 , absY=320
        assertEquals(wL.getAbsX(), 230f);
        assertEquals(wL.getAbsY(), 320f);
        //          |- "wB" x=60, y=60             => absX=90 , absY=180
        assertEquals(wB.getAbsX(), 90f);
        assertEquals(wB.getAbsY(), 180f);
        //    |- "w3" x=500, y=500                 => absX=510 , absY=520
        assertEquals(w3.getAbsX(), 510f);
        assertEquals(w3.getAbsY(), 520f);
    }


    @Test
    @DisplayName("Test Single Edge Pinning")
    void testSingleEdgePinning() {
        PinNest pinNest = new PinNest();
        pinNest.setSize(400f, 300f);

        Rectangle child = new Rectangle(true, randColor());
        child.setSize(50f, 50f);
        pinNest.add(child);

        pinNest.pin(child).east(10f).north(20f);

        pinNest.layout();

        assertEquals(child.getAbsX(), 340f);
        assertEquals(child.getAbsY(), 230f);
    }


    @Test
    @DisplayName("Test Opposing Edge Strecthing")
    void testOpposingEdgeStrecthing() {
        PinNest pinNest = new PinNest();
        pinNest.setSize(400f, 300f);

        Rectangle child = new Rectangle(true, randColor());
        pinNest.add(child);

        pinNest.pin(child).west(20f).east(30f).south(10f).north(15f);

        pinNest.layout();

        assertEquals(child.getX(), 20f);
        assertEquals(child.getWidth(), 350f);

        assertEquals(child.getY(), 10f);
        assertEquals(child.getHeight(), 275f);
    }


    @Test
    @DisplayName("Test Unpinning Pieces")
    void testUnpinningPieces() {
        PinNest pinNest = new PinNest();
        pinNest.setSize(400f, 300f);

        Rectangle child = new Rectangle(false, randColor());
        child.setXY(100f, 100f).setSize(50f, 50f);
        pinNest.add(child);

        pinNest.layout();
        assertEquals(child.getAbsX(), 100f);
        assertEquals(child.getAbsY(), 100f);

        pinNest.pin(child).west(20f);
        child.setXY(100f, 100f);
        pinNest.layout();
        assertEquals(child.getAbsX(), 20f);
        assertEquals(child.getAbsY(), 100f);

        pinNest.pin(child).unpinWest();
        child.setXY(100f, 100f);
        pinNest.layout();
        assertEquals(child.getAbsX(), 100f);
        assertEquals(child.getAbsY(), 100f);
    }


}
