# SSIDO Client
SSIDO client implemented as Android app

## Description

## Quick start

### Prerequisites
- Android Studio 4.2+

### Configuration
The app configuration is specified in the jssi.wallet.WalletConstants class. By default, the external storage directory is /.indy_client/wallet/sovereign_wallet/.
The jssi.resolver.HttpResolver configuration is set as follows:
```
DEFAULT_RESOLVE_URI = URI.create("http://localhost:8080/resolver/1.0/identifiers/");
DEFAULT_PROPERTIES_URI = URI.create("http://localhost:8080/resolver/1.0/properties");
```
### Building
Compile, deploy and run the SSIDO Client app on Android 6.0 (api level 23), at least.


