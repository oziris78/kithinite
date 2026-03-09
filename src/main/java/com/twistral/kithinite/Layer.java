
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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.*;


public class Layer {

    // Rendering Toolkit (TODO: put these into a Window/Application class)
    private final Viewport viewport;
    private final ShapeDrawer drawer;
    private final SpriteBatch batch;
    private Texture pixmapTexture;

    // Layer Objects
    private int width, height;
    private Color bgColor;
    private Nest root;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    public Layer(Nest root, int width, int height, Color bgColor) {
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
        this.root = root;

        this.batch = new SpriteBatch();
        this.viewport = new ScreenViewport();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        this.pixmapTexture = new Texture(pixmap);
        TextureRegion textureRegion = new TextureRegion(this.pixmapTexture, 0, 0, 1, 1);
        this.drawer = new ShapeDrawer(this.batch, textureRegion);
        pixmap.dispose();

        this.resize(width, height);
    }

    public Layer(Color bgColor) {
        this(new PinNest(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), bgColor);
    }

    public Layer() {
        this(new PinNest(), Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), new Color(0x181818ff));
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    public void update(float dt) {
        this.root.layout();
    }


    public void render() {
        batch.begin();
        drawer.filledRectangle(0, 0, this.width, this.height, this.bgColor);
        this.root.render(drawer, 0, 0);
        batch.end();
    }


    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        viewport.update(this.width, this.height, true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }


    public void dispose() {
        this.pixmapTexture.dispose();
        this.batch.dispose();
    }

    // TODO: Maybe in the future
    public void pause() {}
    // TODO: Maybe in the future
    public void resume() {}


    /*///////////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  GETTERS & SETTERS  ///////////////////////////*/
    /*///////////////////////////////////////////////////////////////////////////*/

    public Color getBgColor() { return bgColor; }
    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public Nest getRoot() { return root; }

    public void setBgColor(Color bgColor) { this.bgColor = bgColor; }
    public void setHeight(int height) { this.height = height; }
    public void setWidth(int width) { this.width = width; }
    public void setRoot(Nest root) { this.root = root; }
}
