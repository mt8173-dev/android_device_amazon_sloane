#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

extern "C" {
    int atrace_marker_fd     = -1;

    void atrace_begin_body(const char* name)
    {
        char buf[1024];
        size_t len;

        len = snprintf(buf, 1024, "B|%d|%s", getpid(), name);
        write(atrace_marker_fd, buf, len);
    }
}