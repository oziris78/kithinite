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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class UILayer {

    // Rendering objects
    private Viewport viewport;
    private ShapeDrawer drawer;
    private SpriteBatch batch;

    // Variables
    private int width, height;


    public UILayer(int width, int height) {
        this.width = width;
        this.height = height;
        this.batch = new SpriteBatch();
        this.viewport = new ScreenViewport();

        // Create shapedrawer
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap);
        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 1, 1);
        this.drawer = new ShapeDrawer(this.batch, textureRegion);
        pixmap.dispose();

        // Sync viewport & batch
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    //////////////////////////////////////////////////////////////////////
    ///////////////////////////  MAIN METHODS  ///////////////////////////
    //////////////////////////////////////////////////////////////////////


    public void update(float deltaTime) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            this.width -= 50;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            this.width += 50;
        }
    }

    public void render(float deltaTime) {
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
        this.drawer.getRegion().getTexture().dispose();
        this.batch.dispose();
    }


    /////////////////////////////////////////////////////////////////////
    /////////////////////////////  METHODS  /////////////////////////////
    /////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////////
    /////////////////////////////  GETTERS & SETTERS  /////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////



    //////////////////////////////////////////////////////////////////////////////
    /////////////////////////////  HELPER FUNCTIONS  /////////////////////////////
    //////////////////////////////////////////////////////////////////////////////


}


