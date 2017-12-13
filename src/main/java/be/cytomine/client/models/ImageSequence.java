package be.cytomine.client.models;

/*
 * Copyright (c) 2009-2016. Authors: see NOTICE file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import be.cytomine.client.CytomineException;

/**
 * User: lrollus
 * Date: 9/01/13
 * GIGA-ULg
 */
public class ImageSequence extends Model<ImageSequence> {

    public ImageSequence(){}
    public ImageSequence(Long idImageGroup, Long idImage, Integer zStack, Integer slice, Integer time, Integer channel){
        this.set("imageGroup", idImageGroup);
        this.set("image", idImage);
        this.set("zStack", zStack);
        this.set("slice", slice);
        this.set("time", time);
        this.set("channel", channel);
    }
}
