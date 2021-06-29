#ifndef _BDROID_BUILDCFG_H
#define _BDROID_BUILDCFG_H

#define BTM_DEF_LOCAL_NAME   "Amazon Fire TV 2"

#define PROC_BTWRITE_TIMER_TIMEOUT_MS = 0

#define BTA_AV_MAX_A2DP_MTU  1788
#define BLE_VND_INCLUDED TRUE
#define BLE_INCLUDED TRUE
#define BTA_GATT_INCLUDED TRUE
#define SMP_INCLUDED TRUE

// Turn off BLE_PRIVACY_SPT.  Remote reconnect fails on
// often if this is enabled.
#define BLE_PRIVACY_SPT FALSE

// Disable HFP
#define BTIF_HF_SERVICES (BTA_HSP_SERVICE_MASK)
#define BTIF_HF_SERVICE_NAMES  { BTIF_HSAG_SERVICE_NAME, NULL }

// To sync with CONN TIMEOUT of pepper
#define BTM_BLE_CONN_TIMEOUT_DEF 1000

#endif