# simisola

A [re-frame](https://github.com/Day8/re-frame) application designed to bring ease and organisation to our everyday life.

## Development Mode

### Run application:

```
lein clean
lein dev
```

shadow-cljs will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:8280](http://localhost:8280).

### Run tests:

```
shadow-cljs watch tests
```

### Create release:

```
lein prod
```
