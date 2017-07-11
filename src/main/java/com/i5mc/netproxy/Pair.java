package com.i5mc.netproxy;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by on 2017/7/9.
 */
@AllArgsConstructor
@Data
public class Pair<K, V> {

    private final K key;
    private final V value;
}
