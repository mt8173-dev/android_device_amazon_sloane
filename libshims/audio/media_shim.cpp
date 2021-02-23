#include <media/mediarecorder.h>

using namespace android;

//MediaRecorder(const String16& opPackageName);
extern "C" void _ZN7android13MediaRecorderC1ERKNS_8String16E(const String16& opPackageName);

extern "C" void _ZN7android13MediaRecorderC1Ev() {
	const String16 opPackageName("<Unknown>");
	_ZN7android13MediaRecorderC1ERKNS_8String16E(opPackageName);
}
