package com.i5mc.netproxy;

import com.google.common.collect.ImmutableList;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by on 2017/7/9.
 */
@EqualsAndHashCode(of = "id")
public class MXProxySelector extends ProxySelector {

    private final Pattern scheme = Pattern.compile("http(s)?");
    private final UUID id = UUID.randomUUID();
    private final Main main;
    private final Selector selector;
    private ProxySelector def;

    MXProxySelector(Main main, Selector selector) {
        this.main = main;
        this.selector = selector;
    }

    void hook() {
        def = ProxySelector.getDefault();
        ProxySelector.setDefault(this);
    }

    void close() {
        if (def == null) throw new IllegalStateException();
        if (ProxySelector.getDefault() == this) {
            ProxySelector.setDefault(def);
        }
    }

    public List<Proxy> select(URI uri) {
        if (scheme.matcher(uri.getScheme()).matches()) {
            val p = selector.select(uri.getHost());
            main.log("Req " + uri + " via " + p);
            return Arrays.asList(p);
        }
        return def.select(uri);
    }

    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        def.connectFailed(uri, sa, ioe);
    }

}
