
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


package com.twistral.kithinite.core;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Nest<T extends Nest<T>> extends Piece<T> {

    protected List<Piece<?>> pieces;

    public Nest() {
        super(false);
        this.pieces = new ArrayList<>();
    }

    /*///////////  ADD  ///////////*/

    public T add(Piece<?> piece) {
        this.pieces.add(piece);
        piece.nester = this;
        return self();
    }

    public T add(Piece<?>... pieces) {
        for (Piece<?> p : pieces) {
            this.add(p);
        }
        return self();
    }

    /*///////////  REMOVE  ///////////*/

    public T remove(Piece<?> piece) {
        if (piece.nester == this && this.pieces.contains(piece)) {
            this.pieces.remove(piece);
            piece.nester = null;
        }
        return self();
    }

    public T remove(Piece<?>... pieces) {
        for (Piece<?> p : pieces) {
            this.remove(p);
        }
        return self();
    }

    public T remove(Collection<? extends Piece<?>> pieces) {
        for (Piece<?> p : pieces) {
            this.remove(p);
        }
        return self();
    }

    public T clear() {
        List<Piece<?>> toRemove = new ArrayList<>(this.pieces);
        for (Piece<?> p : toRemove) {
            this.remove(p);
        }
        return self();
    }


    /*///////////  UTILITY  ///////////*/

    public List<Piece<?>> getPieces() {
        return pieces;
    }

    public int getPieceCount() {
        return this.pieces.size();
    }

}
