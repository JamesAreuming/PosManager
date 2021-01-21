package com.jc.pico.bean;

import com.google.gson.JsonObject;

public class Message {
	private JsonObject message = new JsonObject();
	
    public Message(String to, String from, String text) {
        this.message.addProperty("to", to);
        this.message.addProperty("from", from);
        this.message.addProperty("text", text);
    }
    
    public Message(String to, String from, String text, String subject) {
        this.message.addProperty("to", to);
        this.message.addProperty("from", from);
        this.message.addProperty("text", text);
        this.message.addProperty("subject", subject);
        this.message.addProperty("type", "LMS");
    }    
    
    public JsonObject toJson() {
        return this.message;
    }
}