
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


public abstract class Widget extends Piece {

    public Widget() {
        super(true);
    }

    @Override
    protected void layout() {
        // All widgets are supposted to be inside a nest (this is Kithinite's assumption)
        if (this.nester == null) {
            throw new KithiniteException(
                "%s must be added to a Nest before layout() is called.", this.getClass().getSimpleName()
            );
        }

        // Absolute coord calculation
        this.absX = this.nester.absX + this.x;
        this.absY = this.nester.absY + this.y;
    }

}
