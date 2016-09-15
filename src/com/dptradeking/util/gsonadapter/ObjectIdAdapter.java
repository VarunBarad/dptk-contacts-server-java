package com.dptradeking.util.gsonadapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Creator: vbarad
 * Date: 2016-08-30
 * Project: DP-TradeKING
 */
public class ObjectIdAdapter extends TypeAdapter<ObjectId> {
  @Override
  public void write(JsonWriter jsonWriter, ObjectId objectId) throws IOException {
    jsonWriter.value(objectId.toHexString());
  }
  
  @Override
  public ObjectId read(JsonReader jsonReader) throws IOException {
    String hexString;
    if (jsonReader.peek().equals(JsonToken.BEGIN_OBJECT)) {
      jsonReader.beginObject();
      jsonReader.nextName();
      hexString = jsonReader.nextString();
      jsonReader.endObject();
    } else {
      hexString = jsonReader.nextString();
    }
    return new ObjectId(hexString);
  }
}
