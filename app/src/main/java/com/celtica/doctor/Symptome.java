package com.celtica.doctor;

import androidx.annotation.Nullable;

public class Symptome extends Identifier {
    String name;
    public Symptome(int id) {
        super(id);
    }

    public Symptome(int id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
       if(!(obj instanceof Symptome)){
           return false;
       }else {
           Symptome s=(Symptome)obj;
           return (s.name.equals(name) && s.id== id);
       }
    }

    @Override
    public int hashCode() {
        return name.length()/2;
    }
}
