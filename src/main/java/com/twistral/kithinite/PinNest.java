
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PinNest extends Nest {

    private final HashMap<Piece, Pin> pins;

    public PinNest() {
        super();
        this.pins = new HashMap<>(128);
    }

    public Pin pin(Piece piece) {
        if (!this.pins.containsKey(piece)) {
            this.pins.put(piece, new Pin());
        }
        return this.pins.get(piece);
    }

    @Override
    public void layout() {
        for (Piece piece : this.pieces) {
            final Pin pin = this.pins.get(piece);

            if (pin != null) {
                // compute geometry
            }

            piece.layout();
        }

        super.layout();
    }


    @Override
    protected void renderInternal(ShapeDrawer drawer, int offsetX, int offsetY) {
        final int startX = this.x + offsetX;
        final int startY = this.y + offsetY;

        for (Piece p : this.pieces) {
            p.render(drawer, startX, startY);
        }
    }


    /*//////////////////////////////////////////////////////////////////////*/
    /*//////////////////////////  STATIC CLASSES  //////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    public static class Pin {
        private Integer north = null;
        private Integer south = null;
        private Integer east = null;
        private Integer west = null;

        private Pin() {}

        public Pin north(int north) { this.north = north; return this; }
        public Pin south(int south) { this.south = south; return this; }
        public Pin east(int east) { this.east = east; return this; }
        public Pin west(int west) { this.west = west; return this; }

        public Pin all(int x) { return this.north(x).south(x).east(x).west(x); }
        public Pin horizontal(int x) { return this.east(x).west(x); }
        public Pin vertical(int x) { return this.north(x).south(x); }

        public int getNorth() { return north; }
        public int getSouth() { return south; }
        public int getEast() { return east; }
        public int getWest() { return west; }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Pin pin = (Pin) o;
            return Objects.equals(north, pin.north) && Objects.equals(south, pin.south)
                    && Objects.equals(east, pin.east) && Objects.equals(west, pin.west);
        }

        @Override
        public int hashCode() {
            return Objects.hash(north, south, east, west);
        }

        @Override
        public String toString() {
            return "Pin{" + "north=" + north + ", south=" + south +
                    ", east=" + east + ", west=" + west + '}';
        }
    }


}
