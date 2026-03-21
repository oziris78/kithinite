
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
        this.pins = new HashMap<>();
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
                final boolean eastPinned = (pin.east != null);
                final boolean westPinned = (pin.west != null);
                final boolean northPinned = (pin.north != null);
                final boolean southPinned = (pin.south != null);
                final int nestX = this.x, nestY = this.y,
                        nestWidth = this.width, nestHeight = this.height;

                // Horizontal pinning
                if (eastPinned && westPinned) {
                    piece.x = nestX + pin.west;
                    piece.width = nestWidth - pin.east - pin.west; // Perform strecthing
                }
                else if (eastPinned && !westPinned) {
                    piece.x = nestX + nestWidth - pin.east - piece.width;
                }
                else if (!eastPinned && westPinned) {
                    piece.x = nestX + pin.west;
                }

                // Vertical pinning
                if (northPinned && southPinned) {
                    piece.y = nestY + pin.south;
                    piece.height = nestHeight - pin.north - pin.south; // Perform strecthing
                }
                else if (northPinned && !southPinned) {
                    piece.y = nestY + nestHeight - pin.north - piece.height;
                }
                else if (!northPinned && southPinned) {
                    piece.y = nestY + pin.south;
                }
            }

            piece.absX = this.absX + piece.x;
            piece.absY = this.absY + piece.y;
            piece.layout();
        }
    }


    @Override
    protected void renderInternal(ShapeDrawer drawer) {
        for (Piece p : this.pieces) {
            p.render(drawer);
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
    }

}
