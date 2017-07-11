package com.i5mc.netproxy;

import lombok.val;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by on 2017/7/9.
 */
public class Selector {

    private List<Pair<Pattern, Proxy>> list;

    private Selector() {
    }

    public Proxy select(String host) {
        for (val next : list) {
            if (next.getKey().matcher(host).matches()) {
                return next.getValue();
            }
        }
        return Proxy.NO_PROXY;
    }

    public static Selector build(List<Map<String, ?>> list) {
        if (list == null || list.isEmpty()) throw new IllegalArgumentException("list:" + list);
        val out = new Selector();
        out.list = new ArrayList<>(list.size());
        for (Map<String, ?> map : list) {
            val p = new InetSocketAddress(map.get("host").toString(), ((Number) map.get("port")).intValue());
            val f = (List<String>) map.get("filter");
            if (f.isEmpty()) throw new IllegalArgumentException("filter:" + f);
            val i = new StringBuilder("((.)+\\.)?(");
            val itr = f.iterator();
            while (itr.hasNext()) {
                i.append('(');
                i.append(itr.next().replace(".", "\\."));
                i.append(')');
                if (itr.hasNext()) i.append('|');
            }
            i.append(')');
            out.list.add(new Pair<>(Pattern.compile(i.toString()), new Proxy(Proxy.Type.SOCKS, p)));
        }
        return out;
    }
}
