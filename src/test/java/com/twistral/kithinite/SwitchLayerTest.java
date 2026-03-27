
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


public class SwitchLayerTest extends ApplicationAdapter {

    private boolean renderFirstLayer;
    private Layer layerA, layerB;

    @Override
    public void create() {
        this.renderFirstLayer = true;

        layerA = new Layer();
        layerA.setBgColor(Color.YELLOW);

        layerB = new Layer();
        layerB.setBgColor(Color.RED);
    }

    private void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            this.renderFirstLayer = !this.renderFirstLayer;
        }

        layerA.update(dt);
        layerB.update(dt);
    }

    @Override
    public void render() {
        TempestUtils.clear();
        update(Gdx.graphics.getDeltaTime());

        if (renderFirstLayer) {
            layerA.render();
        }
        else {
            layerB.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        layerA.resize(width, height);
        layerB.resize(width, height);
    }

    @Override
    public void dispose() {
        layerA.dispose();
        layerB.dispose();
    }

}


