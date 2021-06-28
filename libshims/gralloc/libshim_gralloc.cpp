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

    void atrace_async_begin_body(const char* name, int32_t cookie)
    {
        char buf[1024];
        size_t len;

        len = snprintf(buf, 1024, "S|%d|%s|%" PRId32,
            getpid(), name, cookie);

        write(atrace_marker_fd, buf, len);
    }

    void atrace_async_end_body(const char* name, int32_t cookie)
    {
        char buf[1024];
        size_t len;

        len = snprintf(buf, 1024, "F|%d|%s|%" PRId32,
            getpid(), name, cookie);

        write(atrace_marker_fd, buf, len);
    }

    void atrace_int_body(const char* name, int32_t value)
    {
        char buf[1024];
        size_t len;

        len = snprintf(buf, 1024, "C|%d|%s|%" PRId32,
            getpid(), name, value);

        write(atrace_marker_fd, buf, len);
    }

    void atrace_int64_body(const char* name, int64_t value)
    {
        char buf[1024];
        size_t len;

        len = snprintf(buf, 1024, "C|%d|%s|%" PRId64,
            getpid(), name, value);

        write(atrace_marker_fd, buf, len);
    }
}