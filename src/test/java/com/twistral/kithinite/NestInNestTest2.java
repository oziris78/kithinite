
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
import com.badlogic.gdx.graphics.Color;
import com.twistral.tempest.TempestUtils;


public class NestInNestTest2 extends ApplicationAdapter {

    private Layer layer;
    private PinNest root, n1, n2;
    private Piece w1, w2, w3, wA, wK, wL, wB;

    @Override
    public void create() {
        root = new PinNest();
        root.setXY(10, 20);

        w1 = rect(new Color(0x00ffffff), 20, 20);
        w2 = rect(new Color(0x00eeeeff), 80, 80);
        w3 = rect(new Color(0x00ddddff), 500, 500);

        n1 = new PinNest();
        n1.setXY(20, 100);
        wA = rect(new Color(0xff00ffff), 10, 10);
        wB = rect(new Color(0xee00ffff), 60, 60);

        n2 = new PinNest();
        wK = rect(new Color(0xffff00ff), 20, 20);
        wL = rect(new Color(0xeeee00ff), 200, 200);

        root.add(w1, w2, n1, w3);
        n1.add(wA, n2, wB);
        n2.add(wK, wL);

        layer = new Layer();
        layer.setRoot(root);
    }

    private static Piece rect(Color c, int x, int y) {
        return new Rectangle(true, c).setXY(x, y).setSize(50, 50);
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }

    @Override
    public void render() {
        TempestUtils.clear();
        final float dt = Gdx.graphics.getDeltaTime();
        layer.update(dt);

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

        if (root.getAbsX() != 10) throw new RuntimeException("Wrong root.getAbsX() value");
        if (root.getAbsY() != 20) throw new RuntimeException("Wrong root.getAbsY() value");
        if (w1.getAbsX()   != 30) throw new RuntimeException("Wrong w1.getAbsX() value");
        if (w1.getAbsY()   != 40) throw new RuntimeException("Wrong w1.getAbsY() value");
        if (w2.getAbsX()   != 90) throw new RuntimeException("Wrong w2.getAbsX() value");
        if (w2.getAbsY()   != 100) throw new RuntimeException("Wrong w2.getAbsY() value");
        if (n1.getAbsX()   != 30) throw new RuntimeException("Wrong n1.getAbsX() value");
        if (n1.getAbsY()   != 120) throw new RuntimeException("Wrong n1.getAbsY() value");
        if (wA.getAbsX()   != 40) throw new RuntimeException("Wrong wA.getAbsX() value");
        if (wA.getAbsY()   != 130) throw new RuntimeException("Wrong wA.getAbsY() value");
        if (n2.getAbsX()   != 30) throw new RuntimeException("Wrong n2.getAbsX() value");
        if (n2.getAbsY()   != 120) throw new RuntimeException("Wrong n2.getAbsY() value");
        if (wK.getAbsX()   != 50) throw new RuntimeException("Wrong wK.getAbsX() value");
        if (wK.getAbsY()   != 140) throw new RuntimeException("Wrong wK.getAbsY() value");
        if (wL.getAbsX()   != 230) throw new RuntimeException("Wrong wL.getAbsX() value");
        if (wL.getAbsY()   != 320) throw new RuntimeException("Wrong wL.getAbsY() value");
        if (wB.getAbsX()   != 90) throw new RuntimeException("Wrong wB.getAbsX() value");
        if (wB.getAbsY()   != 180) throw new RuntimeException("Wrong wB.getAbsY() value");
        if (w3.getAbsX()   != 510) throw new RuntimeException("Wrong w3.getAbsX() value");
        if (w3.getAbsY()   != 520) throw new RuntimeException("Wrong w3.getAbsY() value");
        System.out.println("Everything is correct!");

        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


