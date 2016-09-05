# droidcon Vienna 2016 Conference App

Sample app for the "2016 Android Developer Toolbox" talk at droidconAT

## Features

![droidconat2016 Android screenshots][]

* See all sessions
* Manage your schedule
* Consult speakers information
* Receive a notification when selected sessions are about to begin


## Development Environment

This project uses Java 8, Retrolambda, and Lombok.

Don't forget to install the Java 8 JDK, and the lombok plugin for your IDE before trying to build it.


## Run Unit Tests

```bash
./scripts/run-tests-unit.sh
```


## Run Mock Server

```bash
cd mockserver
npm install
node server
```

You can then access it through:

```
http://localhost:8990/
```


## Acknowledgements

This project took some inspiration from the amazing work done for the [droidcon PL 2015 mobile app][], the [Google I/O app][], and the [DroidKaigi 2016 app][]


## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[droidconat2016 Android screenshots]: https://raw.githubusercontent.com/Nilhcem/droidconat-2016/master/assets/screenshots/screenshots.jpg
[droidcon PL 2015 mobile app]: https://github.com/droidconpl/droidcon-2015-mobile-app
[Google I/O app]: https://github.com/google/iosched
[DroidKaigi 2016 app]: https://github.com/konifar/droidkaigi2016/
