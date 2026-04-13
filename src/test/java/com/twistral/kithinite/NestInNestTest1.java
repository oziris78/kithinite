
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


public class NestInNestTest1 extends ApplicationAdapter {

    private Layer layer;

    @Override
    public void create() {
        layer = new Layer();

        PinNest nest1 = new PinNest();
        nest1.add(new Rectangle(true, Color.BLUE).setXY(50, 50).setSize(100, 100));  // 150 250
        nest1.add(new Rectangle(true, Color.BROWN).setXY(60, 60).setSize(100, 100)); // 160 260

        PinNest root = new PinNest();
        root.add(new Rectangle(true, Color.OLIVE).setXY(20, 20).setSize(100, 100)); // 120 20
        root.add(new Rectangle(true, Color.RED).setXY(30, 30).setSize(100, 100));   // 130 30
        root.add(new Rectangle(true, Color.WHITE).setXY(40, 40).setSize(100, 100)); // 140 40
        root.add(nest1);

        nest1.setY(200);
        root.setX(100);
        layer.setRoot(root);
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
        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


