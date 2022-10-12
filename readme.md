# Habari
## Purpose
Habari is a java application doing nothing by itself.\
It takes its force when it's linked to wonderful plugins to extends the multiples capabilities.

## Configuration
### Sample
```json
{
  "configurations": [
    {
      "allowGet": true,
      "allowPost": false,
      "allowPut": false,
      "allowDelete": false,
      "defaultPageSize": 0,
      "servers": [
        {
          "allowPost": true,
          "allowPut": true,
          "allowDelete": true,
          "secured": false,
          "host": "127.0.0.1",
          "port": 8080,
          "configurationUri": "/configuration",
          "metadataUri": "/metadata",
          "uri": "/"
        },
        {
          "secured": false,
          "host": "0.0.0.0",
          "port": 8080
        }
      ],
      "connections": []
    }
  ]
}
```

### Explanation
For the connections part, please refer to the desired plugin(s).\
You can find a list of all available plugins at [this place](https://github.com/Qazima/habari-plugin-core).

When knowing java and in the case the plugin doesn't already exist, feel free to take a look at existing plugins to create yours.

To block the configuration access, just leave blank the value in the configuration file.

## Status
[![Build](https://github.com/Qazima/habari-core/actions/workflows/maven-build.yml/badge.svg)](https://github.com/Qazima/habari-core/actions/workflows/maven-build.yml)
[![Publish Package](https://github.com/Qazima/habari-core/actions/workflows/maven-publish-package.yml/badge.svg)](https://github.com/Qazima/habari-core/actions/workflows/maven-publish-package.yml)