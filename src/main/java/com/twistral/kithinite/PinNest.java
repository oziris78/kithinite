
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
    protected void layout() {
        if (!this.visible) return;

        // Absolute (absX, absY) coord calculation for nests should happen immediately
        // since pieces that are owned by this nest are going to use this nest's
        // absolute coords to calculate their absolute coords.
        boolean isRootNest = (this.nester == null);
        this.absX = (isRootNest ? 0 : this.nester.absX) + this.x;
        this.absY = (isRootNest ? 0 : this.nester.absY) + this.y;

        // Relative (x,y) coord calculation for owned pieces
        for (Piece piece : this.pieces) {
            if (!piece.isVisible()) continue;

            final Pin pin = this.pins.get(piece);

            if (pin != null) {
                final boolean eastPinned = (pin.east != null);
                final boolean westPinned = (pin.west != null);
                final boolean northPinned = (pin.north != null);
                final boolean southPinned = (pin.south != null);
                final int nestWidth = this.width, nestHeight = this.height;

                // Horizontal pinning
                if (eastPinned && westPinned) {
                    piece.x = pin.west;
                    piece.width = nestWidth - pin.east - pin.west; // Perform strecthing
                }
                else if (eastPinned && !westPinned) {
                    piece.x = nestWidth - pin.east - piece.width;
                }
                else if (!eastPinned && westPinned) {
                    piece.x = pin.west;
                }

                // Vertical pinning
                if (northPinned && southPinned) {
                    piece.y = pin.south;
                    piece.height = nestHeight - pin.north - pin.south; // Perform strecthing
                }
                else if (northPinned && !southPinned) {
                    piece.y = nestHeight - pin.north - piece.height;
                }
                else if (!northPinned && southPinned) {
                    piece.y = pin.south;
                }
            }

            piece.layout();
        }
    }


    @Override
    protected void render(ShapeDrawer drawer) {
        if (!this.visible) return;

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
