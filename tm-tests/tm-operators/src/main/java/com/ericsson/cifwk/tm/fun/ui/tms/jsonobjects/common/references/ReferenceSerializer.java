/*
 * COPYRIGHT Ericsson (c) 2014.
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 */

package com.ericsson.cifwk.tm.fun.ui.tms.jsonobjects.common.references;

import com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.ReferenceHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ReferenceSerializer implements JsonSerializer<Reference>, JsonDeserializer<Identifiable> {

    @Override
    public JsonElement serialize(Reference src, Type type, JsonSerializationContext context) {
        return context.serialize(new ReferenceType(src.getId(), src.getTitle()));
    }

    @Override
    public Identifiable deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        ReferenceType referenceType = context.deserialize(jsonElement, ReferenceType.class);
        return ReferenceHelper.parseEnumById(referenceType.getId(), (Class) type);
    }
}
