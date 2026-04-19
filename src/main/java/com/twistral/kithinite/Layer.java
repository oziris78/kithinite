
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

import java.util.Objects;
import java.util.Optional;


public class Layer {

    // Static variables
    public static final Color DEF_BG_COLOR = new Color(0x181818ff);

    // Rendering Toolkit (TODO: put these into a Window/Application class)
    private final Viewport viewport;
    private final ShapeDrawer drawer;
    private final SpriteBatch batch;
    private Texture pixmapTexture;

    // Layer Objects
    private float width, height;
    private Color bgColor;
    private Nest root;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    public Layer(Nest root, float width, float height, Color bgColor) {
        Objects.requireNonNull(root, "root");
        this.root = root;
        this.root.setSize(width, height);

        this.width = width;
        this.height = height;
        this.bgColor = (bgColor != null) ? bgColor : DEF_BG_COLOR;

        this.batch = new SpriteBatch();
        this.viewport = new ScreenViewport();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        this.pixmapTexture = new Texture(pixmap);
        TextureRegion textureRegion = new TextureRegion(this.pixmapTexture, 0, 0, 1, 1);
        this.drawer = new ShapeDrawer(this.batch, textureRegion);
        pixmap.dispose();

        this.resize((int) width, (int) height);
    }


    public Layer() {
        this(new NullNest(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), DEF_BG_COLOR);
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    public void update(float dt) {
        this.root.layout();
    }


    public void render() {
        batch.begin();

        if (this.bgColor != null) {
            drawer.filledRectangle(0f, 0f, this.width, this.height, this.bgColor);
        }

        this.root.render(drawer);

        batch.end();
    }


    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        this.root.setSize(this.width, this.height);

        viewport.update(width, height, true);
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

    public Layer setBgColor(Color bgColor) { this.bgColor = bgColor; return this; }
    public Layer setHeight(float height) { this.height = height; return this; }
    public Layer setWidth(float width) { this.width = width; return this; }
    public Layer setRoot(Nest root) { this.root = root; return this; }

    public Color getBgColor() { return bgColor; }
    public float getHeight() { return height; }
    public float getWidth() { return width; }
    public Nest getRoot() { return root; }

}
