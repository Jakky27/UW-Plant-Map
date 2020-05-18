#!/bin/bash

if [[ -z "${GMAPS_API_KEY}" ]]; then
    echo "Warning: GMAPS_API_KEY variable is not set. Consider adding it to the C/I configuration."
fi

cat > ~/plantmap/secrets.properties << EOF
mapsApiKey=${GMAPS_API_KEY}
EOF
