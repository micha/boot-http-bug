# boot-http bug

To reproduce:

```bash
$ boot dev-once-broken
```

In another shell:

```bash
$ curl localhost:8080/foo.html # should produce <h4>foo</h4>
```

Re-running `boot dev-once-broken` several times produces different
output when requesting the URL with `curl`.
