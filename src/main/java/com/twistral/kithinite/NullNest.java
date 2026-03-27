
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


public class NullNest extends Nest {

    @Override
    protected void layout() {
        if (!this.visible) return;

        // Absolute (absX, absY) coord calculation for this nest
        boolean isRootNest = (this.nester == null);
        this.absX = (isRootNest ? 0 : this.nester.absX) + this.x;
        this.absY = (isRootNest ? 0 : this.nester.absY) + this.y;

        // Relative (x,y) coord calculation for owned pieces
        for (Piece piece : this.pieces) {
            if (!piece.isVisible()) continue;

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

}
