
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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.twistral.tempest.TempestUtils;

import java.util.Arrays;


public class PinNestTest extends ApplicationAdapter {

    private Layer layer;
    private PinNest pinNest;

    /* 4 different options: N, S, E, W
        pick 0  =>  no_pins
        pick 1  =>  E, W, N, S
        pick 2  =>  NE, NW, SE, SW, NS, EW
        pick 3  =>  NSE, NSW, NEW, SEW
        pick 4  =>  NSEW
        all combinations: no_pins, E, W, N, S, NE, NW, SE, SW, NS, EW, NSE, NSW, NEW, SEW, NSEW
     */

    private Piece rectNo, rectE, rectW, rectN, rectS, rectNE, rectNW, rectSE, rectSW,
            rectNS, rectEW, rectNSE, rectNSW, rectNEW, rectSEW, rectNSEW;
    private Piece[] allRectangles;
            
    @Override
    public void create() {
        layer = new Layer();

        pinNest = new PinNest();
        layer.setRoot(pinNest);

        rectNo = new Rectangle(Color.BLUE).setXY(200, 200).setSize(50, 50);

        rectE = new Rectangle(Color.RED).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectE).east(20);

        rectW = new Rectangle(Color.GREEN).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectW).west(20);

        rectN = new Rectangle(Color.YELLOW).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectN).north(20);

        rectS = new Rectangle(Color.BROWN).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectS).south(20);

        rectNE = new Rectangle(Color.PINK).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNE).north(20).east(20);

        rectNW = new Rectangle(Color.PURPLE).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNW).north(20).west(20);

        rectSE = new Rectangle(Color.WHITE).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectSE).south(20).east(20);

        rectSW = new Rectangle(Color.ORANGE).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectSW).south(20).west(20);

        rectNS = new Rectangle(Color.SKY).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNS).vertical(20);

        rectEW = new Rectangle(Color.LIME).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectEW).horizontal(20);

        rectNSE = new Rectangle(Color.CORAL).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNSE).north(20).south(20).east(20);
        
        rectNSW = new Rectangle(Color.LIGHT_GRAY).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNSW).north(20).south(20).west(20);
        
        rectNEW = new Rectangle(Color.MAROON).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNEW).north(20).east(20).west(20);
        
        rectSEW = new Rectangle(Color.NAVY).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectSEW).south(20).east(20).west(20);

        rectNSEW = new Rectangle(Color.CYAN).setXY(200, 200).setSize(50, 50);
        pinNest.pin(rectNSEW).all(20);

        pinNest.add(rectNo, rectE, rectW, rectN, rectS, rectNE, rectNW, rectSE, rectSW,
                rectNS, rectEW, rectNSE, rectNSW, rectNEW, rectSEW, rectNSEW);

        allRectangles = new Piece[] {
                rectNo, rectE, rectW, rectN, rectS, rectNE, rectNW, rectSE, rectSW,
                rectNS, rectEW, rectNSE, rectNSW, rectNEW, rectSEW, rectNSEW
        };

        selectRect(rectNo, "No pins (1/16)");
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }

    private void handleKeyInputs() {
        // If you think this copy pasted code is "spagetti code" and
        // "shouldnt be in a serious library", then you can go f* yourself

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectRect(rectNo, "No pins (1/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectRect(rectE, "Current pins: E (2/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            selectRect(rectW, "Current pins: W (3/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            selectRect(rectN, "Current pins: N (4/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            selectRect(rectS, "Current pins: S (5/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            selectRect(rectNE, "Current pins: NE (6/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            selectRect(rectNW, "Current pins: NW (7/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            selectRect(rectSE, "Current pins: SE (8/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            selectRect(rectSW, "Current pins: SW (9/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectRect(rectNS, "Current pins: NS (10/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            selectRect(rectEW, "Current pins: EW (11/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            selectRect(rectNSE, "Current pins: NSE (12/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            selectRect(rectNSW, "Current pins: NSW (13/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Y)) {
            selectRect(rectNEW, "Current pins: NEW (14/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            selectRect(rectSEW, "Current pins: SEW (15/16)"); return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            selectRect(rectNSEW, "Current pins: all (16/16)"); return;
        }
    }

    @Override
    public void render() {
        TempestUtils.clear();
        handleKeyInputs();

        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();
    }

    private void selectRect(Piece rectangle, String newTitle) {
        Arrays.stream(allRectangles).forEach(p -> p.setVisible(false)); // make them all invisible
        rectangle.setVisible(true); // make the selected one visible
        Gdx.graphics.setTitle(newTitle); // refresh the title
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


