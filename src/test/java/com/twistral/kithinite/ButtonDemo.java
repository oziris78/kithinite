
// Copyright 2025 Oğuzhan Topaloğlu
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


import com.badlogic.gdx.*;
import com.twistral.tempest.*;



public class ButtonDemo extends ApplicationAdapter {

    private UILayer layer;

    @Override
    public void create() {
        layer = new UILayer(600, 600);
    }

    @Override
    public void render() {
        final float dt = Gdx.graphics.getDeltaTime();
        layer.update(dt);

        TempestUtils.clear();
        layer.render(dt);
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
