
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


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Nest extends Piece {

    protected List<Piece> pieces;

    public Nest() {
        super(false);
        this.pieces = new ArrayList<>();
    }

    public void add(Piece piece) {
        this.pieces.add(piece);
        piece.nester = this;
    }

    public void remove(Piece piece) {
        if (piece.nester == this && this.pieces.contains(piece)) {
            this.pieces.remove(piece);
            piece.nester = null;
        }
    }

    public void add(Piece... pieces) {
        for (Piece p : pieces) {
            this.add(p);
        }
    }

    public void remove(Piece... pieces) {
        for (Piece p : pieces) {
            this.remove(p);
        }
    }

}
