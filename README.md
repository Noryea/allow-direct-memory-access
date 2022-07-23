# allow-direct-memory-access
Some critical code paths will be allowed to use direct memory access for performance. This often greatly reduces CPU overhead for chunk and entity rendering, but can make it harder to diagnose some bugs and crashes.
