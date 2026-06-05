
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
import space.earlygrey.shapedrawer.JoinType;


public class CurrentDev extends ApplicationAdapter {

    private static final float PADDING = 20;
    private static final int MAX_PER_ROW = 4;

    private Layer layer;


    @Override
    public void create() {
        Gdx.graphics.setTitle("Triangles #1");
        Gdx.graphics.setWindowedMode(800, 600);

        layer = new Layer();


        Triangle tri1 = new Triangle(false, 10, 10, 20, 10, 15, 20, Color.RED);
        tri1.setXY(50, 50);
        tri1.setSize(200, 100);

        Triangle tri2 = new Triangle(false, 10, 10, 20, 10, 15, 20, Color.RED, 16f);
        tri2.setXY(400, 50);
        tri2.setSize(200, 100);

        layer.getRoot().add(
                tri1,tri2
        );
    }

    @Override
    public void render() {
        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();
    }


    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }


    @Override
    public void dispose() {
        layer.dispose();
    }

}


