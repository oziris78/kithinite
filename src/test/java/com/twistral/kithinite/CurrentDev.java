
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


public class CurrentDev extends ApplicationAdapter {

    private Layer layer;

    @Override
    public void create() {
        layer = new Layer();
        layer.addRectangleToLayer(Color.OLIVE, 20, 20, 100, 100);
        layer.addRectangleToLayer(Color.RED,   30, 30, 100, 100);
        layer.addRectangleToLayer(Color.WHITE, 40, 40, 100, 100);
        layer.addRectangleToLayer(Color.BLUE,  50, 50, 100, 100);
        layer.addRectangleToLayer(Color.BROWN, 60, 60, 100, 100);
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }

    @Override
    public void render() {
        final float dt = Gdx.graphics.getDeltaTime();
        layer.update(dt);
        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


