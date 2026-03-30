
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

import java.util.HashMap;

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
        this.absX = (isRootNest ? 0f : this.nester.absX) + this.x;
        this.absY = (isRootNest ? 0f : this.nester.absY) + this.y;

        // Relative (x,y) coord calculation for owned pieces
        for (Piece piece : this.pieces) {
            if (!piece.isVisible()) continue;

            final Pin pin = this.pins.get(piece);

            if (pin != null) {
                final boolean eastPinned = pin.isEastPinned();
                final boolean westPinned = pin.isWestPinned();
                final boolean northPinned = pin.isNorthPinned();
                final boolean southPinned = pin.isSouthPinned();
                final float nestWidth = this.width, nestHeight = this.height;

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
        private float north, south, east, west;
        private boolean northPinned, southPinned, eastPinned, westPinned;

        private Pin() {
            this.northPinned = false;
            this.southPinned = false;
            this.eastPinned = false;
            this.westPinned = false;
        }

        /*////////////////////  PINNING  ////////////////////*/

        public Pin north(float north) { this.north = north; northPinned = true; return this; }
        public Pin south(float south) { this.south = south; southPinned = true; return this; }
        public Pin east(float east) { this.east = east; eastPinned = true; return this; }
        public Pin west(float west) { this.west = west; westPinned = true; return this; }

        public Pin all(float x) { return horizontal(x).vertical(x); }
        public Pin horizontal(float x) { return east(x).west(x); }
        public Pin vertical(float x) { return north(x).south(x); }

        /*////////////////////  UNPINNING  ////////////////////*/

        public Pin unpinNorth() { this.northPinned = false; return this; }
        public Pin unpinSouth() { this.southPinned = false; return this; }
        public Pin unpinEast() { this.eastPinned = false; return this; }
        public Pin unpinWest() { this.westPinned = false; return this; }

        public Pin unpinAll() { return unpinHorizontal().unpinVertical(); }
        public Pin unpinHorizontal() { return unpinEast().unpinWest(); }
        public Pin unpinVertical() { return unpinNorth().unpinSouth(); }

        /*////////////////////  GETTERS  ////////////////////*/

        public boolean isNorthPinned() { return this.northPinned; }
        public boolean isSouthPinned() { return this.southPinned; }
        public boolean isEastPinned() { return this.eastPinned; }
        public boolean isWestPinned() { return this.westPinned; }

        public float getNorth() { return this.north; }
        public float getSouth() { return this.south; }
        public float getEast() { return this.east; }
        public float getWest() { return this.west; }

    }

}
