
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


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.viewport.*;
import space.earlygrey.shapedrawer.*;


public class Layer {

    // Rendering Objects (TODO: put these into a Window/Application class)
    private final Viewport viewport;
    private final ShapeDrawer drawer;
    private final SpriteBatch batch;
    private Texture pixmapTexture;

    // Layer Details
    private int width, height;


    /*//////////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  CONSTRUCTORS  ///////////////////////////*/
    /*//////////////////////////////////////////////////////////////////////*/


    public Layer(int width, int height) {
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


    public Layer() {
        this(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    /*/////////////////////////////////////////////////////////////////*/
    /*///////////////////////////  METHODS  ///////////////////////////*/
    /*/////////////////////////////////////////////////////////////////*/


    public void update(float dt) {

    }


    public void render() {
        batch.begin();
        drawer.filledRectangle(0, 0, this.width, this.height, Color.YELLOW);
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


}
