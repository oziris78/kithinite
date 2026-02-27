
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
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;


public class BasicExample extends ApplicationAdapter {

    private Layer layer;

    @Override
    public void create() {
        layer = new Layer();
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }

    @Override
    public void render() {
        final float dt = Gdx.graphics.getDeltaTime();
        // Update
        layer.update(dt);
        // Render
        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


