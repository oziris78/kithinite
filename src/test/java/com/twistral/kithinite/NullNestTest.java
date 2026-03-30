
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


public class NullNestTest extends ApplicationAdapter {

    private Layer layer;


    @Override
    public void create() {
        layer = new Layer();
        int scale = 4;
        int i = 10*scale, s = 30*scale, inc = 10*scale;
            
        layer.getRoot().add(new Rectangle(new Color(0xff0000ff)).setSize(s,s).setXY(i,i)); i += inc;
        layer.getRoot().add(new Rectangle(new Color(0xff7f00ff)).setSize(s,s).setXY(i,i)); i += inc;
        layer.getRoot().add(new Rectangle(new Color(0xffff00ff)).setSize(s,s).setXY(i,i)); i += inc;
        layer.getRoot().add(new Rectangle(new Color(0x00ff00ff)).setSize(s,s).setXY(i,i)); i += inc;
        layer.getRoot().add(new Rectangle(new Color(0x0000ffff)).setSize(s,s).setXY(i,i)); i += inc;
        layer.getRoot().add(new Rectangle(new Color(0x4b0082ff)).setSize(s,s).setXY(i,i)); i += inc;
        layer.getRoot().add(new Rectangle(new Color(0x9400d3ff)).setSize(s,s).setXY(i,i));
    }

    @Override
    public void resize(int width, int height) {
        layer.resize(width, height);
    }


    @Override
    public void render() {
        layer.update(Gdx.graphics.getDeltaTime());
        layer.render();
    }

    @Override
    public void dispose() {
        layer.dispose();
    }

}


