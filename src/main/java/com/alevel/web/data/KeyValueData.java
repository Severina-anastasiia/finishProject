package com.alevel.web.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeyValueData <K, V>{

    K key;
    V value;

    public KeyValueData(K key, V value){
        this.key = key;
        this.value = value;
    }
}
